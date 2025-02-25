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

public class BlackoutDirectRule extends DirectRule {

    public BlackoutDirectRule() {
        super(
                "STBL-BASC-0001",
                "Blackout",
                "If a row, column, or region has enough stars, its unknown spaces are black.",
                "edu/rpi/legup/images/starbattle/empty/BlackOutDirectRule.png");
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
        ContradictionRule contraRule = new TooManyStarsContradictionRule();

        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);

        if (cell.getType() != StarBattleCellType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }

        StarBattleBoard modified = (StarBattleBoard) origBoard.copy();
        modified.getPuzzleElement(puzzleElement).setData(StarBattleCellType.STAR.value);
        if (contraRule.checkContradictionAt(modified, puzzleElement) != null) {
            return "Black cells must be placed in a row, region, or column with enough stars!";
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
