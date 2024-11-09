package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;

public class MustContainBombDirectRule extends DirectRule {
    public MustContainBombDirectRule() {
        super(
                "MINE-BASC-0001",
                "Must Contain Bomb",
                "The only way for the flags around this cell to be satisfied is for this cell to contain a bomb",
                "edu/rpi/legup/images/minesweeper/direct/MustBeBomb.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperBoard parentBoard = (MinesweeperBoard) transition.getParents().get(0).getBoard();
        MinesweeperCell cell = (MinesweeperCell) board.getPuzzleElement(puzzleElement);
        MinesweeperCell parentCell = (MinesweeperCell) parentBoard.getPuzzleElement(puzzleElement);

        if (!(parentCell.getTileType() == MinesweeperTileType.UNSET
                && cell.getTileType() == MinesweeperTileType.BOMB)) {

            return super.getInvalidUseOfRuleMessage()
                    + ": This cell must be a bomb to be applicable with this rule.";
        }

        if (MinesweeperUtilities.isForcedBomb(parentBoard, cell)) {
            return null;
        } else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be a bomb";
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
                    && MinesweeperUtilities.isForcedBomb(
                            (MinesweeperBoard) node.getBoard(), cell)) {
                cell.setCellType(MinesweeperTileData.bomb());
                cell.setModifiable(false);
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
