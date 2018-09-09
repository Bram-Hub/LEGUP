package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PossibleCellCaseRule extends CaseRule {
    public PossibleCellCaseRule() {
        super("Possible Cells for Number",
                "A number has a limited set of cells in which it can be placed.",
                "edu/rpi/legup/images/sudoku/possible_cells_number.png");
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        SudokuBoard sudokuBoard = (SudokuBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(sudokuBoard, this);
        for (PuzzleElement puzzleElement : sudokuBoard.getPuzzleElements()) {
            if (((SudokuCell) puzzleElement).getData() == 0) {
                caseBoard.addPickableElement(puzzleElement);
            }
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
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        SudokuBoard sudokuBoard = (SudokuBoard) board;
        SudokuCell cell = (SudokuCell) puzzleElement;

        Set<Integer> possibleValue = new HashSet<>();
        for (int i = 1; i <= sudokuBoard.getSize(); i++) {
            possibleValue.add(i);
        }

        int groupNum = cell.getGroupIndex();
        for (SudokuCell c : sudokuBoard.getRegion(groupNum)) {
            if (c.getData().equals(c.getData())) {
                possibleValue.remove(c.getData());
            }
        }

        int rowNum = cell.getLocation().y;
        for (SudokuCell c : sudokuBoard.getRegion(rowNum)) {
            if (c.getData().equals(c.getData())) {
                possibleValue.remove(c.getData());
            }
        }

        int colNum = cell.getLocation().x;
        for (SudokuCell c : sudokuBoard.getRegion(colNum)) {
            if (c.getData().equals(c.getData())) {
                possibleValue.remove(c.getData());
            }
        }

        for (Integer i : possibleValue) {
            SudokuBoard newCase = sudokuBoard.copy();

            PuzzleElement newCasePuzzleElement = newCase.getPuzzleElement(puzzleElement);
            newCasePuzzleElement.setData(i);
            newCase.addModifiedData(newCasePuzzleElement);
            cases.add(newCase);
        }

        return cases;
    }
}
