package puzzle.fillapix;

import model.gameboard.GridCell;
import ui.boardview.GridElement;

import java.awt.*;

public class FillapixElement extends GridElement
{
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;
    private static final Color GREY = new Color(200, 200, 200);
    private static final Color BORDER_COLOR = Color.BLACK;
    public static FillapixCell fillapixCell;

    public FillapixElement(GridCell cell)
    {
        super(cell);
        fillapixCell = (FillapixCell) data;
    }

    /**
     * Draws the fillapix element to the screen
     *
     * @param graphics2D graphics object
     */
    @Override
    public void drawElement(Graphics2D graphics2D)
    {
        FillapixCell cell = new FillapixCell(data.getValueInt(), location);

        Color cellColor = null;
        Color textColor = FONT_COLOR;
        if(cell.isUnknown())
        {
            cellColor = GREY;
        }
        else if(cell.isBlack())
        {
            cellColor = Color.BLACK;
            textColor = GREY;
        }
        else if(cell.isWhite())
        {
            cellColor = Color.WHITE;
        }
        graphics2D.setColor(cellColor);
        graphics2D.fillRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);

        if(cell.isGiven())
        {
            graphics2D.setColor(textColor);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String val = String.valueOf(cell.getClue());
            int xText = location.x + (size.width - metrics.stringWidth(val)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(cell.getClue()), xText, yText);
        }

        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        if(isHover())
        {
            graphics2D.setColor(new Color(63, 101, 244));
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
        }

        if(data.isModified())
        {
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setColor(Color.GREEN);
            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
        }
    }
}