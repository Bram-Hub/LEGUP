package edu.rpi.legup.model;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

/**
 * A <b>Goal</b> is an immutable object that stores the type of goal and a GridCell,
 * which holds the type of cell and the cell's location. Goal's should only be constructed
 * twice; once upon loading the puzzle, and once upon saving the puzzle.
 */
public class Goal {
    private final GridCell<Integer> cell;
    private final GoalType goalType;

    /**
     * Constructs a Goal object
     * @param cell Holds location and type of cell
     * @param goalType type of goal
     */
    public Goal(GridCell<Integer> cell, GoalType goalType) {
        this.cell = cell;
        this.goalType = goalType;
    }

    /**
     * Get the goal's cell
     *
     * @return GridCell cell
     */
    public GridCell<Integer> getCell() { return cell; }

    /**
     * Get the goal cell's location
     *
     * @return Point (x, y) of the cell's location
     */
    public Point getLocation() { return cell.getLocation(); }

    /**
     * Get the goal type
     *
     * @return GoalType
     */
    public GoalType getType() { return goalType; }

}
