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

public class UnreachableBasicRule extends BasicRule {
    public UnreachableBasicRule() {
        super("NURI-BASC-0008",
                "Unreachable white region",
                "A cell must be black if it cannot be reached by any white region",
                "edu/rpi/legup/images/nurikabe/rules/Unreachable.png");
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
    protected String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        ContradictionRule contraRule = new CantReachWhiteContradictionRule();

        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = (NurikabeCell) destBoardState.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }

        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();
        NurikabeBoard modified = origBoardState.copy();

        for (int i = 0; i < modified.getWidth(); i++)
            for (int j = 0; j < modified.getHeight(); j++)
            {
                NurikabeCell currentCell = modified.getCell(i, j);
                if (currentCell.getType() == NurikabeType.WHITE)
                    currentCell.setData(NurikabeType.UNKNOWN.toValue());
            }
        NurikabeCell modifiedCell = (NurikabeCell) modified.getPuzzleElement(puzzleElement);
        modifiedCell.setData(NurikabeType.WHITE.toValue());
        if (contraRule.checkContradiction(modified) == null)
            return null;
        return "This is not the only way for black to escape!";
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
