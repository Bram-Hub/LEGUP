package edu.rpi.legup.puzzle.shorttruthtable.rules;

public class BasicRuleAnd extends BasicRule_Generic {

    public BasicRuleAnd() {
        super("And Rule",
                "Not statements must have opposite values",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/And.png",
                new ContradictionRuleAnd()
        );
    }

}
