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
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Skyscrapers.background"));
        g.fillRect(location.x, location.y, size.width, size.height);

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        int val = cell.getData();
        if (val != 0) {
            g.setColor(UIManager.getColor("Skyscrapers.text"));
            g.setFont(UIManager.getFont("Skyscrapers.font"));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String value = String.valueOf(val);
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(value, xText, yText);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Skyscrapers.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Skyscrapers.borderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
