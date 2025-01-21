package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
                if (kakurasuBoard.getCell(x, y) == null) {
                    KakurasuCell cell = new KakurasuCell(KakurasuType.UNKNOWN, new Point(x, y));
                    cell.setIndex(y * columns + x);
                    cell.setModifiable(true);
                    kakurasuBoard.setCell(x, y, cell);
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            kakurasuBoard.getRowClues().set(i, new KakurasuClue(0, i + 1, KakurasuType.CLUE_EAST));
        }

        for (int i = 0; i < columns; i++) {
            kakurasuBoard.getColClues().set(i, new KakurasuClue(0, i + 1, KakurasuType.CLUE_SOUTH));
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
                throw new InvalidFileFormatException(
                        "Kakurasu Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "Kakurasu Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            KakurasuBoard kakurasuBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                kakurasuBoard = new KakurasuBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    kakurasuBoard = new KakurasuBoard(width, height);
                }
            }

            if (kakurasuBoard == null) {
                throw new InvalidFileFormatException("Kakurasu Importer: invalid board dimensions");
            }

            int width = kakurasuBoard.getWidth();
            int height = kakurasuBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                KakurasuCell cell =
                        (KakurasuCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), kakurasuBoard);
                Point loc = cell.getLocation();
                if (cell.getData() != KakurasuType.UNKNOWN) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                kakurasuBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (kakurasuBoard.getCell(x, y) == null) {
                        KakurasuCell cell = new KakurasuCell(KakurasuType.UNKNOWN, new Point(x, y));
                        cell.setIndex(y * width + x);
                        cell.setModifiable(true);
                        kakurasuBoard.setCell(x, y, cell);
                    }
                }
            }

            NodeList axes = boardElement.getElementsByTagName("axis");
            if (axes.getLength() != 2) {
                throw new InvalidFileFormatException("Kakurasu Importer: cannot find axes");
            }

            Element axis1 = (Element) axes.item(0);
            Element axis2 = (Element) axes.item(1);

            if (!axis1.hasAttribute("side") || !axis1.hasAttribute("side")) {
                throw new InvalidFileFormatException(
                        "Kakurasu Importer: side attribute of axis not specified");
            }
            String side1 = axis1.getAttribute("side");
            String side2 = axis2.getAttribute("side");
            if (side1.equalsIgnoreCase(side2)
                    || !(side1.equalsIgnoreCase("east") || side1.equalsIgnoreCase("south"))
                    || !(side2.equalsIgnoreCase("east") || side2.equalsIgnoreCase("south"))) {
                throw new InvalidFileFormatException(
                        "Kakurasu Importer: axes must be different and be {east | south}");
            }
            NodeList eastClues =
                    side1.equalsIgnoreCase("east")
                            ? axis1.getElementsByTagName("clue")
                            : axis2.getElementsByTagName("clue");
            NodeList southClues =
                    side1.equalsIgnoreCase("south")
                            ? axis1.getElementsByTagName("clue")
                            : axis2.getElementsByTagName("clue");

            if (eastClues.getLength() != kakurasuBoard.getHeight()
                    || southClues.getLength() != kakurasuBoard.getWidth()) {
                throw new InvalidFileFormatException(
                        "Kakurasu Importer: there must be same number of clues as the dimension of"
                                + " the board");
            }

            for (int i = 0; i < eastClues.getLength(); i++) {
                Element clue = (Element) eastClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = Integer.valueOf(clue.getAttribute("index"));

                if (index - 1 < 0 || index - 1 > kakurasuBoard.getHeight()) {
                    throw new InvalidFileFormatException(
                            "Kakurasu Importer: clue index out of bounds");
                }

                if (kakurasuBoard.getRowClues().get(index - 1) != null) {
                    throw new InvalidFileFormatException("Kakurasu Importer: duplicate clue index");
                }
                kakurasuBoard
                        .getRowClues()
                        .set(index - 1, new KakurasuClue(value, index, KakurasuType.CLUE_EAST));
            }

            for (int i = 0; i < southClues.getLength(); i++) {
                Element clue = (Element) southClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = Integer.valueOf(clue.getAttribute("index"));

                if (index - 1 < 0 || index - 1 > kakurasuBoard.getWidth()) {
                    throw new InvalidFileFormatException(
                            "Kakurasu Importer: clue index out of bounds");
                }

                if (kakurasuBoard.getColClues().get(index - 1) != null) {
                    throw new InvalidFileFormatException("Kakurasu Importer: duplicate clue index");
                }
                kakurasuBoard
                        .getColClues()
                        .set(index - 1, new KakurasuClue(value, index, KakurasuType.CLUE_SOUTH));
            }

            puzzle.setCurrentBoard(kakurasuBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Kakurasu Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Kakurasu cannot accept text input");
    }
}
