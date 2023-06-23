package edu.rpi.legup.puzzle.fillapix.rules;

import java.util.ArrayList;
import java.awt.*;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;

public class CompleteClueCaseRule extends CaseRule {
    public CompleteClueCaseRule() {
        super("FPIX-CASE-0002",
        "Complete Clue",
        "Each clue must touch that number of squares.",
        "edu/rpi/legup/images/fillapix/cases/CompleteClue.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(fillapixBoard, this);
        fillapixBoard.setModifiable(false);
        for (PuzzleElement data : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) data;
            if (cell.getNumber() >= 0 && cell.getNumber() <= 9 && !FillapixUtilities.isComplete(fillapixBoard, cell)) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();

        // get value of cell
        FillapixCell cell = (FillapixCell) board.getPuzzleElement(puzzleElement);
        int cellMaxBlack = cell.getNumber();
        if (cellMaxBlack < 0 || cellMaxBlack > 9) { // cell is not valid cell
            return null;
        }

        // find number of black & empty squares
        int cellNumBlack = 0;
        int cellNumEmpty = 0;
        Point cellLoc = cell.getLocation();
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(cellLoc.x + i, cellLoc.y + j);
                if (adjCell.getType() == FillapixCellType.BLACK) {
                    cellNumBlack++;
                }
                if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                    cellNumEmpty++;
                }
            }
        }

        /* calculate the total number of cases possible
         * 
         * this is the same as having a set the size of the number of empty 
         * cells and choosing the number of missing 
         * black cells (cellMaxBlack - cellNumBlack)
         * 
         * i.e. this is a n-choose-k problem
         * 
         * solution:
         * n! / (k! * (n-k)!)
         */
        int k = cellMaxBlack - cellNumBlack;
        int n = cellNumEmpty;
        if (k <= 0) {
            return null;
        } 
        int numberCases = 1;
        // TODO: IMPLEMENT FINDING CASES

        // do not want to entertain more than 9 cases
        if (numberCases >= 9) {
            return cases;
        }

        // find all cases
        for (int i=0; i < numberCases; i++) {
        }

        return cases;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {




        return null;
    }


    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}