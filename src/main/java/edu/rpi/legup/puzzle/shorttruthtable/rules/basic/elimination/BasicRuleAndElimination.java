package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAnd;

public class BasicRuleAndElimination extends BasicRule_GenericElimination {

    public BasicRuleAndElimination() {
        super("STTT-BASC-0002", "And", new ContradictionRuleAnd());
        System.out.println("and eliminatio constructor");
    }

}