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
    /**
     * CaseRule Constructor creates a new case rule.
     *
     * @param ruleName    name of the rule
     * @param description description of the rule
     * @param imageName   file name of the image
     */
    public CaseRule(String ruleName, String description, String imageName) {
        super(ruleName, description, imageName);
        this.ruleType = CASE;
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
        TreeNode parentNode = parentNodes.get(0);
        Map<TreeTransition, Boolean> hasCase = new HashMap<>();

        parentNode.getChildren().forEach(c -> hasCase.put(c, false));

        for (TreeTransition c : parentNode.getChildren()) {
            if (hasCase.get(c) != null) {
                hasCase.put(c, true);
            } else {
                return "Invalid case";
            }
        }

        for (TreeTransition c : parentNode.getChildren()) {
            if (hasCase.get(c) != null) {
                hasCase.put(c, true);
            } else {
                return "Invalid case";
            }
        }

        return null;
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this rule. This method is
     * the one that should overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

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
        return null;
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
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}


