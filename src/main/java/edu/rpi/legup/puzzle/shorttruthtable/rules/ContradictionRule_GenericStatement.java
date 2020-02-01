package edu.rpi.legup.puzzle.shorttruthtable.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;

import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;


public abstract class ContradictionRule_GenericStatement extends ContradictionRule{


    public ContradictionRule_GenericStatement(String ruleName, String description, String imageName){
        super(ruleName, description, imageName);
    }


    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

        //cast the board toa shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        //get the cell that contradicts another cell in the board
        ShortTruthTableCell cell = board.getCellFromElement(puzzleElement);
        ShortTruthTableStatement statement = cell.getStatementRefference();

        //must be a NOT statement
        if(cell.getSymbol() != getOperationSymbol())
            return "This cell does not contain the correct operation";

        //check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();
        System.out.println("contra rule generic cell: "+cell);
        if(!cellType.isTrueOrFalse())
            return "Can only check for a contradiction on a cell that is assigned a value of True or False";

        //get the pattern for this sub-statement
        ShortTruthTableCellType[] testPattern = statement.getCellTypePattern();

        //get all contradiction patterns
        ShortTruthTableCellType[][] contradictionPatterns = getContradictingPatterns();

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
                System.out.println("contrRuleGen: matches pat: "+pattern);
                return null;
            }
        }

        System.out.println("not patterns match");
        return "This cell does not match any contradiction pattern for this rule";

    }

    abstract char getOperationSymbol();

    /**
     * Returns an [n][3] array where each element is the three values that make
     * this type of statement false. If a value does not matter, it is null.
     *
     * @return all patterns that result in a contradiction for an operation type
     */
    abstract ShortTruthTableCellType[][] getContradictingPatterns();

}