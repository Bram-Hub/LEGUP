package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import javax.swing.UIManager;

public class TreeTentClueView extends ElementView {

    public TreeTentClueView(TreeTentClue clue) {
        super(clue);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public TreeTentClue getPuzzleElement() {
        return (TreeTentClue) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("TreeTent.text"));
        g.setFont(UIManager.getFont("TreeTent.font"));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String value;

        TreeTentClue clue = getPuzzleElement();
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
                value = TreeTentClue.colNumToString(clue.getData() + 1);
                break;
            default:
                value = "";
        }

        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(value, xText, yText);
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {}
}
