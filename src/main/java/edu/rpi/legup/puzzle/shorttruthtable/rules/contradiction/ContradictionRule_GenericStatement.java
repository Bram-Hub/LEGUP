package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;


public abstract class ContradictionRule_GenericStatement extends ContradictionRule{

    private final char operationSymbol;

    private final ShortTruthTableCellType[][] contradictionPatterns;


    final static ShortTruthTableCellType T = ShortTruthTableCellType.TRUE;
    final static ShortTruthTableCellType F = ShortTruthTableCellType.FALSE;
    final static ShortTruthTableCellType n = null;

    public ContradictionRule_GenericStatement(String ruleName, String description, String imageName,
                                              char operationSymbol, ShortTruthTableCellType[][] contradictionPatterns){
        super(ruleName, description, imageName);
        this.operationSymbol = operationSymbol;
        this.contradictionPatterns = contradictionPatterns;
    }


    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

        //cast the board toa shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        //get the cell that contradicts another cell in the board
        ShortTruthTableCell cell = board.getCellFromElement(puzzleElement);
        ShortTruthTableStatement statement = cell.getStatementRefference();

        //must be the correct statement
        if(cell.getSymbol() != this.operationSymbol)
            return "This cell does not contain the correct operation";

        //check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();
        System.out.println("contra rule generic cell: "+cell);
        if(!cellType.isTrueOrFalse())
            return "Can only check for a contradiction on a cell that is assigned a value of True or False";

        //get the pattern for this sub-statement
        ShortTruthTableCellType[] testPattern = statement.getCellTypePattern();

        //if the board pattern matches any contradiction patter, it is a valid contradiction
        System.out.println("Testing pattern: "+Arrays.toString(testPattern));
        for(ShortTruthTableCellType[] pattern : contradictionPatterns){
            System.out.println("compareing to: "+Arrays.toString(pattern));
            boolean matches = true;
            for(int i = 0; i<3; i++){
                //null means that part does not affect the statement
                if(pattern[i] == null) continue;
                //if it is not null, it must match the test pattern
                if(pattern[i] != testPattern[i]){
                    matches = false;
                    break;
                }
            }
            //if testPattern matches one of the valid contradiction patterns, the contradiction is correct
            if(matches){
                System.out.println("This is a valid contradiction: matches pat: "+pattern);
                return null;
            }
        }

        System.out.println("not patterns match. There is not a contradiction");
        return "This cell does not match any contradiction pattern for this rule";

    }

}