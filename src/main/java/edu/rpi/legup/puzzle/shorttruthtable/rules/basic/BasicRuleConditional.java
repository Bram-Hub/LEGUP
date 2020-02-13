package edu.rpi.legup.puzzle.shorttruthtable.rules;

public class BasicRuleConditional extends BasicRule_Generic {

    public BasicRuleConditional() {
        super("Conditional Rule",
                "Not statements must have opposite values",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/Conditional.png",
                new ContradictionRuleConditional()
        );
    }

}
