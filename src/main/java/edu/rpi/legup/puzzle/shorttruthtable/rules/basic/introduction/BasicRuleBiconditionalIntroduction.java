package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleBiconditional;

public class BasicRuleBiconditionalIntroduction extends BasicRule_GenericIntroduction {

    public BasicRuleBiconditionalIntroduction() {
        super("Biconditional", new ContradictionRuleBiconditional());
    }

}