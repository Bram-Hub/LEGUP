package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleOr extends CaseRule_GenericStatement {

    public CaseRuleOr() {
        super(ShortTruthTableOperation.OR,
                "Or",
                trueCases,
                falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {
            {T, N},
            {N, T}
    };
    private static final ShortTruthTableCellType[][] falseCases = {
            {F, F},
    };

}
