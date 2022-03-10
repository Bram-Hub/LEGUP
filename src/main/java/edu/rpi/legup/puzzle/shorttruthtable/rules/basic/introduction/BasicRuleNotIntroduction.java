package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleNot;

public class BasicRuleNotIntroduction extends BasicRule_GenericIntroduction {

    public BasicRuleNotIntroduction() {
        super("STTT-BASC-0010", "Not", new ContradictionRuleNot());
    }

}