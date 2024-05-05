package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;

public class FinishWithStarsDirectRule extends DirectRule {

    public FinishWithStarsDirectRule() {
        super(
                "STBL-BASC-0004",
                "Finish With Stars",
                "Unknown spaces must be stars if there are just enough in a row, column, or region to satisfy the puzzle number.",
                "edu/rpi/legup/images/starbattle/rules/FinishWithStarDirectRule.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleBoard origBoard = (StarBattleBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new TooFewStarsContradictionRule();

        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);

        if (cell.getType() != StarBattleCellType.STAR) {
            return "Only star cells are allowed for this rule!";
        }

        StarBattleBoard modified = (StarBattleBoard) origBoard.copy();
        modified.getPuzzleElement(puzzleElement).setData(StarBattleCellType.BLACK.value);
        if (contraRule.checkContradictionAt(modified, puzzleElement) != null) {
            return "Star cells must be placed in a row, region, or column without extra spaces!";
        }
        return null;
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

        return null;
    }
}
