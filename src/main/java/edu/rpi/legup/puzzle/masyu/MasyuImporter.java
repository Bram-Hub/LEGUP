package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MasyuImporter extends PuzzleImporter {
    public MasyuImporter(Masyu masyu) {
        super(masyu);
    }

    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    @Override
    public boolean acceptsTextInput() {
        return false;
    }

    /**
     * Creates an empty board for building
     *
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     * @throws RuntimeException if board can not be created
     */
    @Override
    public void initializeBoard(int rows, int columns) {}

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException(
                        "Masyu Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "Masyu Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            MasyuBoard masyuBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                masyuBoard = new MasyuBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    masyuBoard = new MasyuBoard(width, height);
                }
            }

            if (masyuBoard == null) {
                throw new InvalidFileFormatException("Masyu Importer: invalid board dimensions");
            }

            int width = masyuBoard.getWidth();
            int height = masyuBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                MasyuCell cell =
                        (MasyuCell)
                                puzzle.getFactory().importCell(elementDataList.item(i), masyuBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != MasyuType.UNKNOWN) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                masyuBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (masyuBoard.getCell(x, y) == null) {
                        MasyuCell cell = new MasyuCell(MasyuType.UNKNOWN, new Point(x, y));
                        cell.setIndex(y * width + x);
                        cell.setModifiable(true);
                        masyuBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(masyuBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Masyu Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Masyu cannot accept text input");
    }
}
