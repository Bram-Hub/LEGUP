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
        if(x < 0 || y < 0 || x > dimension.height || y > dimension.width) return null;
        return (ThermometerCell) super.getCell(x, y);
    }
}
