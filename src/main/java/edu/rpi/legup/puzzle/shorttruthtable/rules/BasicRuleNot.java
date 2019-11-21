package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;


import java.awt.*;
import java.util.List;

public class BasicRuleNot extends BasicRule {

    public BasicRuleNot() {
        super("Fill in a not",
                "If one atomic value is known, all can be filled in with that value.",
                "edu/rpi/legup/images/treetent/finishGrass.png");
    }



    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this rule. This method is
     * the one that should overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    public String checkRuleRaw(TreeTransition transition) {

//        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
//        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();


        return null;

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
    /*
        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        ShortTruthTableStatement statement = (ShortTruthTableStatement) destBoardState.getPuzzleElement(puzzleElement);

        if (statement.getCell().getSymbol() != ShortTruthTableStatement.NOT) {
            return "Only NOT statements are allowed for this rule!";
        }

        NurikabeBoard modified = origBoardState.copy();
        ShortTruthTableStatement modStatement = (ShortTruthTableStatement) modified.getPuzzleElement(puzzleElement);
        modStatement.getRightStatement().getCell().setData(ShortTruthTableCellType.TRUE);

        ContradictionRule contraRule = new CantReachWhiteContradictionRule();
        if (contraRule.checkContradiction(modified) == null) {
            return null;
        } else {
            return "This is not the only way for black to escape!";
        }
        */
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
