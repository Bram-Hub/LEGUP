package edu.rpi.legup.model;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.util.ArrayList;

/**
 * A <b>Goal</b> is an object that stores the type of goal and a list of GridCells,
 * which holds the type of cell and the cell's location. Goal's should only be constructed
 * twice; once upon loading the puzzle, and once upon saving the puzzle.
 * <p>
 * If the GoalType is NONE, cellList must be null.
 */
public class Goal {
    private ArrayList<GridCell> cellList;
    private final GoalType goalType;

    /**
     * Constructs a Goal object with an empty cell list
     *
     * @param goalType type of goal
     */
    public Goal(GoalType goalType) {
        this.cellList = new ArrayList<>();
        this.goalType = goalType;
    }

    /**
     * Constructs a Goal object only requiring a given cell
     *
     * @param cell Holds location and type of cell
     * @param goalType type of goal
     */
    public Goal(GridCell cell, GoalType goalType) {
        this.cellList = new ArrayList<>();
        if (cell != null) {
            cellList.add(cell);
        }
        this.goalType = goalType;
    }

    /**
     * Get the goal's cell
     *
     * @return GridCell cell
     */
    public ArrayList<GridCell> getCells() { return cellList; }

    public void addCell(GridCell cell) { cellList.add(cell); }

    public void setCellList(ArrayList<GridCell> cellList) { this.cellList = cellList; }

    /**
     * Get the goal type
     *
     * @return GoalType
     */
    public GoalType getType() { return goalType; }

}
