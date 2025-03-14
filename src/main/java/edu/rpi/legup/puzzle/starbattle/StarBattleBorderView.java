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
        graphics2D.setColor(Color.BLACK);

        StarBattleBorder border= getPuzzleElement();

        int xSize = size.width;
        int ySize = size.height;
        if(type == StarBattleCellType.HORIZ_BORDER){    //minimize ySize / height
            graphics2D.setColor(Color.BLACK);
            //dump this
            //System.out.println("Horizontal -- Border, coords " + location.x + "," + location.y + "\n");
            ySize = ySize / 8;
            xSize = xSize + (ySize);
            graphics2D.fillRect(location.x - (ySize / 2), location.y, xSize, ySize);
        }
        else if(type == StarBattleCellType.VERT_BORDER){    //minimize xSize / width
            graphics2D.setColor(Color.BLACK);
            //System.out.println("Vertical | Border, c0ords " + location.x + "," + location.y + "\n");
            xSize = xSize / 8;
            ySize = ySize + (xSize);
            graphics2D.fillRect(location.x, location.y - (xSize / 2), xSize, ySize);
        }
        //This needs more work but it'll do for now (add  - (xSize / 2) to location.y and x that don't have it)
        //Also, look into changing width of borders, size of cell = 30 units
    }
}
