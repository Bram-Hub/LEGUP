package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import javax.swing.UIManager;

public class MasyuLineView extends ElementView {

    public MasyuLineView(MasyuLine line) {
        super(line);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        drawElement(graphics2D);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        MasyuLine line = (MasyuLine) puzzleElement;
        Point p1 = line.getC1().getLocation();
        Point p2 = line.getC2().getLocation();
        int x1 = p1.x * size.width + size.width / 2;
        int y1 = p1.y * size.height + size.height / 2;
        int x2 = p2.x * size.width + size.width / 2;
        int y2 = p2.y * size.height + size.height / 2;

        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor(line.isModified() ? "Puzzle.valid" : "Masyu.line"));
        g.setStroke(new BasicStroke(UIManager.getInt("Masyu.lineWidth")));
        g.drawLine(x1, y1, x2, y2);
        g.dispose();
    }
}
