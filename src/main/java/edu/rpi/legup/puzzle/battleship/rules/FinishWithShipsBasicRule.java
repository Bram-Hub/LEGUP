package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.battleship.BattleshipBoard;
import edu.rpi.legup.puzzle.battleship.BattleshipCell;
import edu.rpi.legup.puzzle.battleship.BattleshipClue;
import edu.rpi.legup.puzzle.battleship.BattleshipType;

import java.awt.*;
import java.util.List;

public class FinishWithShipsBasicRule extends BasicRule {

    public FinishWithShipsBasicRule() {
        super("BTSP-BASC-0002",
                "Finish with Ships",
                "The number of undetermined squares is equal to the number " +
                        "of segments remaining for each clue.",
                "edu/rpi/legup/images/battleship/rules/finishShip.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     * This method is the one that should be overridden in child classes
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return              null if the child node logically follow from the parent node
     *                      at the specified puzzleElement, otherwise error message.
     */
    @Override
    protected String checkRuleRawAt(TreeTransition transition,
                                    PuzzleElement puzzleElement) {
        BattleshipBoard initBoard = (BattleshipBoard) transition.getParents()
                .get(0).getBoard();
        BattleshipCell initCell = (BattleshipCell) initBoard
                .getPuzzleElement(puzzleElement);
        BattleshipBoard finalBoard = (BattleshipBoard) transition.getBoard();
        BattleshipCell finalCell = (BattleshipCell) finalBoard
                .getPuzzleElement(puzzleElement);
        if (!(initCell.getType() == BattleshipType.UNKNOWN
                && BattleshipType.isShip(finalCell.getType())))
            return super.getInvalidUseOfRuleMessage() + ": This cell must be a ship.";

        if (isForced(initBoard, initCell))
            return null;
        else 
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to" +
                    "be a ship segment.";
    }
    
    private boolean isForced(BattleshipBoard board, BattleshipCell cell) {
        Point loc = cell.getLocation();

        // count the number of ship segments and unknowns in the row
        List<BattleshipCell> row = board.getRow(loc.y);
        int rowCount = 0;
        for (BattleshipCell c : row)
            if (c.getType() == BattleshipType.SHIP_UNKNOWN
                || BattleshipType.isShip(c.getType()))
                rowCount++;

        // count the number of ship segments and unknowns in the column
        List<BattleshipCell> col = board.getColumn(loc.x);
        int colCount = 0;
        for (BattleshipCell c : col)
            if (c.getType() == BattleshipType.SHIP_UNKNOWN
                || BattleshipType.isShip(c.getType()))
                colCount++;

        // compare the counts with the clues
        BattleshipClue east = board.getEast().get(loc.y);
        BattleshipClue south = board.getSouth().get(loc.x);

        return rowCount <= east.getData() && colCount <= south.getData();
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the
     * {@link TreeNode}.
     *
     * @param node  tree node used to create default transition board.
     * @return      default board or null if this rule cannot be applied to this tree
     *              node.
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        BattleshipBoard board = (BattleshipBoard) node.getBoard().copy();
        for (PuzzleElement element : board.getPuzzleElements()) {
            BattleshipCell cell = (BattleshipCell) element;
            if (cell.getType() == BattleshipType.UNKNOWN && isForced(board, cell)) {
                cell.setData(BattleshipType.SHIP_UNKNOWN);
                board.addModifiedData(cell);
            }
        }

        if (board.getModifiedData().isEmpty())
            return null;
        else
            return board;
    }
}
