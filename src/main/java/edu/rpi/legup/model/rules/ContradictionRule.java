package edu.rpi.legup.model.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeTransition;

import static edu.rpi.legup.model.rules.RuleType.CONTRADICTION;

public abstract class ContradictionRule extends Rule {
    /**
     * ContradictionRule Constructor creates a new contradiction rule
     *
     * @param ruleName    name of the rule
     * @param description description of the rule
     * @param imageName   file name of the image
     */
    public ContradictionRule(String ruleName, String description, String imageName) {
        super(ruleName, description, imageName);
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
        return checkContradiction(transition);
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
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkContradictionAt(transition, puzzleElement);
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule.
     * This method is the one that should overridden in child classes
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return checkRule(transition);
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     * This method is the one that should overridden in child classes
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleAt(transition, puzzleElement);
    }

    /**
     * Checks whether the tree node has a contradiction using this rule
     *
     * @param transition transition to check contradiction
     * @return null if the tree node contains a contradiction, otherwise error message
     */
    public String checkContradiction(TreeTransition transition) {
        for (PuzzleElement puzzleElement : transition.getBoard().getPuzzleElements()) {
            String checkStr = checkContradictionAt(transition, puzzleElement);
            if (checkStr == null) {
                return checkStr;
            }
        }
        return "Does not contain a contradiction";
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    public abstract String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement);
}
