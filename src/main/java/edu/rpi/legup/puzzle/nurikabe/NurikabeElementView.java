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
        NurikabeCell cell = (NurikabeCell) puzzleElement;
        NurikabeType type = cell.getType();
        if (type == NurikabeType.NUMBER) {
            graphics2D.setColor(UIManager.getColor("Nurikabe.white"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(UIManager.getColor("Nurikabe.text"));
            graphics2D.setFont(UIManager.getFont("Nurikabe.font"));
            FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        } else if (type == NurikabeType.BLACK) {
            graphics2D.setColor(UIManager.getColor("Nurikabe.black"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == NurikabeType.WHITE) {
            graphics2D.setColor(UIManager.getColor("Nurikabe.white"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == NurikabeType.UNKNOWN) {
            graphics2D.setColor(UIManager.getColor("Nurikabe.unknown"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        }
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Nurikabe.borderWidth")));
        graphics2D.setColor(UIManager.getColor("Nurikabe.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
