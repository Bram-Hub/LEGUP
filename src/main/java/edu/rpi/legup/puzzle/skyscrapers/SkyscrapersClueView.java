package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import javax.swing.UIManager;

public class SkyscrapersClueView extends ElementView {

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
        if (isHover()) {
            drawHover(graphics2D);
        }
        if (isShowCasePicker() && isCaseRulePickable()) {
            drawCase(graphics2D);
        }
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Skyscrapers.clue"));
        g.setFont(UIManager.getFont("Skyscrapers.font"));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
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

        // REPRESENT NO CLUE AS EMPTY STRING INSTEAD OF 0, SOLVING PUZZLES WITH NO CLUE IS CURRENTLY
        // NOT WORKING
        // IF YOU ARE IMPLEMENTING NO CLUE FUNCTIONALITY, UNCOMMENT BELOW CODE AND DELETE OTHER IF
        // STATEMENT,
        // ADDITIONALLY, GO TO SkyscrapersBoard AND EDIT LINES 220 AND 223 SO YOU CAN CYCLE FOR NO
        // CLUE
        // IN THE SKYSCRAPERS PUZZLE EDITOR
        //        if (value.equals("0")) {
        //            value = "";
        //        }
        if (value.equals("0")) {
            value = "1";
            clue.setData(1);
        }

        g.drawString(value, xText, yText);
        g.dispose();
    }
}
