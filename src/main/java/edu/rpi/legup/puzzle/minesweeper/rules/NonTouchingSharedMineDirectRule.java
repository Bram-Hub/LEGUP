package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
//import edu.rpi.legup.puzzle.minesweeper.MinesweeperCellType;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperUtilities;
//import edu.rpi.legup.puzzle.minesweeper.Minesweeper.rules.SatisfyClueCaseRule;
import edu.rpi.legup.puzzle.minesweeper.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NonTouchingSharedMineDirectRule extends DirectRule {
    public NonTouchingSharedMineDirectRule() {
        super(
                "MINE-BASC-0003",
                "Non Shared Mine",
                "Adjacent cells with numbers have the same difference in mine in their unshared\n"
                        + "regions as the difference in their numbers",
                "edu/rpi/legup/images/minesweeper/direct/NonSharedMine.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperBoard parentBoard = (MinesweeperBoard) transition.getParents().get(0).getBoard().copy();
        MinesweeperBoard parentBoard2 = parentBoard.copy();
        MinesweeperCell cell = (MinesweeperCell) board.getPuzzleElement(puzzleElement);
        MinesweeperCell parentCell = (MinesweeperCell) parentBoard.getPuzzleElement(puzzleElement);
        if (!(parentCell.getTileType() == MinesweeperTileType.UNSET
                && cell.getTileType() == MinesweeperTileType.MINE)) {

            return super.getInvalidUseOfRuleMessage()
                    + ": This cell must be a mine to be applicable with this rule.";
        }

        // get all adjCells that have a number
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(parentBoard, parentCell);
        adjCells.removeIf(x -> x.getTileNumber() < 1 || x.getTileNumber() >= 9);
        /* remove any number cell that does not have another number cell diagonally
         * adjacent to it on the opposite side of the modified cell */
        Iterator<MinesweeperCell> itr = adjCells.iterator();
        while (itr.hasNext()) {
            MinesweeperCell adjCell = itr.next();

            boolean found = false;
            ArrayList<MinesweeperCell> adjAdjCells =
                    MinesweeperUtilities.getAdjacentCells(parentBoard, adjCell);
            for (MinesweeperCell adjAdjCell : adjAdjCells) {
                if (adjAdjCell.getTileNumber() >= 1
                        && adjAdjCell.getTileNumber() < 9
                        && adjAdjCell.getIndex() != parentCell.getIndex()) {
                    // adjAdjCell is adjacent to adjCell && it has a
                    // number && it is not parentCell
                    found = true;
                }
            }

            // does not qualify for this rule
            if (!found) {
                itr.remove();
            }
        }
        // change the cell to be empty instead of a mine
        parentCell.setCellType(MinesweeperTileData.empty());
        // check for some contradiction in all cases
        parentBoard.addModifiedData(parentCell);
        CaseRule completeClue = new SatisfyNumberCaseRule();
        List<Board> caseBoards;
        for (MinesweeperCell adjCell : adjCells) {
            caseBoards = completeClue.getCases(parentBoard, adjCell);
            boolean found = true;
            for (Board b : caseBoards) {
                if (!MinesweeperUtilities.checkBoardForContradiction((MinesweeperBoard) b)) {
                    found = false;
                }
            }
            if (found) {
                return null;
            }
        }

        return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be a mine";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) node.getBoard().copy();
        for (PuzzleElement element : minesweeperBoard.getPuzzleElements()) {
            MinesweeperCell cell = (MinesweeperCell) element;
            if (cell.getTileType() == MinesweeperTileType.UNSET
                    && MinesweeperUtilities.isForcedMine(
                            (MinesweeperBoard) node.getBoard(), cell)) {
                cell.setCellType(MinesweeperTileData.mine());
                minesweeperBoard.addModifiedData(cell);
            }
        }
        if (minesweeperBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return minesweeperBoard;
        }
    }
}
