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
    public void draw(Graphics2D graphics2D) {
        if (((ShortTruthTableCell) puzzleElement).getData() != ShortTruthTableCellType.NOT_IN_PLAY) {
            drawElement(graphics2D);
            if (puzzleElement.isGiven()) {
                drawGiven(graphics2D);
            }
            if (puzzleElement.isModified()) {
                drawModified(graphics2D);
            }
            if (isShowCasePicker() && isCaseRulePickable()) {
                drawCase(graphics2D);
            }
            if (isHover()) {
                drawHover(graphics2D);
            }
        }
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        ShortTruthTableCell cell = (ShortTruthTableCell) puzzleElement;

        // Stop drawing if cell is not in play (functions like getImage call drawElement directly)
        if (cell.getData() == ShortTruthTableCellType.NOT_IN_PLAY) {
            return;
        }

        // Fill in background color of the cell
        g.setColor(UIManager.getColor(
                switch (cell.getData()) {
                    case TRUE -> "ShortTruthTable.true";
                    case FALSE -> "ShortTruthTable.false";
                    default -> "ShortTruthTable.unknown";
                }));
        g.fillRect(location.x, location.y, size.width, size.height);

        // Draw the symbol on the cell
        g.setColor(UIManager.getColor("ShortTruthTable.text"));
        g.setFont(UIManager.getFont("ShortTruthTable.font"));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String value = String.valueOf(cell.getSymbol());
        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(ShortTruthTableOperation.getLogicSymbol(cell.getSymbol()), xText, yText);

        g.dispose();
    }
}
