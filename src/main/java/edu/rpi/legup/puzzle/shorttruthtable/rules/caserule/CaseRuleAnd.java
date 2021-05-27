package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleAnd extends CaseRule_GenericStatement {

    public CaseRuleAnd() {
        super(ShortTruthTableOperation.AND,
                "And",
                trueCases,
                falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {
            {T, T}
    };
    private static final ShortTruthTableCellType[][] falseCases = {
            {F, N},
            {N, F}
    };

}
