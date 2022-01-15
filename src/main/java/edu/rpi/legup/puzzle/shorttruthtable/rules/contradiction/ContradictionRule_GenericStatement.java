package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;


public abstract class ContradictionRule_GenericStatement extends ContradictionRule{

    private final char operationSymbol;

    private final ShortTruthTableCellType[][] contradictionPatterns;

    final static ShortTruthTableCellType T = ShortTruthTableCellType.TRUE;
    final static ShortTruthTableCellType F = ShortTruthTableCellType.FALSE;
    final static ShortTruthTableCellType n = null;

    private final String NOT_RIGHT_OPERATOR_ERROR_MESSAGE = "This cell does not contain the correct operation";
    private final String NOT_TRUE_FALSE_ERROR_MESSAGE = "Can only check for a contradiction on a cell that is assigned a value of True or False";

    public ContradictionRule_GenericStatement(String ruleName, String description, String imageName,
                                              char operationSymbol, ShortTruthTableCellType[][] contradictionPatterns){
        super(ruleName, description, imageName);
        this.operationSymbol = operationSymbol;
        this.contradictionPatterns = contradictionPatterns;
    }

    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement operatorPuzzleElement) {

        // cast the board to a shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        // get the cell that contradicts another cell in the board
        ShortTruthTableCell cell = board.getCellFromElement(operatorPuzzleElement);
        ShortTruthTableStatement statement = cell.getStatementReference();

        if(cell.getSymbol() != this.operationSymbol)
            return super.getInvalidUseOfRuleMessage() + ": " + this.NOT_RIGHT_OPERATOR_ERROR_MESSAGE;

        // check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();

        if(!cellType.isTrueOrFalse())
            return super.getInvalidUseOfRuleMessage() + ": " + this.NOT_TRUE_FALSE_ERROR_MESSAGE;

        // get the pattern for this sub-statement
        ShortTruthTableCellType[] testPattern = statement.getCellTypePattern();

        // if the board pattern matches any contradiction pattern, it is a valid contradiction
        for(ShortTruthTableCellType[] pattern : contradictionPatterns){
            boolean matches = true;
            for(int i = 0; i < 3; i++){
                // ull means that part does not affect the statement
                if(pattern[i] == null)
                    continue;
                //if it is not null, it must match the test pattern
                if(pattern[i] != testPattern[i]){
                    matches = false;
                    break;
                }
            }
            // if testPattern matches one of the valid contradiction patterns, the contradiction is correct
            if (matches)
                return null;
        }

        return super.getNoContradictionMessage();
    }
}