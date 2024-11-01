package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class KakurasuElementView extends GridElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public KakurasuElementView(KakurasuCell cell) {
        super(cell);
    }

    @Override
    public KakurasuCell getPuzzleElement() {
        return (KakurasuCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        KakurasuCell cell = (KakurasuCell) puzzleElement;
        KakurasuType type = cell.getType();
        if (type == KakurasuType.FILLED) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == KakurasuType.EMPTY) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == KakurasuType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}