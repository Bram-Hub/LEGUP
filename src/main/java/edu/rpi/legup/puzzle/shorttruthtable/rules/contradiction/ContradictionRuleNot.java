package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

public class ContradictionRuleNot extends ContradictionRule_GenericStatement {

    public ContradictionRuleNot() {
        super(
                "STTT-CONT-0005",
                "Contradicting Negation",
                "A negation and its following statement can not have the same truth value",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Not.png",
                ShortTruthTableOperation.NOT,
                new ShortTruthTableCellType[][] {
                    {n, T, T},
                    {n, F, F}
                });
    }
}
