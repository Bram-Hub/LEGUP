package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleConditional;

public class BasicRuleConditionalElimination extends BasicRule_GenericElimination {

    public BasicRuleConditionalElimination() {
        super("Conditional", new ContradictionRuleConditional());
    }

}