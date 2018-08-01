package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.utility.Entry;

public class TreeTentLine extends Element<Entry<TreeTentCell, TreeTentCell>>
{

    public TreeTentLine(TreeTentCell c1, TreeTentCell c2)
    {
        this.data = new Entry<>(c1, c2);
    }

    public TreeTentCell getC1()
    {
        return data.getKey();
    }

    public void setC1(TreeTentCell c1)
    {
        this.data.setKey(c1);
    }

    public TreeTentCell getC2()
    {
        return data.getValue();
    }

    public void setC2(TreeTentCell c2)
    {
        this.data.setValue(c2);
    }

    public boolean compare(TreeTentLine line){
        return ((line.getC1().getLocation().equals(data.getKey().getLocation()) && line.getC2().getLocation().equals(data.getValue().getLocation())) ||
                (line.getC1().getLocation().equals(data.getValue().getLocation()) && line.getC2().getLocation().equals(data.getKey().getLocation())));
    }

    /**
     * Copies this elements element to a new Element object
     *
     * @return copied Element object
     */
    @Override
    public TreeTentLine copy()
    {
        return new TreeTentLine(data.getKey().copy(), data.getValue().copy());
    }
}
