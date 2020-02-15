package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAnd;

public class BasicRuleAndElimination extends BasicRule_GenericElimination {

    public BasicRuleAndElimination() {
        super("And", new ContradictionRuleAnd());
    }

}