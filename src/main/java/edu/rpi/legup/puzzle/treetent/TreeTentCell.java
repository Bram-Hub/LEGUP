package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class TreeTentCell extends GridCell<Integer> {

    public TreeTentCell(int valueInt, Point location) {
        super(valueInt, location);
    }

    public TreeTentType getType() {
        switch (data) {
            case 0:
                return TreeTentType.UNKNOWN;
            case 1:
                return TreeTentType.TREE;
            case 2:
                return TreeTentType.GRASS;
            case 3:
                return TreeTentType.TENT;
            default:
                return null;
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
