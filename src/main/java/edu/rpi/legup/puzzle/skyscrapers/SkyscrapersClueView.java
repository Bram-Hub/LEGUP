package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;

public class SkyscrapersClueView extends ElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public SkyscrapersClueView(SkyscrapersClue clue) {
        super(clue);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public SkyscrapersClue getPuzzleElement() {
        return (SkyscrapersClue) super.getPuzzleElement();
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
        graphics2D.setColor(FONT_COLOR);
        graphics2D.setFont(FONT);
        FontMetrics metrics = graphics2D.getFontMetrics(FONT);
        String value;

        SkyscrapersClue clue = getPuzzleElement();
        switch (clue.getType()) {
            case CLUE_NORTH:
            case CLUE_EAST:
            case CLUE_SOUTH:
            case CLUE_WEST:
                value = String.valueOf(clue.getData());
                break;
            default:
                value = "";
        }

        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(value, xText, yText);
    }
}
