package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.PuzzleElementView;
import java.awt.*;

public class YinYangElementView extends PuzzleElementView {
    public YinYangElementView(YinYangCell cell) {
        super(cell);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        YinYangCell cell = (YinYangCell) getElement();

        if (cell.getType() == YinYangType.WHITE) {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, getWidth(), getHeight());
        } else if (cell.getType() == YinYangType.BLACK) {
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
    }
}
