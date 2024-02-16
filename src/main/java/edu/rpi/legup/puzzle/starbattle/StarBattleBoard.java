package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

public class StarBattleBoard extends GridBoard {
    public StarBattleBoard(int width, int height) {
        super(width, height);
    }

    public StarBattleBoard(int size) {
        super(size, size);
    }

    @Override
    public StarBattleCell getCell(int x, int y) {
        return (StarBattleCell) super.getCell(x,y);
    }
}


