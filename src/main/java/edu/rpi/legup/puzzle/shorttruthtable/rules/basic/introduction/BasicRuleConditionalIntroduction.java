package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleConditional;

public class BasicRuleConditionalIntroduction extends BasicRule_GenericIntroduction {

    public BasicRuleConditionalIntroduction() {
        super("STTT-BASC-0009", "Conditional", new ContradictionRuleConditional());
    }

}