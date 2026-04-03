package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class SudokuElementView extends GridElementView {

    public SudokuElementView(GridCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public SudokuCell getPuzzleElement() {
        return (SudokuCell) super.getPuzzleElement();
    }

    @Override
    public void drawGiven(Graphics2D graphics2D) {
        graphics2D.setColor(UIManager.getColor("Sudoku.given"));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setColor(UIManager.getColor("Sudoku.background"));
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        SudokuCell cell = (SudokuCell) puzzleElement;
        int val = cell.getData();
        if (val != 0) {
            graphics2D.setColor(UIManager.getColor("Sudoku.text"));
            graphics2D.setFont(UIManager.getFont("Sudoku.font"));
            FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
            String value = String.valueOf(val);
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
        } else {
            boolean annotate = LegupPreferences.showAnnotations();
            if (annotate) {
                graphics2D.setColor(UIManager.getColor("Sudoku.text"));
                graphics2D.setFont(UIManager.getFont("Sudoku.annotateFont"));
                FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
                String value = String.valueOf(cell.getAnnotations());
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                graphics2D.drawString(value, xText, yText);
            }
        }

        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Sudoku.minorBorderWidth")));
        graphics2D.setColor(UIManager.getColor("Sudoku.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
