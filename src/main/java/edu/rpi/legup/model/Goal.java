package edu.rpi.legup.model;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class Goal {
    private GridCell<Integer> cell;
    private GoalType goalType;

    public Goal() {
        this.cell = null;
        this.goalType = GoalType.NONE;
    }

    public Goal(GridCell<Integer> cell, GoalType goalType) {
        this.cell = cell;
        this.goalType = goalType;
    }

    public GridCell<Integer> getCell() { return cell; }

    public void setCell(GridCell<Integer> cell) { this.cell = cell; }

    public Point getLocation() { return cell.getLocation(); }

    public GoalType getType() { return goalType; }

    public void getType(GoalType goalType) { this.goalType = goalType; }
}
