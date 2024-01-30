package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

public class ThermometerBoard extends GridBoard{
    public ThermometerBoard(int width, int height){
        super(width, height);
    }

    public ThermometerBoard(int size){
        super(size, size);
    }

    @Override
    public ThermometerCell getCell(int x, int y){
        return (ThermometerCell) super.getCell(x, y);
    }
}
