package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TreeTentCell extends GridCell<TreeTentType> {

    public TreeTentCell(TreeTentType value, Point location) {
        super(value, location);
    }

    public TreeTentType getType() {
        return data;
    }

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

    @Override
    public TreeTentCell copy() {
        TreeTentCell copy = new TreeTentCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
