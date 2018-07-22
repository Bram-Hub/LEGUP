package puzzle.treetent;

import model.gameboard.GridCell;

import java.awt.*;

public class TreeTentCell extends GridCell
{

    public TreeTentCell(int valueInt, Point location)
    {
        super(valueInt, location);
    }

    /**
     * Gets the int value that represents this element
     *
     * @return int value
     */
    @Override
    public Integer getData() {
        return (Integer) super.getData();
    }

    public TreeTentType getType()
    {
        Integer value = getData();
        switch(value)
        {
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
    public TreeTentCell copy()
    {
        TreeTentCell copy = new TreeTentCell((Integer) data, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
