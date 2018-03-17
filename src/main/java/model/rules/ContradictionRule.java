package model.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.TreeNode;
import model.tree.TreeTransition;

import static model.rules.RuleType.CONTRADICTION;

public abstract class ContradictionRule extends Rule
{
    /**
     * ContradictionRule Constructor - creates a new contradiction rule
     *
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public ContradictionRule(String ruleName, String description, String imageName)
    {
        super(ruleName, description, imageName);
        ruleType = CONTRADICTION;
    }

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
        return checkContradiction(transition);
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
        return checkContradictionAt(transition, elementIndex);
    }

    /**
     * Checks whether the tree node has a contradiction using this rule
     *
     * @param transition transition to check contradiction
     * @return null if the tree node contains a contradiction, otherwise error message
     */
    public String checkContradiction(TreeTransition transition)
    {
        for(ElementData data: transition.getBoard().getElementData())
        {
            String checkStr = checkContradictionAt(transition, data.getIndex());
            if(checkStr == null)
                return checkStr;
        }
        return "Does not contain a contradiction";
    }

    /**
     * Checks whether the transition has a contradiction at the specific element index using this rule
     *
     * @param transition transition to check contradiction
     * @param elementIndex index of the element
     * @return null if the transition contains a contradiction at the specified element,
     * otherwise error message
     */
    public abstract String checkContradictionAt(TreeTransition transition, int elementIndex);
}
