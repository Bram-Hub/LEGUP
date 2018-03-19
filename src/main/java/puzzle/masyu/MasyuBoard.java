package puzzle.masyu;

import model.gameboard.GridBoard;

public class MasyuBoard extends GridBoard
{

    public MasyuBoard(int width, int height)
    {
        super(width, height);
    }

    public MasyuBoard(int size)
    {
        super(size, size);
    }

    @Override
    public MasyuCell getCell(int x, int y)
    {
        return (MasyuCell)super.getCell(x, y);
    }

    @Override
    public MasyuBoard copy()
    {
        MasyuBoard copy = new MasyuBoard(dimension.width, dimension.height);
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
