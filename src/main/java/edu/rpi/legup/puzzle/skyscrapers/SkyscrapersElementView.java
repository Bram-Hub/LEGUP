package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class SkyscrapersElementView extends GridElementView {
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = new Color(0x212121);
    private static final Color BORDER_COLOR = new Color(0x424242);
    private static final Color BACKGROUND_COLOR = new Color(0xEEEEEE);

    public SkyscrapersElementView(SkyscrapersCell cell) {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(BACKGROUND_COLOR);
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
        graphics2D.setColor(BORDER_COLOR);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        int val = cell.getData().value;
        if (val != 0) {
            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(val);
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
        }
    }
}
