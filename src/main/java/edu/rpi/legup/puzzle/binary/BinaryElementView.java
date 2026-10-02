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
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Binary.given"));
        g.fillRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }

    /**
     * Draws new cells being added to board with white background
     *
     * @param graphics2D The graphics object to draw on
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Binary.background"));
        g.fillRect(location.x, location.y, size.width, size.height);
        if (((BinaryCell) puzzleElement).getType() != BinaryType.UNKNOWN) {
            drawCenteredText(g);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setStroke(
                new BasicStroke(
                        UIManager.getInt(
                                ((BinaryCell) puzzleElement).getType() == BinaryType.UNKNOWN
                                        ? "Binary.unknownBorderWidth"
                                        : "Binary.knownBorderWidth")));
        g.setColor(UIManager.getColor("Binary.borderColor"));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }

    /**
     * Helper method to draw the centered text within the cell
     *
     * @param graphics2D The graphics object to draw on
     */
    private void drawCenteredText(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Binary.text"));
        g.setFont(UIManager.getFont("Binary.font"));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String value = String.valueOf(puzzleElement.getData());
        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(value, xText, yText);
        g.dispose();
    }
}
