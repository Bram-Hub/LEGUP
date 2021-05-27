package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleOr extends ContradictionRule_GenericStatement{

    public ContradictionRuleOr(){
        super("Contradicting Or",
                "An OR statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Or.png",
                ShortTruthTableOperation.OR,
                new ShortTruthTableCellType[][] {
                    {n, F, T},
                    {T, F, n},
                    {F, T, F}
                }
        );
    }

}