package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class YinYangImporter extends PuzzleImporter {

    public YinYangImporter(YinYang puzzle) {
        super(puzzle);
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
        YinYangBoard board = new YinYangBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                YinYangCell cell = new YinYangCell(YinYangType.UNKNOWN, x, y);
                board.setCell(x, y, cell);
            }
        }

        puzzle.setCurrentBoard(board);
    }

    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        if (!node.getNodeName().equalsIgnoreCase("board")) {
            throw new InvalidFileFormatException("YinYang Importer: cannot find board element");
        }

        Element boardElement = (Element) node;
        if (boardElement.getElementsByTagName("cells").getLength() == 0) {
            throw new InvalidFileFormatException("YinYang Importer: no elements found for board");
        }

        Element cellsElement = (Element) boardElement.getElementsByTagName("cells").item(0);
        NodeList cellNodes = cellsElement.getElementsByTagName("cell");

        int width = Integer.parseInt(boardElement.getAttribute("width"));
        int height = Integer.parseInt(boardElement.getAttribute("height"));
        YinYangBoard board = new YinYangBoard(width, height);

        for (int i = 0; i < cellNodes.getLength(); i++) {
            YinYangCell cell = (YinYangCell) puzzle.getFactory().importCell(cellNodes.item(i), board);
            if (cell.getType() != YinYangType.UNKNOWN) {
                cell.setModifiable(false);
                cell.setGiven(true);
            }
            board.setCell(cell.getX(), cell.getY(), cell);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board.getCell(x, y) == null) {
                    YinYangCell cell = new YinYangCell(YinYangType.UNKNOWN, x, y);
                    board.setCell(x, y, cell);
                }
            }
        }

        puzzle.setCurrentBoard(board);
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("YinYang does not support text input");
    }
}
