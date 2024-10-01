package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StarBattleBorderView extends ElementView {
    private static final Color Border_COLOR = Color.BLACK;
    private StarBattleCellType type;

    public StarBattleBorderView(StarBattleBorder border) {
        super(border);
        type = border.getType();
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

        int xSize = size.width;
        int ySize = size.height;
        if(type == StarBattleCellType.HORIZ_BORDER){    //minimize ySize / height
            ySize = ySize / 8;
        }
        else if(type == StarBattleCellType.VERT_BORDER){    //minimize xSize / width
            xSize = xSize / 8;
        }
        graphics2D.draw(new Rectangle2D.Double(
                location.x, location.y, xSize, ySize));
    }
}
