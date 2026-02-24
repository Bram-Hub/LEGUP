package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;

public class StarBattleCell extends GridCell<Integer> {
    private int groupIndex;
    private int max;

    /**
     * StarBattleCell Constructor - creates a new StarBattle cell to hold the puzzleElement
     *
     * @param value value of the star battle cell denoting its state
     * @param location location of the cell on the board
     * @param groupIndex indicates what group # the cell is in.
     * @param size size of the star battle cell
     */
    public StarBattleCell(int value, Point location, int groupIndex, int size) {
        super(value, location);
        this.groupIndex = groupIndex;
        this.max = size;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementID()) {
            case "STBL-PLAC-0001":
                this.data = -3;
                break;
            case "STBL-PLAC-0002":
                this.data = -2;
                break;
            case "STBL-PLAC-0003":
                this.data = -1;
                break;

            case "STBL-UNPL-0001": // Not sure how button events work
                switch (m.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (this.data > 0 || this.data < -3) {
                            this.data = -3;
                        } else {
                            this.data = this.data + 1;
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        if (this.data > -4) {
                            this.data = this.data - 1;
                        } else {
                            this.data = -1; // Unsure
                        }
                        break;
                }
                break;
        }
    }

    public StarBattleCellType getType() {
        switch (data) {
            case -3:
                return StarBattleCellType.UNKNOWN;
            case -2:
                return StarBattleCellType.STAR;
            case -1:
                return StarBattleCellType.BLACK;
            default:
                if (data >= 0) {
                    return StarBattleCellType.UNKNOWN;
                }
        }
        return null;
    }

    public StarBattleCell copy() {
        StarBattleCell copy = new StarBattleCell(data, (Point) location.clone(), groupIndex, max);
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return !(data == -3);}

    @Override
    public String describeState(boolean isPlural) {
        if (getType() == StarBattleCellType.STAR) {
            return isPlural ? "star" : "stars";
        }
        return "black";
    }
}
