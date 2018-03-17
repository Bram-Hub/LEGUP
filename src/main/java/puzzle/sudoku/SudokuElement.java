package puzzle.sudoku;

import model.gameboard.GridCell;
import ui.boardview.GridElement;

import java.awt.*;

public class SudokuElement extends GridElement
{
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = Color.BLACK;

    public SudokuElement(GridCell cell)
    {
        super(cell);
    }

    /**
     * Draws the sudoku element to the screen
     *
     * @param graphics2D graphics object
     */
    public void draw(Graphics2D graphics2D)
    {
        SudokuCell cell = (SudokuCell)data;
        if(cell.isGiven())
        {
            graphics2D.setColor(new Color(200,200,200));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        }

        if(isHover())
        {
            graphics2D.setColor(new Color(63,101,244));
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
        }
        if(data.isModified())
        {
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setColor(Color.GREEN);
            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
        }

        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        if(data.getValueInt() != 0)
        {
            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(data.getValueInt());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(data.getValueInt()), xText, yText);
        }
    }
}
