package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class NoSolutionContradictionRule extends ContradictionRule {

    public NoSolutionContradictionRule() {
        super("No Solution for Cell",
                "Process of elimination yields no valid numbers for an empty cell.",
                "edu/rpi/legup/images/sudoku/NoSolution.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SudokuBoard sudokuBoard = (SudokuBoard) board;
        SudokuCell cell = (SudokuCell) sudokuBoard.getPuzzleElement(puzzleElement);
        if (cell.getData() != 0) {
            return "Does not contain a contradiction at this index";
        }

        int groupSize = sudokuBoard.getSize();

        Set<SudokuCell> region = sudokuBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = sudokuBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = sudokuBoard.getCol(cell.getLocation().x);
        Set<Integer> solution = new HashSet<>();
        for (int i = 1; i <= groupSize; i++) {
            solution.add(i);
        }

        for (SudokuCell c : region) {
            solution.remove(c.getData());
        }
        for (SudokuCell c : row) {
            solution.remove(c.getData());
        }
        for (SudokuCell c : col) {
            solution.remove(c.getData());
        }

        if (solution.isEmpty()) {
            return null;
        }

        return "Does not contain a contradiction at this index";
    }
}
