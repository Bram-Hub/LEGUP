package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.ui.boardview.ElementView;

import java.awt.*;

public class KakurasuClueView extends ElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public KakurasuClueView(KakurasuClue clue) {
        super(clue);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public KakurasuClue getPuzzleElement() {
        return (KakurasuClue) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setColor(FONT_COLOR);
        graphics2D.setFont(FONT);
        FontMetrics metrics = graphics2D.getFontMetrics(FONT);
        String value;

        KakurasuClue clue = getPuzzleElement();
        value = switch (clue.getType()) {
            case CLUE_NORTH, CLUE_WEST -> String.valueOf(clue.getData() + 1);
            case CLUE_EAST, CLUE_SOUTH -> String.valueOf(clue.getData());
            default -> "";
        };

        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(value, xText, yText);
    }
}
