package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.utility.Entry;

public class MasyuLine extends Element<Entry<MasyuCell, MasyuCell>>
{
    public MasyuLine(MasyuCell c1, MasyuCell c2)
    {
        this.data = new Entry<>(c1, c2);
    }

    public MasyuCell getC1()
    {
        return data.getKey();
    }

    public void setC1(MasyuCell c1)
    {
        this.data.setKey(c1);
    }

    public MasyuCell getC2()
    {
        return data.getValue();
    }

    public void setC2(MasyuCell c2)
    {
        this.data.setValue(c2);
    }

    public boolean compare(MasyuLine line)
    {
        return ((line.getC1().getLocation().equals(data.getKey().getLocation()) && line.getC2().getLocation().equals(data.getValue().getLocation())) ||
                (line.getC1().getLocation().equals(data.getValue().getLocation()) && line.getC2().getLocation().equals(data.getKey().getLocation())));
    }

    /**
     * Copies this elements element to a new Element object
     *
     * @return copied Element object
     */
    @Override
    public MasyuLine copy()
    {
        return new MasyuLine(data.getKey().copy(), data.getValue().copy());
    }
}
