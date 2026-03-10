package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SatisfyClueCaseRule extends CaseRule {
    public SatisfyClueCaseRule() {
        super(
                "FPIX-CASE-0002",
                "Satisfy Clue",
                "Each clue must touch that number of squares.",
                "edu/rpi/legup/images/fillapix/cases/SatisfyClue.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(fillapixBoard, this);
        fillapixBoard.setModifiable(false);
        for (PuzzleElement data : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) data;
            if (cell.getNumber() >= 0
                    && cell.getNumber() <= 9
                    && FillapixUtilities.hasEmptyAdjacent(fillapixBoard, cell)) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<Board>();
        if (puzzleElement == null) {
            return cases;
        }

        // get value of cell
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        FillapixCell cell = (FillapixCell) fillapixBoard.getPuzzleElement(puzzleElement);
        int cellMaxBlack = cell.getNumber();
        if (cellMaxBlack < 0 || cellMaxBlack > 9) { // cell is not valid cell
            return null;
        }

        // find number of black & empty squares
        int cellNumBlack = 0;
        int cellNumEmpty = 0;
        ArrayList<FillapixCell> emptyCells = new ArrayList<FillapixCell>();
        ArrayList<FillapixCell> adjCells = FillapixUtilities.getAdjacentCells(fillapixBoard, cell);
        for (FillapixCell adjCell : adjCells) {
            if (adjCell.getType() == FillapixCellType.BLACK) {
                cellNumBlack++;
            }
            if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                cellNumEmpty++;
                emptyCells.add(adjCell);
            }
        }
        // no cases if no empty or if too many black already
        if (cellNumBlack > cellMaxBlack || cellNumEmpty == 0) {
            return cases;
        }

        // generate all cases as boolean expressions
        ArrayList<boolean[]> combinations;
        combinations = FillapixUtilities.getCombinations(cellMaxBlack - cellNumBlack, cellNumEmpty);

        for (int i = 0; i < combinations.size(); i++) {
            Board case_ = board.copy();
            for (int j = 0; j < combinations.get(i).length; j++) {
                cell = (FillapixCell) case_.getPuzzleElement(emptyCells.get(j));
                if (combinations.get(i)[j]) {
                    cell.setCellType(FillapixCellType.BLACK);
                } else {
                    cell.setCellType(FillapixCellType.WHITE);
                }
                case_.addModifiedData(cell);
            }
            cases.add(case_);
        }

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
