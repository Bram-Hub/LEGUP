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

        graphics2D.setColor(UIManager.getColor("Masyu.background"));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        if (type == MasyuType.BLACK) {
            graphics2D.setColor(UIManager.getColor("Masyu.black"));
            graphics2D.fillOval(location.x + 5, location.y + 5, 20, 20);
        } else if (type == MasyuType.WHITE) {
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setColor(UIManager.getColor("Masyu.white"));
            graphics2D.fillOval(location.x + 5, location.y + 5, 20, 20);
            graphics2D.setColor(UIManager.getColor("Masyu.black"));
            graphics2D.drawOval(location.x + 6, location.y + 6, 18, 18);
        }
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Masyu.borderWidth")));
        graphics2D.setColor(UIManager.getColor("Masyu.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
