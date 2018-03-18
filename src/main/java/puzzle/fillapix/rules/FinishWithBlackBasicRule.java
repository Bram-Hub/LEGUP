package puzzle.fillapix.rules;

import model.rules.BasicRule;
import model.tree.TreeTransition;

public class FinishWithBlackBasicRule extends BasicRule{
    public FinishWithBlackBasicRule() {
        super("Finish with Black",
                "The remaining unknowns around and on a cell must be black to satisfy the number",
                "images/fillapix/rules/FinishWithBlack.png");
    }

    @Override
    public String checkRuleAt(TreeTransition transition, int elementIndex) { return null; }

    @Override
    public boolean doDefaultApplication(TreeTransition transition) {return false; }

    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex) {return false; }
}