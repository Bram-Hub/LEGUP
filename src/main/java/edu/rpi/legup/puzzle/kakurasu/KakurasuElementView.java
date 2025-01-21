package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class KakurasuElementView extends GridElementView {
    public KakurasuElementView(KakurasuCell cell) {
        super(cell);
    }

    /**
     * Draws on the given frame based on the type of the cell of the current puzzleElement
     *
     * @param graphics2D the frame to be drawn on
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        KakurasuCell cell = (KakurasuCell) puzzleElement;
        KakurasuType type = cell.getType();
        graphics2D.setStroke(new BasicStroke(0));
        if (type == KakurasuType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fill(
                    new Rectangle2D.Double(
                            location.x + 0.5f, location.y + 0.5f, size.width - 1, size.height - 1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(
                    new Rectangle2D.Double(
                            location.x + 0.5f, location.y + 0.5f, size.width - 1, size.height - 1));
        } else {
            if (type == KakurasuType.FILLED) {
                graphics2D.drawImage(
                        KakurasuView.FILLED,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
            } else {
                graphics2D.drawImage(
                        KakurasuView.EMPTY,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
            }
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
