package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;

public class TooFewMercuryContradiction extends ContradictionRule{

    private final String Invalid_Use_Message = "Mercury can still reach limit";


    public TooFewMercuryContradiction(){
        super("THERM-CONT-0002", "Too Many Mercury", "More mercury in column/row than target", "edu/rpi/legup/images/thermometer/TooManyMercury.png");
    }

    @Override
    //Checks if row or column of input element has too many blocked tiles
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        ThermometerBoard grid = (ThermometerBoard) board;
        ThermometerCell cell = (ThermometerCell) grid.getPuzzleElement(puzzleElement);
        int blocked = 0;
        for(int i = 0; i < grid.getHeight(cell.getLocation().getX()); i++){
            if(grid.getCell((int)cell.getLocation().getX(), i).getFill() == ThermometerFill.BLOCKED){ blocked++;}
        }
        if(grid.getRowNumber(cell.getLocation().getX()) > blocked) return null;

        blocked = 0;
        for(int i = 0; i < grid.getColumn(cell.getLocation().getY()); i++){
            if(grid.getCell(i, (int)cell.getLocation().getY()).getFill() == ThermometerFill.BLOCKED){ blocked++;}
        }
        if(grid.getColumnNumber(cell.getLocation().getY()) > blocked) return null;

        return Invalid_Use_Message;
    }
}