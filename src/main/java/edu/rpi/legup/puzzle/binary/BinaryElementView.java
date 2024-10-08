package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;

public class BinaryElementView extends GridElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 17);
    private static final Color FONT_COLOR = Color.BLACK;

    public BinaryElementView(BinaryCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public BinaryCell getPuzzleElement() {
        return (BinaryCell) super.getPuzzleElement();
    }

    @Override
    public void drawGiven(Graphics2D graphics2D) {
        BinaryCell cell = (BinaryCell) puzzleElement;
        BinaryType type = cell.getType();
        if (type == BinaryType.ZERO) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        } else {
            if (type == BinaryType.ONE) {
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.LIGHT_GRAY);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
                graphics2D.setColor(FONT_COLOR);
                graphics2D.setFont(FONT);
                FontMetrics metrics = graphics2D.getFontMetrics(FONT);
                String value = String.valueOf(puzzleElement.getData());
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);

            } else {
                if (type == BinaryType.UNKNOWN) {
                    graphics2D.setStroke(new BasicStroke(0));
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.fillRect(location.x, location.y, size.width, size.height);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawRect(location.x, location.y, size.width, size.height);
                }
            }
        }
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        BinaryCell cell = (BinaryCell) puzzleElement;
        BinaryType type = cell.getType();
        if (type == BinaryType.ZERO) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        } else {
            if (type == BinaryType.ONE) {
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
                graphics2D.setColor(FONT_COLOR);
                graphics2D.setFont(FONT);
                FontMetrics metrics = graphics2D.getFontMetrics(FONT);
                String value = String.valueOf(puzzleElement.getData());
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);

            } else {
                if (type == BinaryType.UNKNOWN) {
                    graphics2D.setStroke(new BasicStroke(0));
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.fillRect(location.x, location.y, size.width, size.height);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawRect(location.x, location.y, size.width, size.height);
                }
            }
        }
    }
}
