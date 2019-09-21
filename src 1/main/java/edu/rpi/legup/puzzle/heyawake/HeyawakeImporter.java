package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class HeyawakeImporter extends PuzzleImporter {

    public HeyawakeImporter(Heyawake heyawake) {
        super(heyawake);
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
                throw new InvalidFileFormatException("Heyawake Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("Heyawake Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            HeyawakeBoard heyawakeBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                heyawakeBoard = new HeyawakeBoard(size);
            } else if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                int width = Integer.valueOf(boardElement.getAttribute("width"));
                int height = Integer.valueOf(boardElement.getAttribute("height"));
                heyawakeBoard = new HeyawakeBoard(width, height);
            }

            if (heyawakeBoard == null) {
                throw new InvalidFileFormatException("Heyawake Importer: invalid board dimensions");
            }

            int width = heyawakeBoard.getWidth();
            int height = heyawakeBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                HeyawakeCell cell = (HeyawakeCell) puzzle.getFactory().importCell(elementDataList.item(i), heyawakeBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != -2) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                heyawakeBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (heyawakeBoard.getCell(x, y) == null) {
                        HeyawakeCell cell = new HeyawakeCell(0, new Point(x, y), -1);
                        cell.setIndex(y * height + x);
                        cell.setModifiable(false);
                        heyawakeBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(heyawakeBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("Heyawake Importer: unknown value where integer expected");
        }
    }
}
