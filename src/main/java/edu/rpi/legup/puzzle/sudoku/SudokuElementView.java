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
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Sudoku.given"));
        g.fillRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Sudoku.background"));
        g.fillRect(location.x, location.y, size.width, size.height);

        SudokuCell cell = (SudokuCell) puzzleElement;
        int val = cell.getData();
        if (val != 0) {
            g.setColor(UIManager.getColor("Sudoku.text"));
            g.setFont(UIManager.getFont("Sudoku.font"));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            String value = String.valueOf(val);
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(value, xText, yText);
        } else {
            boolean annotate = LegupPreferences.showAnnotations();
            if (annotate) {
                g.setColor(UIManager.getColor("Sudoku.text"));
                g.setFont(UIManager.getFont("Sudoku.annotateFont"));
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                String value = String.valueOf(cell.getAnnotations());
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                g.drawString(value, xText, yText);
            }
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Sudoku.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Sudoku.minorBorderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
