package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleBiconditional;

public class DirectRuleBiconditionalIntroduction extends DirectRule_GenericIntroduction {

    public DirectRuleBiconditionalIntroduction() {
        super("STTT-BASC-0008", "Biconditional", new ContradictionRuleBiconditional());
    }

}