package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;

public class TreeTentLineView extends ElementView {
    private final Color LINE_COLOR = Color.GREEN;

    private final Stroke LINE_STROKE = new BasicStroke(2);

    /**
     * Constructs a TreeTentLineView for the given TreeTentLine.
     *
     * @param line the TreeTentLine to be visually represented
     */
    public TreeTentLineView(TreeTentLine line) {
        super(line);
    }

    /**
     * Draws the line connecting two TreeTentCells on the board. The line is drawn between the
     * centers of the two cells and changes color depending on whether the line has been modified.
     *
     * @param graphics2D the Graphics2D context used for rendering
     */
    @Override
    public void draw(Graphics2D graphics2D) {

        TreeTentLine line = (TreeTentLine) puzzleElement;
        Point p1 = line.getC1().getLocation();
        Point p2 = line.getC2().getLocation();
        int x1 = (p1.x + 1) * size.width + size.width / 2;
        int y1 = (p1.y + 1) * size.height + size.height / 2;

        int x2 = (p2.x + 1) * size.width + size.width / 2;
        int y2 = (p2.y + 1) * size.height + size.height / 2;
        // graphics2D.setColor(LINE_COLOR);
        graphics2D.setColor(line.isModified() ? Color.GREEN : Color.WHITE);
        graphics2D.setStroke(LINE_STROKE);
        graphics2D.drawLine(x1, y1, x2, y2);
    }
}
