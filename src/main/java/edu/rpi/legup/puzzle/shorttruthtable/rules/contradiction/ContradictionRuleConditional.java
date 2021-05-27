package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleConditional extends ContradictionRule_GenericStatement{

    public ContradictionRuleConditional(){
        super("Contradicting Conditional",
                "A Conditional statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Conditional.png",
                ShortTruthTableOperation.CONDITIONAL,
                new ShortTruthTableCellType[][] {
                        {n, F, T},
                        {F, F, n},
                        {T, T, F}
                }
        );
    }

}