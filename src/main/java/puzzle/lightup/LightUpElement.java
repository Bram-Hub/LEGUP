package puzzle.lightup;

import ui.boardview.GridElement;

import java.awt.*;

public class LightUpElement extends GridElement
{
    private static final Color LITE = new Color(255,255,0,63);
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.WHITE;

    public LightUpElement(LightUpCell cell)
    {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D)
    {
        LightUpCell cell = (LightUpCell)data;
        LightUpCellType type = cell.getType();
        if(type == LightUpCellType.NUMBER)
        {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(data.getValueInt());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(data.getValueInt()), xText, yText);
        }
        else if(type == LightUpCellType.BLACK)
        {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        }
        else if(type == LightUpCellType.EMPTY)
        {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(cell.isLite() ? LITE : Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(location.x + size.width * 7 / 16, location.y + size.height * 7 / 16, size.width / 8, size.height / 8);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        else if(type == LightUpCellType.UNKNOWN)
        {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(cell.isLite() ? LITE : Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        else if(type == LightUpCellType.BULB)
        {
            graphics2D.drawImage(LightUpView.lightImage, location.x, location.y, size.width, size.height, LITE, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }

//        if(data.isModified())
//        {
//            graphics2D.setColor(Color.GREEN);
//            graphics2D.setStroke(new BasicStroke(2));
//            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
//        }
    }
}
