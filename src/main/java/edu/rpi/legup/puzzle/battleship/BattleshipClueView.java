package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.ui.boardview.ElementView;

import java.awt.*;

public class BattleshipClueView extends ElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public BattleshipClueView(BattleshipClue clue) {
        super(clue);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public BattleshipClue getPuzzleElement() {
        return (BattleshipClue) super.getPuzzleElement();
    }

    @Override
    /**
     * Draws the clue from the PuzzleElement associated with this view on the given frame
     * @param graphics2D the frame the clue is to be drawn on
     */
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(FONT_COLOR);
        graphics2D.setFont(FONT);
        FontMetrics metrics = graphics2D.getFontMetrics(FONT);
        String value;

        BattleshipClue clue = getPuzzleElement();
        switch (clue.getType()) {
            case CLUE_NORTH:
                value = String.valueOf(clue.getData() + 1);
                break;
            case CLUE_EAST:
                value = String.valueOf(clue.getData());
                break;
            case CLUE_SOUTH:
                value = String.valueOf(clue.getData());
                break;
            case CLUE_WEST:
                value = BattleshipClue.colNumToString(clue.getData() + 1);
                break;
            default:
                value = "";
        }

        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(value, xText, yText);
    }
}
