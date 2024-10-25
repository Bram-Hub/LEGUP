package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAnd;

public class DirectRuleAndElimination extends DirectRule_GenericElimination {

    public DirectRuleAndElimination() {
        super("STTT-BASC-0002", "And", new ContradictionRuleAnd());
    }
}
