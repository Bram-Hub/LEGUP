package utility;

import model.rules.Rule;
import model.tree.TreeTransition;

public class ValidateRuleCommand extends PuzzleCommand
{
    private TreeTransition transition;
    private Rule oldRule;
    private Rule newRule;

    public ValidateRuleCommand(TreeTransition transition, Rule oldRule, Rule newRule)
    {
        this.transition = transition;
        this.oldRule = oldRule;
        this.newRule = newRule;
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {

    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {

    }
}
