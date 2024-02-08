package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAnd;

public class DirectRuleAndIntroduction extends DirectRule_GenericIntroduction {

    public DirectRuleAndIntroduction() {
        super("STTT-BASC-0007", "And", new ContradictionRuleAnd());
    }

}