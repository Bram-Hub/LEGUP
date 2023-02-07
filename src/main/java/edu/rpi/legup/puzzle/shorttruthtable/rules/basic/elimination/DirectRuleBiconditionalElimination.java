package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleBiconditional;

public class DirectRuleBiconditionalElimination extends DirectRule_GenericElimination {

    public DirectRuleBiconditionalElimination() {
        super("STTT-BASC-0003", "Biconditional", new ContradictionRuleBiconditional());
    }

}