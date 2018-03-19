package puzzle.nurikabe;

import model.gameboard.GridBoard;

public class NurikabeBoard extends GridBoard
{
    public NurikabeBoard(int width, int height)
    {
        super(width, height);
    }

    public NurikabeBoard(int size)
    {
        super(size, size);
    }

    @Override
    public NurikabeCell getCell(int x, int y)
    {
        return (NurikabeCell) super.getCell(x, y);
    }

    @Override
    public NurikabeBoard copy()
    {
        NurikabeBoard copy = new NurikabeBoard(dimension.width, dimension.height);
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
