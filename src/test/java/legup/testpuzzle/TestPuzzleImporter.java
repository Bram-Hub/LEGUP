package legup.testpuzzle;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class TestPuzzleImporter extends PuzzleImporter {

    public TestPuzzleImporter(TestPuzzle puzzle) {super(puzzle);}

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
        GridBoard gridBoard = new GridBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                GridCell cell =
                        new GridCell(0, new Point(x, y));
                cell.setIndex(y * columns + x);
                cell.setModifiable(true);
                gridBoard.setCell(x, y, cell);
            }
        }
        puzzle.setCurrentBoard(gridBoard);

    }

    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException(
                        "TestPuzzleImporter: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException(
                        "TestPuzzleImporter: no puzzleElement found for board");
            }

            GridBoard gridBoard = null;
            if (!boardElement.getAttribute("size").isEmpty()) {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                gridBoard = new GridBoard(size);
            } else {
                if (!boardElement.getAttribute("width").isEmpty()
                        && !boardElement.getAttribute("height").isEmpty()) {
                    int width = Integer.valueOf(boardElement.getAttribute("width"));
                    int height = Integer.valueOf(boardElement.getAttribute("height"));
                    gridBoard = new GridBoard(width, height);
                }
            }

            if (gridBoard == null) {
                throw new InvalidFileFormatException("TestPuzzleImporter: invalid board dimensions");
            }

            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, gridBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    GridCell cell =
                            (GridCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), gridBoard);
                    goal.addCell(cell);
                }
                puzzle.setGoal(goal);
            } else {
                Goal goal = new Goal(null, GoalType.DEFAULT);
                puzzle.setGoal(goal);
            }

            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            int width = gridBoard.getWidth();
            int height = gridBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                GridCell cell =
                        (GridCell)
                                puzzle.getFactory()
                                        .importCell(elementDataList.item(i), gridBoard);
                Point loc = cell.getLocation();
                if ((int) cell.getData() != 0) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                gridBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (gridBoard.getCell(x, y) == null) {
                        GridCell cell =
                                new GridCell(0, new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        gridBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(gridBoard);

            if (boardElement.getElementsByTagName("goal").getLength() != 0) {
                Element goalElement = (Element) boardElement.getElementsByTagName("goal").item(0);
                Goal goal = puzzle.getFactory().importGoal(goalElement, gridBoard);

                NodeList cellList = goalElement.getElementsByTagName("cell");
                for (int i = 0; i < cellList.getLength(); i++) {
                    GridCell cell =
                            (GridCell)
                                    puzzle.getFactory()
                                            .importCell(cellList.item(i), gridBoard);
                    goal.addCell(cell);
                    gridBoard.getCell(cell.getLocation()).setGoal(true);
                }
                puzzle.setGoal(goal);
            } else {
                Goal goal = new Goal(null, GoalType.DEFAULT);

                puzzle.setGoal(goal);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "TestPuzzleImporter: unknown value where integer expected");
        }


    }

    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException, IllegalArgumentException {
        throw new UnsupportedOperationException("TestPuzzleImporter cannot accept text input");
    }
}
