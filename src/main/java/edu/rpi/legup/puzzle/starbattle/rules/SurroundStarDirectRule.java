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

public class SurroundStarDirectRule extends DirectRule {

    public SurroundStarDirectRule() {
        super("STBL-BASC-0005",
                "Surround Star",
                "Any space adjacent to a star must be black.",
                "INSERT IMAGE NAME HERE");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleBoard origBoard = (StarBattleBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ClashingOrbitContradictionRule();

        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);

        if (cell.getType() != StarBattleCellType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }

        StarBattleBoard modified = (StarBattleBoard) origBoard.copy();
        //TODO: please for the love of god make a copy method for star battle board because this isn't actually going to work otherwise
        modified.getPuzzleElement(puzzleElement).setData(StarBattleCellType.STAR);
        if (contraRule.checkContradictionAt(modified, puzzleElement) != null) {
            return "Black cells must be placed adjacent to a star!";
        }
        return null;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {

        return null;
    }
}

