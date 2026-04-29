package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class MasyuElementView extends GridElementView {
    public MasyuElementView(MasyuCell masyuCell) {
        super(masyuCell);
    }

    @Override
    public MasyuCell getPuzzleElement() {
        return (MasyuCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        MasyuCell cell = (MasyuCell) puzzleElement;
        MasyuType type = cell.getType();

        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Masyu.background"));
        g.fillRect(location.x, location.y, size.width, size.height);

        if (type == MasyuType.BLACK) {
            g.setColor(UIManager.getColor("Masyu.black"));
            g.fillOval(location.x + 5, location.y + 5, 20, 20);
        } else if (type == MasyuType.WHITE) {
            g.setStroke(new BasicStroke(2));
            g.setColor(UIManager.getColor("Masyu.white"));
            g.fillOval(location.x + 5, location.y + 5, 20, 20);
            g.setColor(UIManager.getColor("Masyu.black"));
            g.drawOval(location.x + 6, location.y + 6, 18, 18);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Masyu.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Masyu.borderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
