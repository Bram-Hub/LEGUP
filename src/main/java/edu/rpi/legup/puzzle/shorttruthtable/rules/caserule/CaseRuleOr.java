package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleOr extends CaseRule_GenericStatement {

    public CaseRuleOr() {
        super("STTT-CASE-0005", ShortTruthTableOperation.OR,
                "Or",
                trueCases,
                falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {
            {T, U},
            {U, T}
    };
    private static final ShortTruthTableCellType[][] falseCases = {
            {F, F},
    };

}
