package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleNot;

public class DirectRuleNotIntroduction extends DirectRule_GenericIntroduction {

    public DirectRuleNotIntroduction() {
        super("STTT-BASC-0010", "Not", new ContradictionRuleNot());
    }

}