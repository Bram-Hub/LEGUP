package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;


import java.util.ArrayList;
import java.util.List;

public abstract class CaseRule_GenericStatement extends CaseRule_Generic {

    public CaseRule_GenericStatement(char operation, String title,
                                     ShortTruthTableCellType[][] trueCases,
                                     ShortTruthTableCellType[][] falseCases) {
        super(ShortTruthTableOperation.getRuleName(operation),
                title+" case",
                "A known "+title.toUpperCase()+" statment can have multiple forms");

        this.operation = operation;

        this.trueCases = trueCases;
        this.falseCases = falseCases;
    }

    private final char operation;

    private final ShortTruthTableCellType[][] trueCases;
    private final ShortTruthTableCellType[][] falseCases;

    protected static final ShortTruthTableCellType T = ShortTruthTableCellType.TRUE;
    protected static final ShortTruthTableCellType F = ShortTruthTableCellType.FALSE;
    protected static final ShortTruthTableCellType N = null;

    //Adds all elements that can be selected for this caserule
    @Override
    public CaseBoard getCaseBoard(Board board) {
        //copy the board and add all ements that can be selected
        ShortTruthTableBoard sttBoard = (ShortTruthTableBoard) board.copy();
        sttBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(sttBoard, this);

        //add all elements that can be selected for the case rule statement
        for (PuzzleElement element : sttBoard.getPuzzleElements()) {

            System.out.println("GetCaseBoard Testing: "+element);

            //get the cell object
            ShortTruthTableCell cell = sttBoard.getCellFromElement(element);
            //the cell must match the symbol
            if(cell.getSymbol() != this.operation) continue;

            System.out.println("  Selectable... checking logic");

            //the statement must be assigned with unassigned sub-statements
            if(!cell.getType().isTrueOrFalse()) continue;
            System.out.println("  Operation is known");
            if(cell.getStatementRefference().getRightStatement().getCell().getType().isTrueOrFalse()) continue;
            System.out.println("  right side is unknown");
            if(this.operation != ShortTruthTableOperation.NOT &&
                    cell.getStatementRefference().getRightStatement().getCell().getType().isTrueOrFalse()) continue;
            System.out.println("  left side is unknown");

            System.out.println("    Valid choice");
            //if the element has passed all the checks, it can be selected
            caseBoard.addPickableElement(element);

        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board         the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {

        ShortTruthTableBoard sttBoard = ((ShortTruthTableBoard) board);

        ShortTruthTableCell cell = sttBoard.getCellFromElement(puzzleElement);

        //if the statement is set to true
        if(cell.getType() == ShortTruthTableCellType.TRUE)
            return getCasesFromCell(sttBoard, puzzleElement, trueCases);

        //if the statement is set to false
        return getCasesFromCell(sttBoard, puzzleElement, falseCases);

    }

    private ArrayList<Board> getCasesFromCell(ShortTruthTableBoard board, PuzzleElement puzzleElement, ShortTruthTableCellType[][] possibilities){

        //store all possible boards
        ArrayList<Board> cases = new ArrayList<>();

        //go through all the possibilities
        for(int i = 0; i<possibilities.length; i++){
            //create a new board
            ShortTruthTableBoard b = board.copy();

            //get the statement of the square that was selected
            ShortTruthTableCell cell = b.getCellFromElement(puzzleElement);
            ShortTruthTableStatement statement = cell.getStatementRefference();

            //modify its children
            //avoid error if it is a NOT statement
            if(possibilities[i][0] != null){
                ShortTruthTableCell leftCell = statement.getLeftStatement().getCell();
                leftCell.setData(possibilities[i][0]);
                b.addModifiedData(leftCell);
            }

            //always modify the right side of the statement
            if(possibilities[i][1] != null) {
                ShortTruthTableCell rightCell = statement.getRightStatement().getCell();
                rightCell.setData(possibilities[i][1]);
                b.addModifiedData(rightCell);
            }

            //add the board possibility to the list
            cases.add(b);
        }

        //return all possibilities
        return cases;

    }

}
