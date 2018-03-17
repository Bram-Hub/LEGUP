package model.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.TreeNode;
import model.tree.TreeTransition;

import static model.rules.RuleType.BASIC;

public abstract class BasicRule extends Rule
{
    /**
     * BasicRule Constructor - creates a new basic rule
     *
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public BasicRule(String ruleName, String description, String imageName)
    {
        super(ruleName, description, imageName);
        ruleType = BASIC;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    public String checkRule(TreeTransition transition)
    {
        Board finalBoard = transition.getBoard();

        if(!finalBoard.isModified())
        {
            return "State must be modified";
        }
        for(ElementData data: finalBoard.getModifiedData())
        {
            int elementIndex = data.getIndex();
            String checkStr = checkRuleAt(transition, elementIndex);
            if(checkStr != null)
                return checkStr;
        }
        return null;
    }
}
