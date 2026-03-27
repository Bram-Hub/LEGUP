package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LightUpImporter extends PuzzleImporter {
    public LightUpImporter(LightUp lightUp) {
        super(lightUp);
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
        LightUpBoard lightUpBoard = new LightUpBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (lightUpBoard.getCell(x, y) == null) {
                    LightUpCell cell = new LightUpCell(-2, new Point(x, y));
                    cell.setIndex(y * columns + x);
                    cell.setModifiable(true);
                    lightUpBoard.setCell(x, y, cell);
                }
            }
        }
        puzzle.setCurrentBoard(lightUpBoard);
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
                        "lightup Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "lightup Importer: no puzzleElement found for board");
            }
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            LightUpBoard lightUpBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                lightUpBoard = new LightUpBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    lightUpBoard = new LightUpBoard(width, height);
                }
            }

            if (lightUpBoard == null) {
                throw new InvalidFileFormatException("lightup Importer: invalid board dimensions");
            }



            int width = lightUpBoard.getWidth();
            int height = lightUpBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                LightUpCell cell =
                        (LightUpCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), lightUpBoard);
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
            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, lightUpBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    LightUpCell cell =
                            (LightUpCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), lightUpBoard);
                    // Store the goal value as goalData and mark the board cell as goal
                    LightUpCell boardCell = (LightUpCell) lightUpBoard.getCell(cell.getLocation());
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
                    "lightup Importer: unknown value where integer expected");
        }
    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Light Up cannot accept text input");
    }
}
