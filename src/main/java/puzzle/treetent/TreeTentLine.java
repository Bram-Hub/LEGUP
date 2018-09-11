package puzzle.treetent;

import model.gameboard.ElementData;

public class TreeTentLine extends ElementData
{
    private TreeTentCell c1, c2;

    public TreeTentLine(TreeTentCell c1, TreeTentCell c2)
    {
        this.c1 = c1;
        this.c2 = c2;
        this.setIndex(-1);

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
     * Copies this elements data to a new ElementData object
     *
     * @return copied ElementData object
     */
    @Override
    public TreeTentLine copy()
    {
        return new TreeTentLine(c1.copy(), c2.copy());
    }
    @Override
    public String getValueString(){
        return "LINE";
    }



}
