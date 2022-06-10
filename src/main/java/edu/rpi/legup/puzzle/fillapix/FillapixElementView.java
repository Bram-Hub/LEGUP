package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class FillapixElementView extends GridElementView {
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);

    private static final Color BLACK_COLOR = new Color(0x212121);
    private static final Color WHITE_COLOR = new Color(0xF5F5F5);
    private static final Color GRAY_COLOR = new Color(0x9E9E9E);

    public FillapixElementView(FillapixCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public FillapixCell getPuzzleElement() {
        return (FillapixCell) super.getPuzzleElement();
    }

    /**
     * Draws the fillapix puzzleElement to the screen
     *
     * @param graphics2D graphics object
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        FillapixCell cell = (FillapixCell) puzzleElement;
        FillapixCellType type = cell.getType();
        graphics2D.setStroke(new BasicStroke(1));
        switch (type) {
            case UNKNOWN:
                graphics2D.setColor(GRAY_COLOR);
                break;
            case BLACK:
                graphics2D.setColor(BLACK_COLOR);
                break;
            default:
                graphics2D.setColor(WHITE_COLOR);
                break;
        }
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
        if (cell.getNumber() != -1) {
            graphics2D.setColor(type == FillapixCellType.WHITE ? BLACK_COLOR : WHITE_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(cell.getNumber());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
        }
        graphics2D.setColor(BLACK_COLOR);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}