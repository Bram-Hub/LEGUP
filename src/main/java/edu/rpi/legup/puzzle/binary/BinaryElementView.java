package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class BinaryElementView extends GridElementView {

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

    /**
     * Overlays the cells provided in the puzzle's .xml file with light gray filter
     *
     * @param graphics2D The graphics object to draw on
     */
    @Override
    public void drawGiven(Graphics2D graphics2D) {
        graphics2D.setColor(UIManager.getColor("Binary.given"));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
    }

    /**
     * Draws new cells being added to board with white background
     *
     * @param graphics2D The graphics object to draw on
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setColor(UIManager.getColor("Binary.background"));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        if (((BinaryCell) puzzleElement).getType() == BinaryType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(UIManager.getInt("Binary.borderWidthUnknown")));
        }
        else {
            graphics2D.setStroke(new BasicStroke(UIManager.getInt("Binary.borderWidthKnown")));
            drawCenteredText(graphics2D);
        }
        graphics2D.setColor(UIManager.getColor("Binary.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }

    /**
     * Helper method to draw the centered text within the cell
     *
     * @param graphics2D The graphics object to draw on
     */
    private void drawCenteredText(Graphics2D graphics2D) {
        graphics2D.setColor(UIManager.getColor("Binary.text"));
        graphics2D.setFont(UIManager.getFont("Binary.font"));
        FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
        String value = String.valueOf(puzzleElement.getData());
        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(value, xText, yText);
    }
}
