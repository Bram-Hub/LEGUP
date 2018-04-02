package utility;

import model.rules.Rule;
import model.tree.TreeTransition;

public class ValidateRuleAction implements Action
{
    private TreeTransition transition;
    private Rule oldRule;
    private Rule newRule;

    public ValidateRuleAction(TreeTransition transition, Rule oldRule, Rule newRule)
    {
        this.transition = transition;
        this.oldRule = oldRule;
        this.newRule = newRule;
    }

    /**
     * Undoes an action on the board
     */
    @Override
    public void undo()
    {
        this.transition.setRule(oldRule);
    }

    /**
     * Redoes an action on the board
     */
    @Override
    public void redo()
    {
        this.transition.setRule(newRule);
    }
}
