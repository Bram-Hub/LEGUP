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
        HeyawakeCell cell = (HeyawakeCell) puzzleElement;

        if (cell.getData() >= 0) {
            graphics2D.setColor(UIManager.getColor("Heyawake.black"));
            graphics2D.setFont(UIManager.getFont("Heyawake.font"));
            FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        }

        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Heyawake.cellBorderWidth")));
        graphics2D.setColor(UIManager.getColor("Heyawake.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
