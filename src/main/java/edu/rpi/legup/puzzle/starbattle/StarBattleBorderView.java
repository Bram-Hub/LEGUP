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
        Color borderColor = Color.RED;
        float borderWidth = 3.0f; //ySize = ySize / 15
        if(type == StarBattleCellType.HORIZ_BORDER){    //minimize ySize / height
            graphics2D.setColor(borderColor);
            //dump this
            //System.out.println("Horizontal -- Border, coords " + location.x + "," + location.y + "\n");
            ySize = borderWidth;
            xSize = xSize + (ySize);
            //graphics2D.fillRect(location.x - (ySize / 2), location.y, xSize, ySize);
            graphics2D.setStroke(new BasicStroke(3));
            /*
            graphics2D.draw(
                    new Rectangle2D.Double(
                            location.x - (borderWidth/2.0f), location.y - (borderWidth/2.0f), xSize, ySize));

             */
            graphics2D.draw(
                    new Line2D.Double(
                            location.x, location.y, location.x, location.y + xSize));
        }
        else if(type == StarBattleCellType.VERT_BORDER){    //minimize xSize / width
            graphics2D.setColor(borderColor);
            //System.out.println("Vertical | Border, c0ords " + location.x + "," + location.y + "\n");
            xSize = borderWidth;
            ySize = ySize + (xSize);
            System.out.println("Border width = " + xSize + "\n");
            //graphics2D.fillRect(location.x, location.y - (xSize / 2), xSize, ySize);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.draw(
                    new Line2D.Double(
                            location.x, location.y, location.x + ySize, location.y));
        }
        //This needs more work but it'll do for now (add  - (xSize / 2) to location.y and x that don't have it)
        //Also, look into changing width of borders, size of cell = 30 units
    }
}
