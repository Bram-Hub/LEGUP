package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightUpCell extends GridCell<Integer> {
    private boolean isLite;

    public LightUpCell(int valueInt, @NotNull Point location) {
        super(valueInt, location);
        this.isLite = false;
    }

    /**
     * Sets the type of this LightUpCell
     *
     * @param e element to set the type of this binary cell to
     */
    @Override
    public void setType(@NotNull Element e, @NotNull MouseEvent m) {
        switch (e.getElementID()) {
            case "LTUP-ELEM-0002":
                this.data = -4;
                break;
            case "LTUP-ELEM-0001":
                this.data = -1;
                break;
            case "LTUP-ELEM-0004":
                this.data = -2;
                break;
            case "LTUP-ELEM-0003":
                switch (m.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (this.data < 0 || this.data > 3) {
                            this.data = 0;
                        } else {
                            this.data = this.data + 1;
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        if (this.data > 0) {
                            this.data = this.data - 1;
                        } else {
                            this.data = 4;
                        }
                        break;
                }
                break;
        }
    }

    public @Nullable LightUpCellType getType() {
        switch (data) {
            case -4:
                return LightUpCellType.BULB;
            case -3:
                return LightUpCellType.EMPTY;
            case -2:
                return LightUpCellType.UNKNOWN;
            case -1:
                return LightUpCellType.BLACK;
            default:
                if (data >= 0) {
                    return LightUpCellType.NUMBER;
                }
        }
        return null;
    }

    /**
     * Gets whether this LightUpCell is illuminated
     *
     * @return true if the cell is lit, false otherwise
     */
    public boolean isLite() {
        return isLite;
    }

    /**
     * Sets whether this LightUpCell is illuminated
     *
     * @param isLite true if the cell should be lit, false otherwise
     */
    public void setLite(boolean isLite) {
        this.isLite = isLite;
    }

    /**
     * Performs a deep copy on the LightUpCell
     *
     * @return a new copy of the LightUpCell that is independent of this one
     */
    @Override
    public @NotNull LightUpCell copy() {
        LightUpCell copy = new LightUpCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return data != -2;}

    @Override
    public String describeState(boolean isPlural) {
        return getType().toString().toLowerCase();
    }
}