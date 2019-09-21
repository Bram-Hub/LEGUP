package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;

import java.util.LinkedHashSet;
import java.util.Set;

public class WhiteBottleNeckBasicRule extends BasicRule {

    public WhiteBottleNeckBasicRule() {
        super("White Bottle Neck",
                "If a region needs more whites and there is only one path for the region to expand, then those unknowns must be white.", "edu/rpi/legup/images/nurikabe/rules/OneUnknownWhite.png");
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
        Set<ContradictionRule> contras = new LinkedHashSet<>();
        contras.add(new NoNumberContradictionRule());
        contras.add(new TooFewSpacesContradictionRule());

        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        NurikabeCell cell = (NurikabeCell) destBoardState.getPuzzleElement(puzzleElement);

        if (cell.getType() != NurikabeType.WHITE) {
            return "Only white cells are allowed for this rule!";
        }
        NurikabeBoard modified = origBoardState.copy();
        NurikabeCell modCell = (NurikabeCell) modified.getPuzzleElement(puzzleElement);
        modCell.setData(NurikabeType.BLACK.toValue());

        for (ContradictionRule contraRule : contras) {
            if (contraRule.checkContradiction(modified) == null) {
                return null;
            }
        }
        return "This is not the only way for white to escape!";
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
