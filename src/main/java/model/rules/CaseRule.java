package model.rules;

import model.gameboard.Board;
import model.gameboard.CaseBoard;
import model.tree.TreeTransition;

import java.util.ArrayList;

import static model.rules.RuleType.CASE;

public abstract class CaseRule extends Rule
{
    /**
     * CaseRule Constructor - creates a new case rule
     *
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public CaseRule(String ruleName, String description, String imageName)
    {
        super(ruleName, description, imageName);
        this.ruleType = CASE;
    }

    /**
     * Gets the case board that indicates where this case rule can be applied
     *
     * @param board board state to find locations where this case rule can be applied
     * @return a case board
     */
    public abstract CaseBoard getCaseBoard(Board board);

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param elementIndex element to determine the possible cases for
     * @return a list of elements the specified could be
     */
    public abstract ArrayList<Board> getCases(Board board, int elementIndex);

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule.
     * This method is the one that should overridden in child classes
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleAt(TreeTransition transition, int elementIndex)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     * This method is the one that should overridden in child classes
     *
     * @param transition   transition to check
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
        return null;
    }
}


