package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class YinYangElementView extends GridElementView {

    private static final Color WHITE_COLOR = Color.WHITE;
    private static final Color BLACK_COLOR = Color.BLACK;
    private static final Color UNKNOWN_COLOR = Color.LIGHT_GRAY;

    public YinYangElementView(YinYangCell cell) {
        super(cell);
    }

    @Override
    public YinYangCell getPuzzleElement() {
        return (YinYangCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        YinYangCell cell = getPuzzleElement();
        YinYangType type = cell.getType();

        graphics2D.setStroke(new BasicStroke(1));

        switch (type) {
            case WHITE:
                graphics2D.setColor(WHITE_COLOR);
                graphics2D.fillOval(location.x, location.y, size.width, size.height);
                break;

            case BLACK:
                graphics2D.setColor(BLACK_COLOR);
                graphics2D.fillOval(location.x, location.y, size.width, size.height);
                break;

            case UNKNOWN:
            default:
                graphics2D.setColor(UNKNOWN_COLOR);
                graphics2D.fillOval(location.x, location.y, size.width, size.height);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawOval(location.x, location.y, size.width, size.height);
                break;
        }
    }
}