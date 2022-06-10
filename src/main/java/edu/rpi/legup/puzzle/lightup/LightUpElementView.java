package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class LightUpElementView extends GridElementView {
    private static final Color LITE = new Color(0xFFF176);
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);

    private static final Color BLACK_COLOR = new Color(0x212121);
    private static final Color WHITE_COLOR = new Color(0xF5F5F5);
    private static final Color GRAY_COLOR = new Color(0x9E9E9E);

    public LightUpElementView(LightUpCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public LightUpCell getPuzzleElement() {
        return (LightUpCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        LightUpCell cell = (LightUpCell) puzzleElement;
        LightUpCellType type = cell.getType();
        if (type == LightUpCellType.NUMBER) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(BLACK_COLOR);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(WHITE_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        } else if (type == LightUpCellType.BLACK) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(BLACK_COLOR);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == LightUpCellType.EMPTY) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(cell.isLite() ? LITE : WHITE_COLOR);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(BLACK_COLOR);
            graphics2D.fillRect(location.x + size.width * 7 / 16, location.y + size.height * 7 / 16, size.width / 8, size.height / 8);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == LightUpCellType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(cell.isLite() ? LITE : Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == LightUpCellType.BULB) {
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.drawImage(LightUpView.lightImage, location.x, location.y, size.width, size.height, LITE, null);
            graphics2D.setColor(BLACK_COLOR);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
