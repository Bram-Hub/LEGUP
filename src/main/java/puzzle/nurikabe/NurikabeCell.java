package puzzle.nurikabe;

import model.gameboard.GridCell;

import java.awt.*;

public class NurikabeCell extends GridCell
{

    public NurikabeCell(int valueInt, Point location)
    {
        super(valueInt, location);
    }

    public NurikabeType getType()
    {
        switch(valueInt)
        {
            case -2:
                return NurikabeType.UNKNOWN;
            case -1:
                return NurikabeType.BLACK;
            case 0:
                return NurikabeType.WHITE;
            default:
                if(valueInt > 0)
                {
                    return NurikabeType.NUMBER;
                }
        }
        return null;
    }

    @Override
    public NurikabeCell copy()
    {
        NurikabeCell copy = new NurikabeCell(valueInt, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
