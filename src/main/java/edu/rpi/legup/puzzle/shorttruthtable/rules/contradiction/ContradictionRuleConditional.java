package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

public class ContradictionRuleConditional extends ContradictionRule_GenericStatement{


    public ContradictionRuleConditional(){
        super("Contradicting Conditional",
                "A Conditional statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Conditional.png",
                ShortTruthTableOperation.CONDITIONAL);
    }

}