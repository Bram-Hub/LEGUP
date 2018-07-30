package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.Element;

public class MasyuLine extends Element
{
    private MasyuCell c1, c2;

    public MasyuLine(MasyuCell c1, MasyuCell c2)
    {
        this.c1 = c1;
        this.c2 = c2;

    }

    public MasyuCell getC1()
    {
        return c1;
    }

    public void setC1(MasyuCell c1)
    {
        this.c1 = c1;
    }

    public MasyuCell getC2()
    {
        return c2;
    }

    public void setC2(MasyuCell c2)
    {
        this.c2 = c2;
    }

    public boolean compare(MasyuLine line){
        return ((line.getC1().getLocation().equals(c1.getLocation()) && line.getC2().getLocation().equals(c2.getLocation())) ||
                (line.getC1().getLocation().equals(c2.getLocation()) && line.getC2().getLocation().equals(c1.getLocation())));
    }

    /**
     * Copies this elements element to a new Element object
     *
     * @return copied Element object
     */
    @Override
    public MasyuLine copy()
    {
        return new MasyuLine(c1.copy(), c2.copy());
    }
}
