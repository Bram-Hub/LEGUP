package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridBoard;

public class BattleShipBoard extends GridBoard {

    // PRETTY SURE WIDTH AND HEIGHT ARE ALWAYS GOING TO BE 10
    // ACCORDING TO THE RULES OF BATTLESHIP
    public  BattleShipBoard(int width, int height) {
        super(width, height);
    }

    public BattleShipBoard(int size) {
        super(size,size);
    }

    @Override
    public BattleShipCell getCell(int x, int y) {
        return (BattleShipCell) super.getCell(x, y);
    }
}