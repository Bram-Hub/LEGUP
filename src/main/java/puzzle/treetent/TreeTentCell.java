package puzzle.treetent;

import model.gameboard.GridCell;

import java.awt.*;

public class TreeTentCell extends GridCell
{
    private boolean linked = false;
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

    public void setLink(boolean hasLink){
        linked = hasLink;
//        if(linked)
//            System.out.println("Link true at " + getLocation());
//        else
//            System.out.println("Link false at " + getLocation());
    }

    public boolean isLinked(){
        return linked;
    }
    @Override
    public TreeTentCell copy()
    {
        TreeTentCell copy = new TreeTentCell(valueInt, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setLink(linked);
        return copy;
    }
}
