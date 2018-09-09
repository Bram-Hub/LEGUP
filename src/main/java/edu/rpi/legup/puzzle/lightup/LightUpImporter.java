package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class LightUpImporter extends PuzzleImporter {
    public LightUpImporter(LightUp lightUp) {
        super(lightUp);
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
                throw new InvalidFileFormatException("lightup Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("lightup Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            LightUpBoard lightUpBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                lightUpBoard = new LightUpBoard(size);
            } else if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                int width = Integer.valueOf(boardElement.getAttribute("width"));
                int height = Integer.valueOf(boardElement.getAttribute("height"));
                lightUpBoard = new LightUpBoard(width, height);
            }

            if (lightUpBoard == null) {
                throw new InvalidFileFormatException("lightup Importer: invalid board dimensions");
            }

            int width = lightUpBoard.getWidth();
            int height = lightUpBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                LightUpCell cell = (LightUpCell) puzzle.getFactory().importCell(elementDataList.item(i), lightUpBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != -2) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                lightUpBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (lightUpBoard.getCell(x, y) == null) {
                        LightUpCell cell = new LightUpCell(-2, new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        lightUpBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(lightUpBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("lightup Importer: unknown value where integer expected");
        }
    }
}
