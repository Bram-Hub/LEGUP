package edu.rpi.legup.puzzle.shorttruthtable.rules;

public class BasicRuleAtomic extends BasicRule_Generic {

    public BasicRuleAtomic() {
        super("Fill in all atoms",
                "If one atomic value is known, all can be filled in with that value.",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/basic/Atomic.png",
                new ContradictionRuleAtomic()
        );
    }

}
