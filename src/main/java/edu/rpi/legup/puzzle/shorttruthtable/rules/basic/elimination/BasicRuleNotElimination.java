package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleNot;

public class BasicRuleNotElimination extends BasicRule_GenericElimination {

    public BasicRuleNotElimination() {
        super("STTT-BASC-0005", "Not", new ContradictionRuleNot());
    }

}