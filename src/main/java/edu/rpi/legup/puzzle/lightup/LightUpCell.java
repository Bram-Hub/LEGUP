package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class LightUpCell extends GridCell
{
    private boolean isLite;

    public LightUpCell(int valueInt, Point location)
    {
        super(valueInt, location);
        this.isLite = false;
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

    public LightUpCellType getType()
    {
        Integer value = getData();
        switch(value)
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
                if(value >= 0)
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
        LightUpCell copy = new LightUpCell((Integer) data, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
