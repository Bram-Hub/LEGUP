package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import java.awt.geom.Line2D;
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
        graphics2D.setColor(Color.BLACK);

        StarBattleBorder border= getPuzzleElement();
        float xSize = size.width;
        float ySize = size.height;
        Color borderColor = Color.BLACK;
        float borderWidth = 3.0f; //ySize = ySize / 15
        if(type == StarBattleCellType.HORIZ_BORDER){    //minimize ySize / height
            graphics2D.setColor(borderColor);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.draw(
                    new Line2D.Double(
                            location.x, location.y, location.x + (xSize), location.y));
        }
        else if(type == StarBattleCellType.VERT_BORDER){    //minimize xSize / width
            graphics2D.setColor(borderColor);
            System.out.println("Border width = " + xSize + "\n");
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.draw(
                    new Line2D.Double(
                            location.x, location.y, location.x, location.y + (ySize)));
        }
        //This needs more work but it'll do for now (add  - (xSize / 2) to location.y and x that don't have it)
        //Also, look into changing width of borders, size of cell = 30 units
    }
}
