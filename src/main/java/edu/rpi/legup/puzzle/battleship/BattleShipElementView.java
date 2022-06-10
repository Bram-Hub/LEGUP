package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class BattleShipElementView extends GridElementView {
    private static final Stroke OUTLINE_STROKE = new BasicStroke(1);
    private static final Color OUTLINE_COLOR = new Color(0x212121);

    private static final Color UNKNOWN_COLOR = new Color(0xE0E0E0);
    private static final Color WATER_COLOR = new Color(0x1565C0);
    private static final Color SHIP_COLOR = new Color(0x757575);

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 10);
    private static final Color FONT_COLOR = new Color(0xFFEB3B);

    public BattleShipElementView(BattleShipCell cell) {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        BattleShipCell cell = (BattleShipCell) puzzleElement;
        BattleShipCellType type = cell.getType();

        switch (type) {
            case UNKNOWN:
                graphics2D.setColor(UNKNOWN_COLOR);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                break;
            case WATER:
                graphics2D.setColor(WATER_COLOR);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                break;
            case SHIP_SEGMENT_UNKNOWN:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillRect(location.x + 3 * size.width / 8, location.y + 3 * size.height / 8,
                        size.width / 4, size.height / 4);

                graphics2D.setColor(FONT_COLOR);
                graphics2D.setFont(FONT);
                FontMetrics metrics = graphics2D.getFontMetrics(FONT);
                String value = "?";
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
                graphics2D.drawString(value, xText, yText);
                break;
            case SHIP_SIZE_1:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillOval(location.x + size.width / 4, location.y + size.width / 4,
                        size.width / 2, size.height / 2);
                break;
            case SHIP_SEGMENT_TOP:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillArc(location.x, location.y - size.height / 2, size.width, size.height, 180, 180);
                break;
            case SHIP_SEGMENT_RIGHT:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillArc(location.x + size.height / 2, location.y, size.width, size.height, 90, 180);
                break;
            case SHIP_SEGMENT_BOTTOM:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillArc(location.x, location.y + size.height / 2, size.width, size.height, 0, 180);
                break;
            case SHIP_SEGMENT_LEFT:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillArc(location.x - size.height / 2, location.y, size.width, size.height, 270, 180);
                break;
            case SHIP_SEGMENT_MIDDLE:
                graphics2D.setColor(SHIP_COLOR);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                break;
            default:
                graphics2D.setColor(new Color(0xE040FB));
                break;
        }

        graphics2D.setColor(OUTLINE_COLOR);
        graphics2D.setStroke(OUTLINE_STROKE);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}