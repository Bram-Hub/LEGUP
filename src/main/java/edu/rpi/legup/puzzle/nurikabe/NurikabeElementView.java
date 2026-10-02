package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class NurikabeElementView extends GridElementView {

    public NurikabeElementView(NurikabeCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public NurikabeCell getPuzzleElement() {
        return (NurikabeCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        NurikabeCell cell = (NurikabeCell) puzzleElement;
        NurikabeType type = cell.getType();
        if (type == NurikabeType.NUMBER) {
            g.setColor(UIManager.getColor("Nurikabe.white"));
            g.fillRect(location.x, location.y, size.width, size.height);

            g.setColor(UIManager.getColor("Nurikabe.text"));
            g.setFont(UIManager.getFont("Nurikabe.font"));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        } else if (type == NurikabeType.BLACK) {
            g.setColor(UIManager.getColor("Nurikabe.black"));
            g.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == NurikabeType.WHITE) {
            g.setColor(UIManager.getColor("Nurikabe.white"));
            g.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == NurikabeType.UNKNOWN) {
            g.setColor(UIManager.getColor("Nurikabe.unknown"));
            g.fillRect(location.x, location.y, size.width, size.height);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Nurikabe.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Nurikabe.borderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
