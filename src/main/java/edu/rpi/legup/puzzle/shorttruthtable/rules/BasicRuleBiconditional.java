package edu.rpi.legup.puzzle.shorttruthtable.rules;

public class BasicRuleBiconditional extends BasicRule_Generic {

    public BasicRuleBiconditional() {
        super("Biconditional Rule",
                "Not statements must have opposite values",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/Biconditional.png",
                new ContradictionRuleBiconditional()
        );
    }

}
