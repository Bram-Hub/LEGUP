package ui.boardview;

import model.gameboard.GridCell;

import java.awt.*;

public class GridElement extends PuzzleElement
{
    protected GridCell cell;

    public GridElement(GridCell cell)
    {
        super(cell);
        this.cell = cell;
    }

    /**
     * Draws the puzzle element on the screen
     *
     * @param graphics2D graphics2D object used for drawing
     */
    @Override
    public void draw(Graphics2D graphics2D)
    {
        draw(graphics2D, cell.getLocation().x, cell.getLocation().y);
    }

    /**
     * Draws the grid element to the screen
     *
     * @param graphics2D graphics object
     * @param x x position on the board
     * @param y y position on the board
     */
    public void draw(Graphics2D graphics2D, int x, int y)
    {
        GridCell cell = (GridCell)data;
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, size.width, size.height);
        graphics2D.drawString(cell.getValueString(), x, y);
    }

}
