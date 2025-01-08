package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;

public class NonTouchingSharedMineDirectRule extends DirectRule {
    public NonTouchingSharedMineDirectRule() {
        super(
                "MINE-BASC-0003",
                "Non Shared Mine",
                "Adjacent cells with numbers have the same difference in mine in their unshared\n" +
                        "regions as the difference in their numbers",
                "edu/rpi/legup/images/minesweeper/direct/NonSharedMine.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperBoard parentBoard = (MinesweeperBoard) transition.getParents().get(0).getBoard();
        MinesweeperCell cell = (MinesweeperCell) board.getPuzzleElement(puzzleElement);
        MinesweeperCell parentCell = (MinesweeperCell) parentBoard.getPuzzleElement(puzzleElement);

        if (!(parentCell.getTileType() == MinesweeperTileType.UNSET
                && cell.getTileType() == MinesweeperTileType.MINE)) {

            return super.getInvalidUseOfRuleMessage()
                    + ": This cell must be a mine to be applicable with this rule.";
        }

        if (MinesweeperUtilities.nonTouchingSharedIsMine(parentBoard, cell)) {
            return null;
        } else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be a mine";
        }
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
