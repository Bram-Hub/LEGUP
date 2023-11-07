package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleConditional extends CaseRule_GenericStatement {

    public CaseRuleConditional() {
        super("STTT-CASE-0004", ShortTruthTableOperation.CONDITIONAL,
                "Conditional",
                trueCases,
                falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {
            {U, T},
            {F, U}
    };
    private static final ShortTruthTableCellType[][] falseCases = {
            {T, F},
    };

}
