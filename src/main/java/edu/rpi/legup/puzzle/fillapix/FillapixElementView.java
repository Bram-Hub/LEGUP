package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
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
        FillapixCell cell = (FillapixCell) puzzleElement;
        FillapixCellType type = cell.getType();
        graphics2D.setColor(UIManager.getColor(
                switch (type) {
                    case BLACK -> "Fillapix.black";
                    case WHITE -> "Fillapix.white";
                    case UNKNOWN -> "Fillapix.unknown";
                }
        ));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
        if (cell.getNumber() >= 0 && cell.getNumber() < 10) {
            graphics2D.setColor(UIManager.getColor(
                    type == FillapixCellType.WHITE ? "Fillapix.black" : "Fillapix.white"));
            graphics2D.setFont(UIManager.getFont("Fillapix.font"));
            FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
            String value = String.valueOf(cell.getNumber());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
        }
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Fillapix.borderWidth")));
        graphics2D.setColor(UIManager.getColor("Fillapix.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
