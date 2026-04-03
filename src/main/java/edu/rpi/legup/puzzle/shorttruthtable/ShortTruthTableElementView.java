package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class ShortTruthTableElementView extends GridElementView {

    public ShortTruthTableElementView(ShortTruthTableCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public ShortTruthTableCell getPuzzleElement() {
        return (ShortTruthTableCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {

        // get information about the cell
        ShortTruthTableCell cell = (ShortTruthTableCell) puzzleElement;
        ShortTruthTableCellType type = cell.getData();

        // do not draw the cell if it is not in play
        if (type == ShortTruthTableCellType.NOT_IN_PLAY) return;

        // fill in background color of the cell
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(UIManager.getColor(switch (type) {
            case TRUE -> "ShortTruthTable.true";
            case FALSE -> "ShortTruthTable.false";
            default -> "ShortTruthTable.unknown";
        }));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        // Draw the symbol on the cell
        graphics2D.setColor(UIManager.getColor("ShortTruthTable.text"));
        graphics2D.setFont(UIManager.getFont("ShortTruthTable.font"));
        FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
        String value = String.valueOf(cell.getSymbol());
        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(
                ShortTruthTableOperation.getLogicSymbol(cell.getSymbol()), xText, yText);
    }
}
