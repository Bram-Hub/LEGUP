package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class BinaryImporter extends PuzzleImporter {
    public BinaryImporter(Binary binary) {
        super(binary);
    }

    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    @Override
    public boolean acceptsTextInput() {
        return false;
    }

    @Override
    public void initializeBoard(int rows, int columns) {
        BinaryBoard binaryBoard = new BinaryBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                BinaryCell cell = new BinaryCell(BinaryType.UNKNOWN.toValue(), new Point(x, y));
                cell.setIndex(y * columns + x);
                cell.setModifiable(true);
                binaryBoard.setCell(x, y, cell);
            }
        }
        puzzle.setCurrentBoard(binaryBoard);
    }

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
                throw new InvalidFileFormatException("binary Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("binary Importer: no puzzleElement found for board");
            }

            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            BinaryBoard binaryBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                binaryBoard = new BinaryBoard(size);
            }
            else {
                if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    binaryBoard = new BinaryBoard(width, height);
                }
            }

            int width = binaryBoard.getWidth();
            int height = binaryBoard.getHeight();
            
            if (binaryBoard == null || width % 2 != 0 || height % 2 != 0) {
                throw new InvalidFileFormatException("binary Importer: invalid board dimensions");
            }


            for (int i = 0; i < elementDataList.getLength(); i++) {
                BinaryCell cell = (BinaryCell) puzzle.getFactory().importCell(elementDataList.item(i), binaryBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != BinaryType.UNKNOWN.toValue()) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                binaryBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (binaryBoard.getCell(x, y) == null) {
                        BinaryCell cell = new BinaryCell(BinaryType.UNKNOWN.toValue(), new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        binaryBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(binaryBoard);
        }
        catch (NumberFormatException e) {
            throw new InvalidFileFormatException("binary Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Binary cannot accept text input");
    }
}
