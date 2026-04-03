package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class ThermometerNumberView extends GridElementView {

    public ThermometerNumberView(GridCell<Integer> cell) {
        super(cell);
    }

    @Override
    public GridCell getPuzzleElement() {
        return (GridCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        ThermometerCell cell = (ThermometerCell) puzzleElement;

        graphics2D.setColor(UIManager.getColor("Thermometer.text"));
        graphics2D.setFont(UIManager.getFont("Thermometer.font"));
        FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
        int val;

        if (cell != null) val = cell.getRotation();
        else val = -1;

        int xText = location.x + (size.width - metrics.stringWidth(String.valueOf(val))) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics2D.drawString(String.valueOf(val), xText, yText);
    }
}
