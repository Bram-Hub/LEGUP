package edu.rpi.legup.model.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.rpi.legup.model.rules.RuleType.CASE;

public abstract class CaseRule extends Rule {

    private final String INVALID_USE_MESSAGE;

    /**
     * CaseRule Constructor creates a new case rule.
     *
     * @param ruleID      ID of the rule
     * @param ruleName    name of the rule
     * @param description description of the rule
     * @param imageName   file name of the image
     */
    public CaseRule(String ruleID, String ruleName, String description, String imageName) {
        super(ruleID, ruleName, description, imageName);
        this.ruleType = CASE;
        this.INVALID_USE_MESSAGE = "Invalid use of the case rule " + this.ruleName;
    }

    /**
     * Gets the case board that indicates where this case rule can be applied on the given {@link Board}.
     *
     * @param board board to find locations where this case rule can be applied
     * @return a case board
     */
    public abstract CaseBoard getCaseBoard(Board board);

    /**
     * Gets the possible cases for this {@link Board} at a specific {@link PuzzleElement} based on this case rule.
     *
     * @param board         the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    public abstract List<Board> getCases(Board board, PuzzleElement puzzleElement);

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this rule.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition) {
        List<TreeNode> parentNodes = transition.getParents();
        if (parentNodes.size() != 1) {
            return "Must not have multiple parent nodes";
        }

        /*if (transition.getBoard().getModifiedData().size() != 0){
            return "Should not modify before case rule";
        }*/


        for (TreeTransition childTrans : parentNodes.get(0).getChildren()) {
            if (childTrans.getRule() == null || !childTrans.getRule().getClass().equals(this.getClass())) {
                return "All children nodes must be justified with the same case rule.";
            } else if (childTrans.getBoard().getModifiedData().isEmpty()) {
                return "You must modify the board in each case node";
            }
        }

        String check = checkRuleRaw(transition);

        boolean isCorrect = (check == null);
        for (TreeTransition childTrans : parentNodes.get(0).getChildren()) {
            childTrans.setCorrect(isCorrect);
            for (PuzzleElement element : childTrans.getBoard().getModifiedData()) {
                element.setValid(isCorrect);
            }
        }

        return check;
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this rule. This method is
     * the one that should overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public abstract String checkRuleRaw(TreeTransition transition);

    /**
     * Checks whether the child node logically follows from the parent node at the specific puzzleElement index using
     * this rule.
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return this.INVALID_USE_MESSAGE;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific puzzleElement index using
     * this rule. This method is the one that should overridden in child classes.
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public abstract String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement);
}


