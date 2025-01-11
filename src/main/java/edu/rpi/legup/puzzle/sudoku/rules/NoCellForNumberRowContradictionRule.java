package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;
import java.util.HashSet;
import java.util.Set;

public class NoCellForNumberRowContradictionRule extends ContradictionRule {

    public NoCellForNumberRowContradictionRule() {
        super(
                "SUDO-CONT-0002",
                "No Cell for Number (Row)",
                "Process of elimination yields no valid numbers for an empty cell in a row.",
                "edu/rpi/legup/images/sudoku/rules/NoCellForNumberRow.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using
     * this rule
     *
     * @param board board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     *     otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SudokuBoard sudokuBoard = (SudokuBoard) board;
        SudokuCell cell = (SudokuCell) sudokuBoard.getPuzzleElement(puzzleElement);
        if (cell.getData() != 0) {
            return super.getNoContradictionMessage();
        }

        int groupSize = sudokuBoard.getSize();

        Set<SudokuCell> row = sudokuBoard.getRow(cell.getGroupIndex());
        Set<Integer> numbersNotInRow = new HashSet<>();

        for (int i = 1; i <= groupSize; i++) {
            numbersNotInRow.add(i);
        }
        for (SudokuCell c : row) {
            if (c.getData() != 0) {
                numbersNotInRow.remove(c.getData());
            }
        }

        for (Integer i : numbersNotInRow) {
            // Check if number can be in cell
            boolean canFit = false;
            for (SudokuCell c : row) {
                if (c.getData() != 0) {
                    continue;
                }

                // Get row and col groups
                Set<SudokuCell> region = sudokuBoard.getRow(c.getLocation().y);
                Set<SudokuCell> col = sudokuBoard.getCol(c.getLocation().x);

                // Check if it alr exists in row or col
                boolean duplicate = false;
                for (SudokuCell rc : region) {
                    if (rc.getData() == i) {
                        duplicate = true;
                    }
                }
                for (SudokuCell cc : col) {
                    if (cc.getData() == i) {
                        duplicate = true;
                    }
                }

                // If there is no duplicate it can exist in the region
                if (!duplicate) {
                    canFit = true;
                    break;
                }
            }
            // If the number can't fit anywhere in region then contradiction
            if (!canFit) {
                return null;
            }
        }
        return super.getNoContradictionMessage();
    }
}
