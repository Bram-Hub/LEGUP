package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

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
        if (type == MasyuType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == MasyuType.BLACK) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillOval(location.x + 5, location.y + 5, 20, 20);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == MasyuType.WHITE) {
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillOval(location.x + 5, location.y + 5, 20, 20);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawOval(location.x + 6, location.y + 6, 18, 18);
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
