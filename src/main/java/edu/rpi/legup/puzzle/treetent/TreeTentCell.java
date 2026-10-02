package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TreeTentCell extends GridCell<TreeTentType> {

    /**
     * Constructs a TreeTentCell with the specified type and location.
     *
     * @param value the TreeTentType value of the cell
     * @param location the position of the cell on the board
     */
    public TreeTentCell(TreeTentType value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this TreeTentCell.
     *
     * @return the TreeTentType of this cell
     */
    public TreeTentType getType() {
        return data;
    }

    /**
     * Gets the integer value corresponding to the TreeTentType of this cell.
     *
     * @return 1 for TREE, 2 for GRASS, 3 for TENT, or 0 for UNKNOWN
     */
    public int getValue() {
        switch (data) {
            case TREE:
                return 1;
            case GRASS:
                return 2;
            case TENT:
                return 3;
            default:
                return 0;
        }
    }

    /**
     * Sets the type of this TreeTentCell based on the provided element.
     *
     * @param e the element representing the new type
     * @param m the mouse event triggering the change
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementName()) {
            case "Unknown Tile":
                this.data = TreeTentType.UNKNOWN;
                break;
            case "Tree Tile":
                this.data = TreeTentType.TREE;
                break;
            case "Grass Tile":
                this.data = TreeTentType.GRASS;
                break;
            case "Tent Tile":
                this.data = TreeTentType.TENT;
        }
    }

    /**
     * Creates a deep copy of this TreeTentCell.
     *
     * @return a new TreeTentCell with the same data and properties
     */
    @Override
    public TreeTentCell copy() {
        TreeTentCell copy = new TreeTentCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {
        return !(data == TreeTentType.UNKNOWN);
    }

    @Override
    public String describeState(boolean isPlural) {
        return getType().toString().toLowerCase();
    }
}
