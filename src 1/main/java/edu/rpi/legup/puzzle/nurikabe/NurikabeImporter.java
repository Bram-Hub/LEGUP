package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class NurikabeImporter extends PuzzleImporter {
    public NurikabeImporter(Nurikabe nurikabe) {
        super(nurikabe);
    }

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("nurikabe Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("nurikabe Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            NurikabeBoard nurikabeBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                nurikabeBoard = new NurikabeBoard(size);
            } else if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                int width = Integer.valueOf(boardElement.getAttribute("width"));
                int height = Integer.valueOf(boardElement.getAttribute("height"));
                nurikabeBoard = new NurikabeBoard(width, height);
            }

            if (nurikabeBoard == null) {
                throw new InvalidFileFormatException("nurikabe Importer: invalid board dimensions");
            }

            int width = nurikabeBoard.getWidth();
            int height = nurikabeBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                NurikabeCell cell = (NurikabeCell) puzzle.getFactory().importCell(elementDataList.item(i), nurikabeBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != NurikabeType.UNKNOWN.toValue()) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                nurikabeBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (nurikabeBoard.getCell(x, y) == null) {
                        NurikabeCell cell = new NurikabeCell(NurikabeType.UNKNOWN.toValue(), new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        nurikabeBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(nurikabeBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("nurikabe Importer: unknown value where integer expected");
        }
    }
}
