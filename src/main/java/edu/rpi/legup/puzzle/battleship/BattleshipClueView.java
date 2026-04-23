package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import javax.swing.UIManager;

public class BattleshipClueView extends ElementView {

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
    public void draw(Graphics2D graphics2D) {
        drawElement(graphics2D);
    }

    /**
     * Draws the clue from the PuzzleElement associated with this view on the given frame
     *
     * @param graphics2D the frame the clue is to be drawn on
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Battleship.clue"));
        g.setFont(UIManager.getFont("Battleship.clueFont"));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String value;

        BattleshipClue clue = getPuzzleElement();
        switch (clue.getType()) {
            case CLUE_NORTH:
                value = String.valueOf(clue.getData() + 1);
                break;
            case CLUE_EAST, CLUE_SOUTH:
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
        g.drawString(value, xText, yText);
        g.dispose();
    }
}
