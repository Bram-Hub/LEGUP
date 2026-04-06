package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class HeyawakeElementView extends GridElementView {

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
        Graphics2D g = (Graphics2D) graphics2D.create();
        HeyawakeCell cell = (HeyawakeCell) puzzleElement;

        if (cell.getData() >= 0) {
            g.setColor(UIManager.getColor("Heyawake.black"));
            g.setFont(UIManager.getFont("Heyawake.font"));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Heyawake.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Heyawake.cellBorderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
