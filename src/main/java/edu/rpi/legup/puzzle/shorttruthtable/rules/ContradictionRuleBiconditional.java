package edu.rpi.legup.puzzle.shorttruthtable.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;

import java.awt.Point;

import java.util.Set;
import java.util.Iterator;


public class ContradictionRuleBiconditional extends ContradictionRule{


    public ContradictionRuleBiconditional(){
        super("Contradicting Biconditional Statement",
                "A Biconditional statement must not have correct format",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Biconditional.png");
    }


    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

        //cast the board toa shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        //get the cell that contradicts another cell in the board
        ShortTruthTableCell cell = (ShortTruthTableCell) board.getPuzzleElement(puzzleElement);
        ShortTruthTableStatement statement = cell.getStatementRefference();

        //must be a BICONDITIONAL statement
        if(cell.getSymbol() != ShortTruthTableStatement.BICONDITIONAL)
            return "Can not check for negation contradiction on a non-biconditional element";


        System.out.println(board);

        Point leftPos = statement.getLeftStatement().getCell().getLocation();
        ShortTruthTableCell leftCell = (ShortTruthTableCell) board.getCell((int)leftPos.getX(), (int)leftPos.getY());

        System.out.println("Contradition Rule Biconitional: statement:       "+statement);
        System.out.println("Contradition Rule Biconitional: cell:            "+statement.getCell());
        System.out.println("Contradition Rule Biconitional: cell type:       "+statement.getCell().getType());

        System.out.println("Contradition Rule Biconitional: left statement:  "+statement.getLeftStatement());
        System.out.println("Contradition Rule Biconitional: left cell:       "+statement.getLeftStatement().getCell());
        System.out.println("Contradition Rule Biconitional: left cell type:  "+statement.getLeftStatement().getCell().getType());

        System.out.println("Contradition Rule Biconitional: left2 statement:  "+leftCell.getStatementRefference());
        System.out.println("Contradition Rule Biconitional: left2 cell:       "+leftCell);
        System.out.println("Contradition Rule Biconitional: left2 cell type:  "+leftCell.getType());

        System.out.println("left1=2? "+(statement.getLeftStatement().getCell()==leftCell));

        System.out.println("Contradition Rule Biconitional: right statement: "+statement.getRightStatement());
        System.out.println("Contradition Rule Biconitional: right cell:      "+statement.getRightStatement().getCell());
        System.out.println("Contradition Rule Biconitional: right cell type: "+statement.getRightStatement().getCell().getType());

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