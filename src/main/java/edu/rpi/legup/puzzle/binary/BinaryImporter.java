package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BinaryImporter extends PuzzleImporter {
    public BinaryImporter(Binary binary) {
        super(binary);
    }

    /**
     * Determines if puzzle uses row and column input
     *
     * @return true if row and column input is used, false otherwise
     */
    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    /**
     * Determines if puzzle uses text input
     *
     * @return true if text input is used, false otherwise
     */
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
                throw new InvalidFileFormatException(
                        "Binary Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "Binary Importer: no puzzleElement found for board");
            }

            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            BinaryBoard binaryBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                binaryBoard = new BinaryBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    binaryBoard = new BinaryBoard(width, height);
                }
            }

            int width = binaryBoard.getWidth();
            int height = binaryBoard.getHeight();

            if (binaryBoard == null || width % 2 != 0 || height % 2 != 0) {
                throw new InvalidFileFormatException("Binary Importer: invalid board dimensions");
            }

            for (int i = 0; i < elementDataList.getLength(); i++) {
                BinaryCell cell =
                        (BinaryCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), binaryBoard);
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
                        BinaryCell cell =
                                new BinaryCell(BinaryType.UNKNOWN.toValue(), new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        binaryBoard.setCell(x, y, cell);
                    }
                }
            }

            puzzle.setCurrentBoard(binaryBoard);

            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, binaryBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    BinaryCell cell =
                            (BinaryCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), binaryBoard);
                    goal.addCell(cell);
                }
                puzzle.setGoal(goal);
            } else {
                Goal goal = new Goal(null, GoalType.DEFAULT);

                puzzle.setGoal(goal);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "binary Importer: unknown value where integer expected");
        }
    }

    /**
     * Initializes a board with text
     *
     * @param statements the text being used
     * @throws UnsupportedOperationException Binary does not use text input
     */
    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Binary cannot accept text input");
    }
}
