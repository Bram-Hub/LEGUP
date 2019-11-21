package rpi.legup.puzzle.shorttruthtable;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;

import java.util.Set;
import java.util.Iterator;


public class ContradictionRuleNegation extends ContradictionRule{


    ContradictionRuleNegation(){
        super("Contradicting Negation",
                "A negation and its following statement can not have the same truth value",
                "image path");
    }


    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

        //cast the board toa shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        //get the cell that contradicts another cell in the board
        ShortTruthTableStatement statement = (ShortTruthTableStatement) board.getPuzzleElement(puzzleElement);
        ShortTruthTableCell cell = statement.getCell();

        //must be a NOT statement
        if(cell.getSymbol() != ShortTruthTableStatement.NOT)
            return "Can not check for negation contradiction on a non-negation element";

        //check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();
        if(!cellType.isTrueOrFalse())
            return "Can only check for a contradiction on a cell that is assigned a value of True or False";

        //check that the statement to the right is assigned
        ShortTruthTableCellType rightCellType = statement.getRightStatement().getCell().getType();
        if(!rightCellType.isTrueOrFalse())
            return "Can only check for a contradiction on a negation cell that has an assigned True or False as a second statement";

        //check if they are a contradiction or not
        if(cellType != rightCellType)
            return "This negation does not contradict";

        return null;

    }

}