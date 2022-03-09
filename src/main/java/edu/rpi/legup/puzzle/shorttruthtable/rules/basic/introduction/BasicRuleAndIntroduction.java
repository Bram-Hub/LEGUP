package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAnd;

public class BasicRuleAndIntroduction extends BasicRule_GenericIntroduction {

    public BasicRuleAndIntroduction() {
        super("STTT-BASC-0007", "And", new ContradictionRuleAnd());
    }

}