package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class RepeatedNumberContradictionRule extends ContradictionRule {

    public RepeatedNumberContradictionRule() {
        super("SUDO-CONT-0002", "Repeated Numbers",
                "Two identical numbers are placed in the same group.",
                "edu/rpi/legup/images/sudoku/RepeatedNumber.png");
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
        //Get board to check
        SudokuBoard sudokuBoard = (SudokuBoard) board;

        //Loop all group indexes
        for(int i = 0; i < 9; i++) {
            //Get regions and sets to check duplicates
            Set<SudokuCell> region = sudokuBoard.getRegion(i);
            Set<Integer> regionDup = new HashSet<>();

            Set<SudokuCell> row = sudokuBoard.getRow(i);
            Set<Integer> rowDup = new HashSet<>();

            Set<SudokuCell> col = sudokuBoard.getCol(i);
            Set<Integer> colDup = new HashSet<>();

            //Check for non zero duplicates to trigger contradiction
            for (SudokuCell c : region) {
                if (c.getData() == 0)
                    continue;
                if (regionDup.contains(c.getData()))
                    return null;
                regionDup.add(c.getData());
            }

            for (SudokuCell c : row) {
                if (c.getData() == 0)
                    continue;
                if (rowDup.contains(c.getData()))
                    return null;
                rowDup.add(c.getData());
            }

            for (SudokuCell c : col) {
                if (c.getData() == 0)
                    continue;
                if (colDup.contains(c.getData()))
                    return null;
                colDup.add(c.getData());
            }
        }

        return super.getNoContradictionMessage();
    }
}
