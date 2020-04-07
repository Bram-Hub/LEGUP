package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleConditional extends CaseRule_GenericStatement {

    public CaseRuleConditional() {
        super(ShortTruthTableOperation.CONDITIONAL,
                "True or False",
                "Each unknown statement must be either true or false",
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
