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

    @Override
    public Integer getData()
    {
        return (Integer) super.getData();
    }

    public MasyuCell getConnectedCell()
    {
        return connectedCell;
    }

    public MasyuType getType()
    {
        Integer value = getData();
        switch(value)
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
        MasyuCell copy = new MasyuCell((Integer) data, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
