package puzzle.treetent;

import model.gameboard.GridBoard;

public class TreeTentBoard extends GridBoard
{

    public TreeTentBoard(int width, int height)
    {
        super(width, height);
    }

    public TreeTentBoard(int size)
    {
        super(size, size);
    }

    @Override
    public TreeTentCell getCell(int x, int y)
    {
        return (TreeTentCell)super.getCell(x, y);
    }

    @Override
    public TreeTentBoard copy()
    {
        TreeTentBoard copy = new TreeTentBoard(dimension.width, dimension.height);
        for(int x = 0; x < this.dimension.width; x++)
        {
            for(int y = 0; y < this.dimension.height; y++)
            {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        return copy;
    }
}
