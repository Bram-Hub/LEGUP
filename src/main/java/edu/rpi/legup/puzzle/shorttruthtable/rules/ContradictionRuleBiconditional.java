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


public class ContradictionRuleBiconditional extends ContradictionRule{


    ContradictionRuleBiconditional(){
        super("Contradicting Biconditional Statement",
                "A Biconditional statement must not have correct format",
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
        if(cell.getSymbol() != ShortTruthTableStatement.BICONDITIONAL)
            return "Can not check for negation contradiction on a non-biconditional element";

        //check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();
        if(!cellType.isTrueOrFalse())
            return "Can only check for a contradiction on a cell that is assigned a value of True or False";

        //get both sides of the statement
        ShortTruthTableCellType leftCellType = statement.getLeftStatement().getCell().getType();
        ShortTruthTableCellType rightCellType = statement.getRightStatement().getCell().getType();

        if(leftCellType.isTrueOrFalse() || rightCellType.isTrueOrFalse())
            return "Both sides of a biconditional must have assigned true/false valuse";

        if(cellType == ShortTruthTableCellType.FALSE &&
                leftCellType == rightCellType){
            return "A false conditional statement can not have equivilant sub-statements";
        }

        if(cellType == ShortTruthTableCellType.TRUE &&
                leftCellType != rightCellType){
            return "A true conditional statement must have equivilant sub-statements";
        }


        return null;

    }

}