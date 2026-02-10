package edu.rpi.legup.model;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * A <b>Goal</b> is an object that stores the type of goal and a list of GridCells,
 * which holds the type of cell and the cell's location. Goal's should only be constructed
 * twice; once upon loading the puzzle, and once upon saving the puzzle.
 * <p>
 * If the GoalType is DEFAULT, cellList must be null.
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
     * Get the goal's cells
     *
     * @return GridCell cells
     */
    public ArrayList<GridCell> getCells() { return cellList; }

    public void addCell(GridCell cell) { cellList.add(cell); }

    public void setCellList(ArrayList<GridCell> cellList) { this.cellList = cellList; }

    /**
     * Get the goal type.
     *
     * @return GoalType.
     */
    public GoalType getType() { return goalType; }

    /**
     * Get the text description of the goal condition.
     *
     * @return String text.
     */
    public String getGoalText() {

        if (goalType == GoalType.DEFAULT) return "Find all solutions to the puzzle or prove none exist.";

        return switch(goalType) {
            case GoalType.PROVE_CELL_MUST_BE -> getValueSeparatedGoalText(
                    " is ", " are ");
            case GoalType.PROVE_CELL_MIGHT_NOT_BE -> getValueSeparatedGoalText(
                    " might not be ", " might not be ");
            case GoalType.PROVE_SINGLE_CELL_VALUE -> {
                String text = "Prove " + (cellList.size() > 1 ? "cells " : "cell ");
                text += concatCellLocs(cellList);
                text += (cellList.size() > 1 ? " individually have" : " has");
                yield text + " only one possible value.";
            }
            case GoalType.PROVE_MULTIPLE_CELL_VALUE -> {
                String text = "Prove " + (cellList.size() > 1 ? "cells " : "cell ");
                text += concatCellLocs(cellList);
                text += (cellList.size() > 1 ? " individually have" : " has");
                yield text + " multiple possible values.";
            }
            default -> "Unrecognized goal condition.";
        };
    }

    /**
     * Create grammatically correct String listing cell locations.
     *
     * @param cells List of cells to stringify.
     * @return String text.
     */
    private String concatCellLocs(ArrayList<GridCell> cells) {
        String text = "";
        for (int i = 0; i < cells.size(); ++i) {
            if (i == 1 && cells.size() == 2) {
                text += " and ";
            }
            else if (i != 0) {
                text += ", ";
                if (i == cells.size() - 1) { text += "and "; }
            }
            Point loc = cells.get(i).getLocation();
            text += "(" + (int) loc.getX() + ", " + (int) loc.getY() + ")";
        }
        return text;
    }

    /**
     * Get a TreeMap of ArrayLists of cells separated by state description string keys.
     *
     * @throws IllegalStateException when cellList is empty.
     */
    private TreeMap<String, ArrayList<GridCell>> getCellsByState() throws IllegalStateException {

        if (cellList.isEmpty()) throw new IllegalStateException("No goal cells to copy.");
        TreeMap<String, ArrayList<GridCell>> cellsByState = new TreeMap<>();

        for (GridCell cell : cellList) {
            String state = cell.describeState();
            if (!cellsByState.containsKey(state)) {
                cellsByState.put(state, new ArrayList<>());
            }
            cellsByState.get(state).add(cell);
        }
        return cellsByState;
    }

    /**
     * Get String describing how the goal condition relates goal cells to their values.
     *
     * @param singleCondition Relationship between single cell and its value.
     * @param pluralCondition Relationship between multiple cells and their shared value.
     * @return String Description text.
     */
    private String getValueSeparatedGoalText(String singleCondition, String pluralCondition) {

        TreeMap<String, ArrayList<GridCell>> cellsByState;
        try {
            cellsByState = getCellsByState();
        } catch (IllegalStateException e) {
            return "No condition.";
        }

        String text = "Prove ";
        boolean delimiter = false;
        for (Map.Entry<String, ArrayList<GridCell>> state : cellsByState.entrySet()) {
            if (delimiter) { text += " and "; }
            delimiter = true;
            text += (state.getValue().size() > 1 ? "cells " : "cell ");
            text += concatCellLocs(state.getValue());
            text += (state.getValue().size() > 1 ? pluralCondition : singleCondition) + state.getKey();
        }
        return text + ".";
    }
}
