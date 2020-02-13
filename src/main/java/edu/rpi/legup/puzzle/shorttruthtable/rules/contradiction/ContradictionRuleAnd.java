package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

public class ContradictionRuleAnd extends ContradictionRule_GenericStatement{


    public ContradictionRuleAnd(){
        super("Contradicting And",
                "An AND statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/And.png",
                ShortTruthTableOperation.AND);
    }

}