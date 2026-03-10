package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SatisfyNumberCaseRule extends CaseRule {
    public SatisfyNumberCaseRule() {
        super(
                "MINE-CASE-0002",
                "Satisfy Number",
                "Create a different path for each valid way to mark mines and empty cells around a number cell",
                "edu/rpi/legup/images/minesweeper/cases/SatisfyNumber.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(minesweeperBoard, this);
        minesweeperBoard.setModifiable(false);
        for (PuzzleElement data : minesweeperBoard.getPuzzleElements()) {
            MinesweeperCell cell = (MinesweeperCell) data;
            if (cell.getTileNumber() > 0
                    && cell.getTileNumber() <= 8
                    && MinesweeperUtilities.hasEmptyAdjacent(minesweeperBoard, cell)) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<Board>();

        // get value of cell
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board.copy();
        MinesweeperCell cell = (MinesweeperCell) minesweeperBoard.getPuzzleElement(puzzleElement);
        int cellMaxBlack = cell.getTileNumber();
        if (cellMaxBlack <= 0 || cellMaxBlack > 8) { // cell is not valid cell
            return null;
        }

        // find number of black & unset squares
        int cellNummine = 0;
        int cellNumUnset = 0;
        ArrayList<MinesweeperCell> unsetCells = new ArrayList<MinesweeperCell>();
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if (adjCell.getTileType() == MinesweeperTileType.MINE) {
                cellNummine++;
            }
            if (adjCell.getTileType() == MinesweeperTileType.UNSET) {
                cellNumUnset++;
                unsetCells.add(adjCell);
            }
        }
        // no cases if no empty or if too many black already
        if (cellNummine >= cellMaxBlack || cellNumUnset == 0) {
            return cases;
        }

        // generate all cases as boolean expressions
        ArrayList<boolean[]> combinations;
        combinations =
                MinesweeperUtilities.getCombinations(cellMaxBlack - cellNummine, cellNumUnset);

        for (int i = 0; i < combinations.size(); i++) {
            Board case_ = board.copy();
            for (int j = 0; j < combinations.get(i).length; j++) {
                cell = (MinesweeperCell) case_.getPuzzleElement(unsetCells.get(j));
                if (combinations.get(i)[j]) {
                    cell.setCellType(MinesweeperTileData.mine());
                } else {
                    cell.setCellType(MinesweeperTileData.empty());
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
