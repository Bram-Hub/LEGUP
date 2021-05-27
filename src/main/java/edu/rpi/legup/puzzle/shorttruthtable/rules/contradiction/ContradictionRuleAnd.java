package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleAnd extends ContradictionRule_GenericStatement{

    public ContradictionRuleAnd(){
        super("Contradicting And",
                "An AND statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/And.png",
                ShortTruthTableOperation.AND,
                new ShortTruthTableCellType[][] {
                    {n, T, F},
                    {F, T, n},
                    {T, F, T},
                }
        );
    }

}