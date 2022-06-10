package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class HeyawakeElementView extends GridElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);

    private static final Color BLACK_COLOR = new Color(0x212121);
    private static final Color WHITE_COLOR = new Color(0xF5F5F5);
    private static final Color GRAY_COLOR = new Color(0x9E9E9E);

    public HeyawakeElementView(HeyawakeCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public HeyawakeCell getPuzzleElement() {
        return (HeyawakeCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        HeyawakeCell cell = (HeyawakeCell) puzzleElement;
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(BLACK_COLOR);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        if (cell.getData() >= 0) {
            graphics2D.setColor(BLACK_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        }
    }
}
