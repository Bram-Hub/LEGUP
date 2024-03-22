package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.sudoku.Sudoku;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class NoCellForNumberContradictionRule extends ContradictionRule {

    public NoCellForNumberContradictionRule() {
        super("SUDO-CONT-0001", "No Cell for Number (Region)",
                "Process of elimination yields no valid numbers for an empty cell in a region.",
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

        //Loop through every individual region
        //Check if there is a number that cannot exist in region with current board



        SudokuCell cell = (SudokuCell) sudokuBoard.getPuzzleElement(puzzleElement);
        if (cell.getData() != 0) {
            return super.getNoContradictionMessage();
        }

        int groupSize = sudokuBoard.getSize();

        Set<SudokuCell> region = sudokuBoard.getRegion(cell.getGroupIndex());
        Set<Integer> numbersNotInRegion = new HashSet<>();

        for (int i = 1; i <= groupSize; i++) {
            numbersNotInRegion.add(i);
        }
        for (SudokuCell c : region) {
            if(c.getData() != 0) {
                numbersNotInRegion.remove(c.getData());
            }
        }

        for (Integer i : numbersNotInRegion) {
            //Check if number can be in cell
            boolean canFit = false;
            for(SudokuCell c : region){
                if(c.getData() != 0)
                    continue;

                //Get row and col groups
                Set<SudokuCell> row = sudokuBoard.getRow(c.getLocation().y);
                Set<SudokuCell> col = sudokuBoard.getCol(c.getLocation().x);

                //Check if it alr exists in row or col
                boolean duplicate = false;
                for(SudokuCell rc : row) {
                    if(rc.getData() == i)
                        duplicate = true;
                }
                for(SudokuCell cc : col) {
                    if(cc.getData() == i)
                        duplicate = true;
                }

                //If there is no duplicate it can exist in the region
                if(!duplicate) {
                    canFit = true;
                    break;
                }
            }
            //If the number can't fit anywhere in region then contradiction
            if(!canFit)
                return null;
        }
        return super.getNoContradictionMessage();
    }
}
