package puzzle.lightup;

import model.gameboard.GridCell;

import java.awt.*;

public class LightUpCell extends GridCell
{
    private boolean isLite;

    public LightUpCell(int valueInt, Point location)
    {
        super(valueInt, location);
        this.isLite = false;
    }

    public LightUpCellType getType()
    {
        switch(valueInt)
        {
            case -4:
                return LightUpCellType.BULB;
            case -3:
                return LightUpCellType.EMPTY;
            case -2:
                return LightUpCellType.UNKNOWN;
            case -1:
                return LightUpCellType.BLACK;
            default:
                if(valueInt >= 0)
                {
                    return LightUpCellType.NUMBER;
                }
        }
        return null;
    }

    public boolean isLite()
    {
        return isLite;
    }

    public void setLite(boolean isLite)
    {
        this.isLite = isLite;
    }

    @Override
    public LightUpCell copy()
    {
        LightUpCell copy = new LightUpCell(valueInt, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
