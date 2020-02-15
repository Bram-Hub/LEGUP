package edu.rpi.legup.puzzle.shorttruthtable.rules.basic;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleAtomic;

public class BasicRuleAtomic extends BasicRule_Generic{
    
    public BasicRuleAtomic(){
        super("Atomic Rule",
                "All identical atoms have the same T/F value",
                "Atomic",
                new ContradictionRuleAtomic(),
                false
        );
    }
    
}