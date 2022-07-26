package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.battleship.BattleshipBoard;
import edu.rpi.legup.puzzle.battleship.BattleshipCell;
import edu.rpi.legup.puzzle.battleship.BattleshipType;

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
        BattleshipBoard initialBoard = (BattleshipBoard) transition.getParents()
                .get(0).getBoard();
        BattleshipCell initCell = (BattleshipCell) initialBoard
                .getPuzzleElement(puzzleElement);
        BattleshipBoard finalBoard = (BattleshipBoard) transition.getBoard();
        BattleshipCell finalCell = (BattleshipCell) finalBoard
                .getPuzzleElement(puzzleElement);
        if (!(initCell.getType() == BattleshipType.UNKNOWN
                && BattleshipType.isShip(finalCell.getType())))
            return super.getInvalidUseOfRuleMessage() + ": This cell must be a ship.";

        return null;
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
        return null;
    }
}
