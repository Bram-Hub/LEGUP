package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleConditional;

public class DirectRuleConditionalElimination extends DirectRule_GenericElimination {

    public DirectRuleConditionalElimination() {
        super("STTT-BASC-0004", "Conditional", new ContradictionRuleConditional());
    }
}
