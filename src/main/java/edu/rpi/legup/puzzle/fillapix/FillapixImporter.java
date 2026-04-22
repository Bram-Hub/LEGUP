package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FillapixImporter extends PuzzleImporter {
    public FillapixImporter(Fillapix fillapix) {
        super(fillapix);
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
     * @throws RuntimeException if board can not be made
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        FillapixBoard fillapixBoard = new FillapixBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                FillapixCell cell =
                        new FillapixCell(FillapixCellType.UNKNOWN.value, new Point(x, y));
                cell.setIndex(y * columns + x);
                cell.setNumber(FillapixCell.DEFAULT_VALUE);
                cell.setModifiable(true);
                fillapixBoard.setCell(x, y, cell);
            }
        }
        puzzle.setCurrentBoard(fillapixBoard);
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
                        "Fillapix Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "Fillapix Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            FillapixBoard fillapixBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                fillapixBoard = new FillapixBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    fillapixBoard = new FillapixBoard(width, height);
                }
            }

            if (fillapixBoard == null) {
                throw new InvalidFileFormatException("Fillapix Importer: invalid board dimensions");
            }

            int width = fillapixBoard.getWidth();
            int height = fillapixBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                FillapixCell cell =
                        (FillapixCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), fillapixBoard);
                Point loc = cell.getLocation();
                cell.setModifiable(true);
                cell.setGiven(true);
                fillapixBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (fillapixBoard.getCell(x, y) == null) {
                        FillapixCell cell =
                                new FillapixCell(FillapixCell.DEFAULT_VALUE, new Point(x, y));
                        cell.setIndex(y * width + x);
                        cell.setModifiable(true);
                        fillapixBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(fillapixBoard);
            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, fillapixBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    FillapixCell cell =
                            (FillapixCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), fillapixBoard);
                    // Store the goal value as goalData and mark the board cell as goal
                    FillapixCell boardCell = (FillapixCell) fillapixBoard.getCell(cell.getLocation());
                    if (boardCell != null) {
                        boardCell.setGoalData(cell.getData());
                        boardCell.setGoal(true);
                    }
                    goal.addCell(cell);
                }
                puzzle.setGoal(goal);
            } else {
                Goal goal = new Goal(null, GoalType.DEFAULT);
                puzzle.setGoal(goal);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Fillapix Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Fillapix cannot accept text input");
    }
}
