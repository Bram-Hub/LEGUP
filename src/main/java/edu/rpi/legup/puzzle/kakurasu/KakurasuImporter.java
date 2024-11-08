package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

// TODO: Include changes to puzzle implementation into the importer
public class KakurasuImporter extends PuzzleImporter {
    public KakurasuImporter(Kakurasu kakurasu) {
        super(kakurasu);
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
    public void initializeBoard(int rows, int columns) {
        KakurasuBoard kakurasuBoard = new KakurasuBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                KakurasuCell cell =
                        new KakurasuCell(KakurasuType.UNKNOWN.toValue(), new Point(x, y));
                cell.setIndex(y * columns + x);
                cell.setModifiable(true);
                kakurasuBoard.setCell(x, y, cell);
            }
        }
        puzzle.setCurrentBoard(kakurasuBoard);
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
                throw new InvalidFileFormatException("kakurasu Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("kakurasu Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            KakurasuBoard kakurasuBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                kakurasuBoard = new KakurasuBoard(size);
            } else if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                int width = Integer.valueOf(boardElement.getAttribute("width"));
                int height = Integer.valueOf(boardElement.getAttribute("height"));
                kakurasuBoard = new KakurasuBoard(width, height);
            }

            if (kakurasuBoard == null) {
                throw new InvalidFileFormatException("kakurasu Importer: invalid board dimensions");
            }

            int width = kakurasuBoard.getWidth();
            int height = kakurasuBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                KakurasuCell cell = (KakurasuCell) puzzle.getFactory().importCell(elementDataList.item(i), kakurasuBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != KakurasuType.UNKNOWN.toValue()) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                kakurasuBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (kakurasuBoard.getCell(x, y) == null) {
                        KakurasuCell cell = new KakurasuCell(KakurasuType.UNKNOWN.toValue(), new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        kakurasuBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(kakurasuBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("kakurasu Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Kakurasu cannot accept text input");
    }
}
