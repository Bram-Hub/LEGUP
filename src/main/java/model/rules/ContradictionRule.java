package model.rules;

import model.gameboard.Board;

import static model.rules.RuleType.CONTRADICTION;

public abstract class ContradictionRule extends Rule
{
    /**
     * ContradictionRule Constructor - creates a new contradiction rule
     *
     * @param ruleName name of the rule
     * @param imageName file name of the image
     * @param description description of the rule
     */
    public ContradictionRule(String ruleName, String imageName, String description)
    {
        super(ruleName, imageName, description);
        ruleType = CONTRADICTION;
    }

    /**
     * Checks whether the board has a contradiction using this rule
     *
     * @param board state of the board
     * @return null if the board contains a contradiction, otherwise error message
     */
    public abstract String checkContradiction(Board board);

    /**
     * Checks whether the board has a contradiction at the specific element index using this rule
     *
     * @param board state of the board
     * @param elementIndex index of the element
     * @return null if the board contains a contradiction at the specified element,
     * otherwise error message
     */
    public abstract String checkContradictionAt(Board board, int elementIndex);
}
