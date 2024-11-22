package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.PuzzleCellFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;

public class YinYangCellFactory extends PuzzleCellFactory {
    @Override
    public PuzzleElement createCell(int data, Object... params) {
        int x = (int) params[0];
        int y = (int) params[1];
        return new YinYangCell(data, x, y);
    }
}