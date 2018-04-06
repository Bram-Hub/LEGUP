package model;

import model.gameboard.ElementData;
import model.rules.MergeRule;
import model.tree.TreeElement;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import save.ExportFileException;
import save.InvalidFileFormatException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public abstract class PuzzleExporter
{
    protected Puzzle puzzle;

    /**
     * PuzzleExporter Constructor - exports the puzzle object to a file
     */
    public PuzzleExporter(Puzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    public void exportPuzzle(String fileName) throws ExportFileException
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document newDocument = docBuilder.newDocument();

            Element legupElement = newDocument.createElement("Legup");
            legupElement.setAttribute("version", "2.0");
            newDocument.appendChild(legupElement);

            Element puzzleElement = newDocument.createElement("puzzle");
            puzzleElement.setAttribute("name", puzzle.getName());
            legupElement.appendChild(puzzleElement);

            puzzleElement.appendChild(createBoardElement(newDocument));
            if(true)
            {
                puzzleElement.appendChild(createProofElement(newDocument));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);
        }
        catch(ParserConfigurationException e)
        {
            throw new ExportFileException("Puzzle Exporter: parser configuration exception");
        }
        catch(TransformerException e)
        {
            throw new ExportFileException("Puzzle Exporter: parser configuration exception");
        }
        catch(Exception e)
        {
            throw e;
            //throw new ExportFileException(e.getMessage());
        }
    }

    protected abstract Element createBoardElement(Document newDocument);

    protected Element createProofElement(Document newDocument)
    {
        Element proofElement = newDocument.createElement("proof");
        Element treeElement = createTreeElement(newDocument);
        proofElement.appendChild(treeElement);
        return proofElement;
    }

    protected Element createTreeElement(Document newDocument)
    {
        Element treeElement = newDocument.createElement("tree");
        Element nodesElement = newDocument.createElement("nodes");
        Element transitionsElement = newDocument.createElement("transitions");

        ArrayList<TreeElement> treeElements = new ArrayList<>();
        treeElements.add(puzzle.getTree().getRootNode());
        while(!treeElements.isEmpty())
        {
            TreeElement element = treeElements.get(treeElements.size() - 1);
            treeElements.remove(element);
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode treeNode = (TreeNode)element;

                Element nodeElement = newDocument.createElement("node");
                nodeElement.setAttribute("id", String.valueOf(treeNode.hashCode()));
                if(treeNode.isRoot())
                {
                    nodeElement.setAttribute("root","true");
                }

                nodesElement.appendChild(nodeElement);
                treeElements.addAll(treeNode.getChildren());
            }
            else
            {
                TreeTransition treeTransition = (TreeTransition)element;

                Element transElement = newDocument.createElement("transition");
                transElement.setAttribute("id", String.valueOf(treeTransition.hashCode()));
                transElement.setAttribute("parent", String.valueOf(treeTransition.getParentNode().hashCode()));
                transElement.setAttribute("child", String.valueOf(treeTransition.getChildNode().hashCode()));
                if(treeTransition.isJustified())
                {
                    transElement.setAttribute("rule", treeTransition.getRule().getRuleName());
                }

                for(ElementData data : treeTransition.getBoard().getModifiedData())
                {
                    transElement.appendChild(puzzle.getFactory().exportCell(newDocument, data));
                }
                transitionsElement.appendChild(transElement);

                if(treeTransition.getChildNode() != null)
                {
                    treeElements.add(treeTransition.getChildNode());
                }
            }
        }

        treeElement.appendChild(nodesElement);
        treeElement.appendChild(transitionsElement);
        return treeElement;
    }
}
