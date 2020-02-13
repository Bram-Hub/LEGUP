package edu.rpi.legup.puzzle.shorttruthtable.rules;

public class BasicRuleOr extends BasicRule_Generic {

    public BasicRuleOr() {
        super("Or Rule",
                "Or statements must have opposite values",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/Or.png",
                new ContradictionRuleOr()
        );
    }

}
