package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class NoNumberForCellContradictionRule extends ContradictionRule {

    public NoNumberForCellContradictionRule() {
        super("SUDO-CONT-0003", "No Number for Cell",
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
            return super.getNoContradictionMessage();
        }

        int groupSize = sudokuBoard.getSize();

        Set<SudokuCell> region = sudokuBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = sudokuBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = sudokuBoard.getCol(cell.getLocation().x);
        Set<Integer> solution = new HashSet<>();
        for(SudokuCell s : region) {
            solution.add(s.getData());
        }
        for(SudokuCell s : row) {
            solution.add(s.getData());
        }

        for(SudokuCell s : col) {
            solution.add(s.getData());
        }
        solution.remove(0);

        if(solution.size() == 9) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
