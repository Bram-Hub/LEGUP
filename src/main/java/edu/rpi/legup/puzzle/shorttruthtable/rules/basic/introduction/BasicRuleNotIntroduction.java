package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleNot;

public class BasicRuleNotIntroduction extends BasicRule_GenericIntroduction {

    public BasicRuleNotIntroduction() {
        super("Not", new ContradictionRuleNot());
    }

}