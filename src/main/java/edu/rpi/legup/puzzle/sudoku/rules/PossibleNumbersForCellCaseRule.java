package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.*;
import java.util.ArrayList;
import java.util.List;

public class PossibleNumbersForCellCaseRule extends CaseRule {

    public PossibleNumbersForCellCaseRule() {
        super(
                "SUDO-CASE-0001",
                "Possible Numbers for Cell",
                "An empty cell has a limited set of possible numbers that can fill it.",
                "edu/rpi/legup/images/sudoku/rules/PossibleValues.png");
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
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
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
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        return getCases(board, puzzleElement, 1, GroupType.REGION);
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @param value value that the rule will be applied from
     * @param groupType group type
     * @return a list of elements the specified could be
     */
    public ArrayList<Board> getCases(
            Board board, PuzzleElement puzzleElement, int value, GroupType groupType) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        SudokuBoard sudokuBoard = (SudokuBoard) board;
        List<SudokuCell> caseCells = new ArrayList<>();
        SudokuCell cell = (SudokuCell) puzzleElement;

        for (int i = 1; i <= 9; i++) {
            Board newCase = sudokuBoard.copy();
            PuzzleElement element = newCase.getPuzzleElement(puzzleElement);
            element.setData(i);
            newCase.addModifiedData(element);
            cases.add(newCase);
        }

        return cases;
    }
}
