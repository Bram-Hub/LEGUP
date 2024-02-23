package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;

public class TooManyMercuryContradiction extends ContradictionRule{

    private final String Invalid_Use_Message = "Mercury does not exceed limit";


    public TooManyMercuryContradiction(){
        super("THERM-CONT-0002", "Too Many Mercury", "More mercury in column/row than target", "edu/rpi/legup/images/thermometer/TooManyMercury.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        ThermometerBoard grid = (ThermometerBoard) board;
        ThermometerCell cell = (ThermometerCell) grid.getPuzzleElement(puzzleElement);
        int filled = 0;
        for(int i = 0; i < grid.getHeight(cell.getLocation().getX()); i++){
            if(grid.getCell((int)cell.getLocation().getX(), i).getFill() == ThermometerFill.FILLED){ filled++;}
        }
        if(grid.getRowNumber(cell.getLocation().getX()) > filled) return null;

        filled = 0;
        for(int i = 0; i < grid.getColumn(cell.getLocation().getY()); i++){
            if(grid.getCell(i, (int)cell.getLocation().getY()).getFill() == ThermometerFill.FILLED){ filled++;}
        }
        if(grid.getColumnNumber(cell.getLocation().getY()) > filled) return null;

        return Invalid_Use_Message;
    }
}
