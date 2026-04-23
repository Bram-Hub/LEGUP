package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.UIManager;

public class FillapixElementView extends GridElementView {

    public FillapixElementView(FillapixCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public FillapixCell getPuzzleElement() {
        return (FillapixCell) super.getPuzzleElement();
    }

    /**
     * Draws the fillapix puzzleElement to the screen
     *
     * @param graphics2D graphics object
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        FillapixCell cell = (FillapixCell) puzzleElement;
        FillapixCellType type = cell.getType();
        g.setColor(UIManager.getColor(
                switch (type) {
                    case BLACK -> "Fillapix.black";
                    case WHITE -> "Fillapix.white";
                    case UNKNOWN -> "Fillapix.unknown";
                }
        ));
        g.fillRect(location.x, location.y, size.width, size.height);
        if (cell.getNumber() >= 0 && cell.getNumber() < 10) {
            g.setColor(UIManager.getColor(
                    type == FillapixCellType.BLACK ? "Fillapix.white" : "Fillapix.black"));
            g.setFont(UIManager.getFont("Fillapix.font"));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String value = String.valueOf(cell.getNumber());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(value, xText, yText);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Fillapix.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Fillapix.borderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
