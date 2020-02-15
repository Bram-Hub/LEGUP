package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleBiconditional;

public class BasicRuleBiconditionalElimination extends BasicRule_GenericElimination {

    public BasicRuleBiconditionalElimination() {
        super("Biconditional", new ContradictionRuleBiconditional());
    }

}