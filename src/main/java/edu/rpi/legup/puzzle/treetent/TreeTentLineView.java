package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import javax.swing.UIManager;

public class TreeTentLineView extends ElementView {

    private final Stroke LINE_STROKE = new BasicStroke(2);

    public TreeTentLineView(TreeTentLine line) {
        super(line);
    }

    @Override
    public void draw(Graphics2D graphics2D) {

        TreeTentLine line = (TreeTentLine) puzzleElement;
        Point p1 = line.getC1().getLocation();
        Point p2 = line.getC2().getLocation();
        int x1 = (p1.x + 1) * size.width + size.width / 2;
        int y1 = (p1.y + 1) * size.height + size.height / 2;

        int x2 = (p2.x + 1) * size.width + size.width / 2;
        int y2 = (p2.y + 1) * size.height + size.height / 2;
        graphics2D.setColor(line.isModified() ? UIManager.getColor("Puzzle.valid") : Color.WHITE);
        graphics2D.setStroke(LINE_STROKE);
        graphics2D.drawLine(x1, y1, x2, y2);
    }
}
