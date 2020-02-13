package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

public class ContradictionRuleBiconditional extends ContradictionRule_GenericStatement{


    public ContradictionRuleBiconditional(){
        super("Contradicting Biconditional",
                "A Biconditional statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Biconditional.png",
                ShortTruthTableOperation.BICONDITIONAL);
    }

}