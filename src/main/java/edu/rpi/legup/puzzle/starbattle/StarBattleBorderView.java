package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StarBattleBorderView extends ElementView {
    private static final Color Border_COLOR = Color.BLACK;

    public StarBattleBorderView(StarBattleBorder border) {
        super(border);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public StarBattleBorder getPuzzleElement() {
        return (StarBattleBorder) super.getPuzzleElement();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        drawElement(graphics2D);
        if (this.isShowCasePicker() && this.isCaseRulePickable()) {
            drawCase(graphics2D);
            if (this.isHover()) {
                drawHover(graphics2D);
            }
        }
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setColor(Border_COLOR);

        StarBattleBorder border= getPuzzleElement();

        int xBorder = location.x + (size.width / 2);
        int yBorder = location.y + (size.height / 2);
        graphics2D.draw(new Rectangle2D.Double(
                location.x + 1.5f, location.y + 1.5f, size.width - 3, size.height - 3));
    }
}
