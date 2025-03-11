package edu.rpi.legup.model;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.save.ExportFileException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract class for exporting puzzle data to XML format. This class provides functionality to
 * export a puzzle object, including its board and tree structure, to an XML file. Subclasses must
 * implement methods to create specific elements for the board and the tree.
 */
public abstract class PuzzleExporter {
    private static final Logger LOGGER = LogManager.getLogger(PuzzleExporter.class.getName());

    protected Puzzle puzzle;

    /**
     * PuzzleExporter Constructor exports the puzzle object to a file
     *
     * @param puzzle puzzle that is to be exported
     */
    public PuzzleExporter(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Takes the puzzle state and the current date/time to obfuscate the solved state with an onto
     * function to prevent cheating
     *
     * @param solved the solved state of the board, true if solved
     * @param date the current date and time, passed during export
     * @return hash value of time and solved state
     */
    public static int obfHash(boolean solved, String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DATE_FORMAT);
        String obf = solved + ":" + dateTime.toEpochSecond(ZoneOffset.UTC) + ";";
        return obf.hashCode();
    }

    /**
     * Deobfuscates the solved state of the board from hash value using the time provided in the
     * puzzle xml-style export
     *
     * @param hash the hash value saved to the export
     * @param date the date/time value saved to the export
     * @return boolean value of the solved state of the board
     */
    public static Boolean inverseHash(int hash, String date) {
        long timestamp;
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, DATE_FORMAT);
            timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            timestamp = -1;
        }

        if ((true + ":" + timestamp + ";").hashCode() == hash) {
            return Boolean.TRUE;
        } else if ((false + ":" + timestamp + ";").hashCode() == hash) {
            return Boolean.FALSE;
        }
        return null;
    }

    /**
     * Exports the puzzle to an xml formatted file
     *
     * @param fileName name of file to be exported
     * @throws ExportFileException if puzzle can not be exported
     */
    public void exportPuzzle(String fileName) throws ExportFileException {
        try {
            // quick patch for ParserConfigurationException thrown
            // when a double quote is placed in the file name
            fileName = fileName.replace("\"", "");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document newDocument = docBuilder.newDocument();

            // hardcoded version number
            org.w3c.dom.Element legupElement = newDocument.createElement("Legup");
            legupElement.setAttribute("version", "6.1.0");
            newDocument.appendChild(legupElement);

            org.w3c.dom.Element puzzleElement = newDocument.createElement("puzzle");
            String idStr =
                    puzzle.getTag().isEmpty()
                            ? fileName.substring(fileName.lastIndexOf("\\") + 1)
                            : puzzle.getTag();
            puzzleElement.setAttribute("tag", idStr);
            puzzleElement.setAttribute("name", puzzle.getName());
            legupElement.appendChild(puzzleElement);

            puzzleElement.appendChild(createBoardElement(newDocument));
            if (puzzle.getTree() != null
                    && !puzzle.getTree().getRootNode().getChildren().isEmpty()) {
                puzzleElement.appendChild(createProofElement(newDocument));
            }

            org.w3c.dom.Element statusElement = newDocument.createElement("solved");
            LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/New_York"));
            String time = dateTime.format(DATE_FORMAT);
            statusElement.setAttribute("lastSaved", time);
            int hashedState = obfHash(puzzle.isPuzzleComplete(), time);
            statusElement.setAttribute("isSolved", hashedState + "");
            legupElement.appendChild(statusElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new ExportFileException("Puzzle Exporter: parser configuration exception");
        }
    }

    /**
     * Creates an XML element representing the board of the puzzle. Subclasses must implement this
     * method to provide the XML structure for the board.
     *
     * @param newDocument The XML document to create elements within.
     * @return An XML element representing the puzzle's board.
     */
    protected abstract Element createBoardElement(Document newDocument);

    /**
     * Creates an XML element representing the proof of the puzzle, including its tree structure.
     * This method is used to generate the proof section of the XML, which contains the tree
     * representation.
     *
     * @param newDocument The XML document to create elements within.
     * @return An XML element representing the proof of the puzzle.
     */
    protected Element createProofElement(Document newDocument) {
        org.w3c.dom.Element proofElement = newDocument.createElement("proof");
        org.w3c.dom.Element treeElement = createTreeElement(newDocument);
        proofElement.appendChild(treeElement);
        return proofElement;
    }

    /**
     * Creates an XML element representing the tree structure of the puzzle. This method traverses
     * the tree nodes and transitions, and creates XML elements for each.
     *
     * @param newDocument The XML document to create elements within.
     * @return An XML element representing the puzzle's tree structure.
     */
    protected Element createTreeElement(Document newDocument) {
        org.w3c.dom.Element treeElement = newDocument.createElement("tree");

        Set<TreeNode> visited = new HashSet<>();
        List<TreeNode> nodes = new ArrayList<>();
        nodes.add(puzzle.getTree().getRootNode());
        while (!nodes.isEmpty()) {
            TreeNode treeNode = nodes.get(nodes.size() - 1);
            nodes.remove(treeNode);
            if (!visited.contains(treeNode)) {
                visited.add(treeNode);

                org.w3c.dom.Element nodeElement = newDocument.createElement("node");
                nodeElement.setAttribute("id", String.valueOf(treeNode.hashCode()));
                if (treeNode.isRoot()) {
                    nodeElement.setAttribute("root", "true");
                }

                for (TreeTransition transition : treeNode.getChildren()) {
                    org.w3c.dom.Element transElement = newDocument.createElement("transition");
                    transElement.setAttribute("id", String.valueOf(transition.hashCode()));

                    TreeNode child = transition.getChildNode();
                    if (child != null) {
                        transElement.setAttribute("child", String.valueOf(child.hashCode()));
                        nodes.add(child);
                    }

                    if (transition.isJustified()) {
                        transElement.setAttribute("rule", transition.getRule().getRuleName());
                        transElement.setAttribute("rule_id", transition.getRule().getRuleID());
                    }

                    for (PuzzleElement data : transition.getBoard().getModifiedData()) {
                        transElement.appendChild(puzzle.getFactory().exportCell(newDocument, data));
                    }
                    nodeElement.appendChild(transElement);
                }
                treeElement.appendChild(nodeElement);
            }
        }
        return treeElement;
    }
}
