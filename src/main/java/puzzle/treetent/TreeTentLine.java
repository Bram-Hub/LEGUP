package puzzle.treetent;

import model.gameboard.Element;

public class TreeTentLine extends Element
{
    private TreeTentCell c1, c2;

    public TreeTentLine(TreeTentCell c1, TreeTentCell c2)
    {
        this.c1 = c1;
        this.c2 = c2;

    }

    public TreeTentCell getC1()
    {
        return c1;
    }

    public void setC1(TreeTentCell c1)
    {
        this.c1 = c1;
    }

    public TreeTentCell getC2()
    {
        return c2;
    }

    public void setC2(TreeTentCell c2)
    {
        this.c2 = c2;
    }

    public boolean compare(TreeTentLine l){
        return ((l.getC1().getLocation().equals(c1.getLocation()) && l.getC2().getLocation().equals(c2.getLocation())) || (l.getC1().getLocation().equals(c2.getLocation()) && l.getC2().getLocation().equals(c1.getLocation())));
    }

    /**
     * Copies this elements element to a new Element object
     *
     * @return copied Element object
     */
    @Override
    public TreeTentLine copy()
    {
        return new TreeTentLine(c1.copy(), c2.copy());
    }
}
