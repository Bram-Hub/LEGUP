package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

import java.util.List;
import java.util.ArrayList;

public abstract class CaseRule_GenericStatement extends CaseRule_Generic {

    public CaseRule_GenericStatement(String ruleID, char operation, String title,
                                     ShortTruthTableCellType[][] trueCases,
                                     ShortTruthTableCellType[][] falseCases) {
        super(ruleID, ShortTruthTableOperation.getRuleName(operation),
                title + " case",
                "A known " + title.toUpperCase() + " statement can have multiple forms");

        this.operation = operation;

        this.trueCases = trueCases;
        this.falseCases = falseCases;
    }

    private final char operation;

    private final ShortTruthTableCellType[][] trueCases;
    private final ShortTruthTableCellType[][] falseCases;

    protected static final ShortTruthTableCellType T = ShortTruthTableCellType.TRUE;
    protected static final ShortTruthTableCellType F = ShortTruthTableCellType.FALSE;
    protected static final ShortTruthTableCellType U = ShortTruthTableCellType.UNKNOWN;

    // Adds all elements that can be selected for this caserule
    @Override
    public CaseBoard getCaseBoard(Board board) {
        //copy the board and add all elements that can be selected
        ShortTruthTableBoard sttBoard = (ShortTruthTableBoard) board.copy();
        sttBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(sttBoard, this);

        //add all elements that can be selected for the case rule statement
        for (PuzzleElement element : sttBoard.getPuzzleElements()) {
            //get the cell object
            ShortTruthTableCell cell = sttBoard.getCellFromElement(element);
            //the cell must match the symbol
            if (cell.getSymbol() != this.operation) continue;
            //the statement must be assigned with unassigned sub-statements
            if (!cell.getType().isTrueOrFalse()) continue;
            if (cell.getStatementReference().getRightStatement().getCell().getType().isTrueOrFalse()) continue;
            if (this.operation != ShortTruthTableOperation.NOT &&
                    cell.getStatementReference().getRightStatement().getCell().getType().isTrueOrFalse()) {
                continue;
            }
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

        // If the statement is set to true, collect true cases. Otherwise, collect the false cases
        if (cell.getType() == ShortTruthTableCellType.TRUE) {
            return getCasesFromCell(sttBoard, puzzleElement, trueCases);
        }
        return getCasesFromCell(sttBoard, puzzleElement, falseCases);
    }

    /**
     * Collects a list of boards for each possible outcome of case-rule application
     * @param board current board state
     * @param puzzleElement case rule operator
     * @param possibilities list of possibilities for operator state
     * @return ArrayList of Boards
     */
    private ArrayList<Board> getCasesFromCell(ShortTruthTableBoard board, PuzzleElement puzzleElement, ShortTruthTableCellType[][] possibilities) {
        // Create branch case for each possibility
        ArrayList<Board> cases = new ArrayList<>();
        for (int i = 0; i < possibilities.length; i++) {
            // Create a new board to modify and get statement of selected square
            ShortTruthTableBoard b = board.copy();
            ShortTruthTableCell cell = b.getCellFromElement(puzzleElement);
            ShortTruthTableStatement statement = cell.getStatementReference();

            // Modify neighboring cells of case-rule application by the provided logical cases
            if (possibilities[i][0] != ShortTruthTableCellType.UNKNOWN) {
                ShortTruthTableCell leftCell = statement.getLeftStatement().getCell();
                leftCell.setData(possibilities[i][0]);
                b.addModifiedData(leftCell);
            }
            if (possibilities[i][1] != ShortTruthTableCellType.UNKNOWN) {
                ShortTruthTableCell rightCell = statement.getRightStatement().getCell();
                rightCell.setData(possibilities[i][1]);
                b.addModifiedData(rightCell);
            }

            cases.add(b);
        }
        return cases;
    }
  
  /**
     * Returns the elements necessary for the cases returned by getCases(board,puzzleElement) to be valid
     * Overridden by case rules dependent on more than just the modified data
     *
     * @param board         board state at application
     * @param puzzleElement selected puzzleElement
     * @return List of puzzle elements (typically cells) this application of the case rule depends upon.
     * Defaults to any element modified by any case
     */
    @Override
    public List<PuzzleElement> dependentElements(Board board, PuzzleElement puzzleElement){
        List<PuzzleElement> elements = super.dependentElements(board,puzzleElement);

        elements.add(board.getPuzzleElement(puzzleElement));

        return elements;
    }
}
