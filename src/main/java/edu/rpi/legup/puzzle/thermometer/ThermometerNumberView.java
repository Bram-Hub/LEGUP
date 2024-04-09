package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.ui.boardview.GridElementView;
import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;


public class ThermometerNumberView extends GridElementView {
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public ThermometerNumberView(GridCell<Integer> cell) {
        super(cell);
    }
    @Override
    public GridCell getPuzzleElement() { return  (GridCell) super.getPuzzleElement();}

    @Override
    public void drawElement(Graphics2D graphics2D) {
        GridCell cell = (GridCell) puzzleElement;

        graphics2D.setColor(FONT_COLOR);
        graphics2D.setFont(FONT);
        FontMetrics metrics = graphics2D.getFontMetrics(FONT);
        int val = (int) cell.getData();

        int xText = location.x + (size.width - metrics.stringWidth(String.valueOf(val))) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics2D.drawString(String.valueOf(val), xText, yText);
    }
}
