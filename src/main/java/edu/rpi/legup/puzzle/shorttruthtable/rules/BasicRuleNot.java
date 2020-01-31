package edu.rpi.legup.puzzle.shorttruthtable.rules;

public class BasicRuleNot extends BasicRule_Generic {

    public BasicRuleNot() {
        super("Not Rule",
                "Not statements must have opposite values",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/Not.png",
                new ContradictionRuleNot()
        );
    }

}
