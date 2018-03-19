package puzzle.treetent;

import model.gameboard.GridCell;

import java.awt.*;

public class TreeTentCell extends GridCell
{

    public TreeTentCell(int valueInt, Point location)
    {
        super(valueInt, location);
    }

    public TreeTentType getType()
    {
        switch(valueInt)
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
        TreeTentCell copy = new TreeTentCell(valueInt, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
