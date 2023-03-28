package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleOr;

public class DirectRuleOrIntroduction extends DirectRule_GenericIntroduction {

    public DirectRuleOrIntroduction() {
        super("STTT-BASC-0011", "Or", new ContradictionRuleOr());
    }

}