package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.Point;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StarBattleImporter extends PuzzleImporter {

    public StarBattleImporter(StarBattle starbattle) {
        super(starbattle);
    }

    /** Puzzle setting to support row and column inputs */
    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    /** Puzzle setting to disable support for text input */
    @Override
    public boolean acceptsTextInput() {
        return false;
    }

    /**
     * Constructs empty StarBattle gameboard as per the provided dimensions
     *
     * @param rows number of rows and columns for the gameboard
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        int puzzle_num = 1;
        StarBattleBoard StarBattleBoard = new StarBattleBoard(rows, puzzle_num);
        puzzle.setCurrentBoard(StarBattleBoard);
    }

    /**
     * Constructs StarBattle gameboard
     *
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        Element puzzleElement = (Element) node;
        int puzzle_num = Integer.parseInt(puzzleElement.getAttribute("puzzle_num"));
        NodeList regionNodes = puzzleElement.getElementsByTagName("region");
        int size = Integer.parseInt(puzzleElement.getAttribute("size"));
        if (regionNodes.getLength() != size) {
            throw new InvalidFileFormatException(
                    "Not the current amount of regions in the puzzle.");
        }

        StarBattleBoard StarBattleBoard =
                new StarBattleBoard(
                        size, puzzle_num); // Initialize the board with width and height from XML

        for (int i = 0; i < regionNodes.getLength(); i++) {
            Element regionElement = (Element) regionNodes.item(i);
            NodeList cellNodes = regionElement.getElementsByTagName("cell");
            StarBattleRegion region_i = new StarBattleRegion();

            for (int j = 0; j < cellNodes.getLength(); j++) {
                Element cellElement = (Element) cellNodes.item(j);
                int x = Integer.parseInt(cellElement.getAttribute("x"));
                int y = Integer.parseInt(cellElement.getAttribute("y"));
                int value = Integer.parseInt(cellElement.getAttribute("value"));

                Point cellPoint = new Point(x, y);

                // Create the StarBattleCell with the cell type and value
                StarBattleCell cell = new StarBattleCell(value, cellPoint, i, size);
                cell.setIndex(y * size + x); // Calculate the index based on size
                cell.setModifiable(true);

                // Add the cell to the board
                StarBattleBoard.setCell(x, y, cell);
                region_i.addCell(cell);
            }
            StarBattleBoard.setRegion(i, region_i);
        }

        puzzle.setCurrentBoard(StarBattleBoard);
        if (puzzleElement.getElementsByTagName("goal").getLength() != 0) {
            Element goalElement = (Element) puzzleElement.getElementsByTagName("goal").item(0);
            Goal goal = puzzle.getFactory().importGoal(goalElement, StarBattleBoard);

            NodeList cellList = goalElement.getElementsByTagName("cell");
            for (int i = 0; i < cellList.getLength(); i++) {
                StarBattleCell cell =
                        (StarBattleCell)
                                puzzle.getFactory()
                                        .importCell(cellList.item(i), StarBattleBoard);
                goal.addCell(cell);
            }
            puzzle.setGoal(goal);
        } else {
            Goal goal = new Goal(null, GoalType.DEFAULT);

            puzzle.setGoal(goal);
        }
    }

    /**
     * Initialize board via string of statements.
     *
     * @throws UnsupportedOperationException since StarBattle does not support text input
     */
    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Star Battle does not accept text input");
    }
}
