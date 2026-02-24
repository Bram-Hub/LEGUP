package edu.rpi.legup.model.rules;

import static edu.rpi.legup.model.rules.RuleType.CONTRADICTION;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeTransition;

/**
 * ContradictionRule is an abstract class representing a rule that identifies contradictions in a
 * puzzle. It provides methods to check for contradictions both globally and at specific puzzle
 * elements.
 */
public abstract class ContradictionRule extends Rule {

    private final String NO_CONTRADICTION_MESSAGE =
            "No instance of the contradiction " + this.ruleName + " here";

    /**
     * ContradictionRule Constructor creates a new contradiction rule
     *
     * @param ruleID ID of the rule
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public ContradictionRule(String ruleID, String ruleName, String description, String imageName) {
        super(ruleID, ruleName, description, imageName);
        ruleType = CONTRADICTION;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition) {
        return checkContradiction(transition.getBoard());
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
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkContradictionAt(transition.getBoard(), puzzleElement);
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule. This
     * method is the one that should be overridden in child classes
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return checkContradiction(transition.getBoard());
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule This method is the one that should be overridden in child
     * classes
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkContradictionAt(transition.getBoard(), puzzleElement);
    }

    /**
     * Checks whether the tree node has a contradiction using this rule
     *
     * @param board board to check contradiction
     * @return null if the tree node contains a contradiction, otherwise error message
     */
    public String checkContradiction(Board board) {
        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            String checkStr = checkContradictionAt(board, puzzleElement);
            if (checkStr == null) {
                return checkStr;
            }
        }
        return this.NO_CONTRADICTION_MESSAGE;
    }

    public String getNoContradictionMessage() {
        return this.NO_CONTRADICTION_MESSAGE;
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using
     * this rule
     *
     * @param board board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     *     otherwise error message
     */
    public abstract String checkContradictionAt(Board board, PuzzleElement puzzleElement);
}
