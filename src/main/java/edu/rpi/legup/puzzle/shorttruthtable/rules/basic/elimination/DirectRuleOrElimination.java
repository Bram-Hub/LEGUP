package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleOr;

public class DirectRuleOrElimination extends DirectRule_GenericElimination {

    public DirectRuleOrElimination() {
        super("STTT-BASC-0006", "Or", new ContradictionRuleOr());
    }

}