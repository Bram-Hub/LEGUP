package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NonTouchingSharedEmptyDirectRule extends DirectRule {
    public NonTouchingSharedEmptyDirectRule() {
        super(
                "MINE-BASC-0003",
                "Non Shared Empty",
                "Adjacent cells with numbers have the same difference in mine in their unshared\n"
                        + " regions as the difference in their numbers",
                "edu/rpi/legup/images/minesweeper/direct/NonSharedEmpty.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperBoard parentBoard =
                (MinesweeperBoard) transition.getParents().get(0).getBoard().copy();
        MinesweeperBoard parentBoard2 = parentBoard.copy();
        MinesweeperCell cell = (MinesweeperCell) board.getPuzzleElement(puzzleElement);
        MinesweeperCell parentCell = (MinesweeperCell) parentBoard.getPuzzleElement(puzzleElement);
        if (!(parentCell.getTileType() == MinesweeperTileType.UNSET
                && cell.getTileType() == MinesweeperTileType.EMPTY)) {

            return super.getInvalidUseOfRuleMessage()
                    + ": This cell must be empty to be applicable with this rule.";
        }
        if (MinesweeperUtilities.checkBoardForContradiction(board)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be empty";
        }
        // get all adjCells that have a number
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(parentBoard, parentCell);
        adjCells.removeIf(x -> x.getTileNumber() < 1 || x.getTileNumber() >= 9);
        /* remove any number cell that does not have another number cell
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
        // change the cell to be a mine instead of empty
        parentCell.setCellType(MinesweeperTileData.mine());
        // check for some contradiction in all cases
        parentBoard.addModifiedData(parentCell);
        CaseRule completeClue = new SatisfyNumberCaseRule();
        List<Board> caseBoards;
        for (MinesweeperCell adjCell : adjCells) {
            caseBoards = completeClue.getCases(parentBoard, adjCell);
            // System.out.println(adjCell.getLocation().x + " " + adjCell.getLocation().y);
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

        return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be empty";
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
                    && MinesweeperUtilities.isForcedEmpty(
                            (MinesweeperBoard) node.getBoard(), cell)) {
                cell.setCellType(MinesweeperTileData.empty());
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
