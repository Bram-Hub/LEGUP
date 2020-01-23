package edu.rpi.legup.puzzle.shorttruthtable.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;

import java.util.Set;
import java.util.Iterator;


public class ContradictionRuleConditional extends ContradictionRule{


    public ContradictionRuleConditional(){
        super("Contradicting Conditional Statement",
                "A Conditional statement must not have correct format",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Conditional.png");
    }


    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

        //cast the board toa shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        //get the cell that contradicts another cell in the board
        ShortTruthTableStatement statement = (ShortTruthTableStatement) board.getPuzzleElement(puzzleElement);
        ShortTruthTableCell cell = statement.getCell();

        //must be a NOT statement
        if(cell.getSymbol() != ShortTruthTableStatement.CONDITIONAL)
            return "Can not check for negation contradiction on a non-negation element";

        //check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();
        if(!cellType.isTrueOrFalse())
            return "Can only check for a contradiction on a cell that is assigned a value of True or False";

        //get both sides of the statement
        ShortTruthTableCellType leftCellType = statement.getLeftStatement().getCell().getType();
        ShortTruthTableCellType rightCellType = statement.getRightStatement().getCell().getType();

        if(cellType == ShortTruthTableCellType.FALSE &&
                leftCellType == ShortTruthTableCellType.FALSE){
            return "A false conditional statement can not have false predicate";
        }

        if(cellType == ShortTruthTableCellType.FALSE &&
                rightCellType == ShortTruthTableCellType.TRUE){
            return "A false conditional statement can not have true conclusion";
        }

        if(cellType == ShortTruthTableCellType.TRUE &&
                leftCellType == ShortTruthTableCellType.TRUE &&
                rightCellType == ShortTruthTableCellType.FALSE){
            return "A true conditional statement can not have a true predicate and false conclusion";
        }


        return null;

    }

}