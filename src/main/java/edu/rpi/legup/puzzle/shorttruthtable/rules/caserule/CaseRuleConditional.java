package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleConditional extends CaseRule_GenericStatement {

    public CaseRuleConditional() {
        super(ShortTruthTableOperation.CONDITIONAL,
                "Conditional",
                trueCases,
                falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {
            {N, T},
            {F, N}
    };
    private static final ShortTruthTableCellType[][] falseCases = {
            {T, F},
    };

}
