package puzzle.masyu;

import model.gameboard.GridCell;

import java.awt.*;

public class MasyuCell extends GridCell
{

    private MasyuCell connectedCell;

    public MasyuCell(int valueInt, Point location)
    {
        super(valueInt, location);
        this.connectedCell = null;
    }

    public MasyuCell getConnectedCell()
    {
        return connectedCell;
    }

    public MasyuType getType()
    {
        switch(valueInt)
        {
            case 0:
                return MasyuType.UNKNOWN;
            case 1:
                return MasyuType.BLACK;
            case 2:
                return MasyuType.WHITE;
            default:
                return null;
        }
    }

    @Override
    public MasyuCell copy()
    {
        MasyuCell copy = new MasyuCell(valueInt, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
