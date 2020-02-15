package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleOr;

public class BasicRuleOrIntroduction extends BasicRule_GenericIntroduction {

    public BasicRuleOrIntroduction() {
        super("Or", new ContradictionRuleOr());
    }

}