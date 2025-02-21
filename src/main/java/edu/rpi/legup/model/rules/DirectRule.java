package edu.rpi.legup.model.rules;

import static edu.rpi.legup.model.rules.RuleType.BASIC;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

public abstract class DirectRule extends Rule {
    /**
     * DirectRule Constructor creates a new basic rule.
     *
     * @param ruleID ID of the rule
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public DirectRule(String ruleID, String ruleName, String description, String imageName) {
        super(ruleID, ruleName, description, imageName);
        this.ruleType = BASIC;
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    public String checkRule(TreeTransition transition) {
        Board finalBoard = transition.getBoard();
        // System.out.println(finalBoard.getModifiedData().size());
        if (transition.getParents().size() != 1
                || transition.getParents().get(0).getChildren().size() != 1) {
            return "State must have only 1 parent and 1 child";
        } else if (finalBoard.getModifiedData().isEmpty()) {
            // null transition
            return null;
        } else {
            return checkRuleRaw(transition);
        }
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule. This method is the one that should overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    public String checkRuleRaw(TreeTransition transition) {
        Board finalBoard = transition.getBoard();
        String checkStr = null;

        // Go directly to specific direct rule's judgement if no cell's are edited
        if (finalBoard.getModifiedData().size() == 0) {
            checkStr = checkRuleRawAt(transition, null);
        }
        for (PuzzleElement puzzleElement : finalBoard.getModifiedData()) {
            String tempStr = checkRuleAt(transition, puzzleElement);
            if (tempStr != null) {
                checkStr = tempStr;
            }
        }
        return checkStr;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific {@link
     * PuzzleElement} using this rule.
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement) {
        Board finalBoard = transition.getBoard();
        puzzleElement = finalBoard.getPuzzleElement(puzzleElement);
        String checkStr;
        if (!puzzleElement.isModified()) {
            checkStr = "PuzzleElement must be modified";
        } else {
            if (transition.getParents().size() != 1
                    || transition.getParents().get(0).getChildren().size() != 1) {
                checkStr = "State must have only 1 parent and 1 child";
            } else {
                checkStr = checkRuleRawAt(transition, puzzleElement);
            }
        }
        puzzleElement.setValid(checkStr == null);
        return checkStr;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    public abstract Board getDefaultBoard(TreeNode node);
}
