package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class RippleEffectElementView extends GridElementView {

    public RippleEffectElementView(RippleEffectCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public RippleEffectCell getPuzzleElement() {
        return (RippleEffectCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        RippleEffectCell cell = getPuzzleElement();
        RippleEffectCellType type = cell.getType();

        // Draw the cell based on its type
        switch (type) {
            case WHITE:
                graphics2D.setColor(Color.WHITE);
                break;
            case BLUE:
                graphics2D.setColor(Color.BLUE);
                break;
            case RED:
                graphics2D.setColor(Color.RED);
                break;
            case YELLOW:
                graphics2D.setColor(Color.YELLOW);
                break;
            case GREEN:
                graphics2D.setColor(Color.GREEN);
                break;
            default:
                // For BLACK and any other type
                graphics2D.setColor(Color.BLACK);
                break;
        }

        // Fill the cell with the color
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        // Draw a black border
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        // Draw the number inside the cell
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("Arial", Font.BOLD, 14));
        String data = String.valueOf(cell.getNumber());
        FontMetrics metrics = graphics2D.getFontMetrics();
        int x = location.x + (size.width - metrics.stringWidth(data)) / 2;
        int y = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(data, x, y);
    }
}