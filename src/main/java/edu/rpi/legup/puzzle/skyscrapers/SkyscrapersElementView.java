package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class SkyscrapersElementView extends GridElementView {

    public SkyscrapersElementView(SkyscrapersCell cell) {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setColor(UIManager.getColor("Skyscrapers.background"));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        int val = cell.getData();
        if (val != 0) {
            graphics2D.setColor(UIManager.getColor("Skyscrapers.text"));
            graphics2D.setFont(UIManager.getFont("Skyscrapers.font"));
            FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
            String value = String.valueOf(val);
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
        }

        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Skyscrapers.borderWidth")));
        graphics2D.setColor(UIManager.getColor("Skyscrapers.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
