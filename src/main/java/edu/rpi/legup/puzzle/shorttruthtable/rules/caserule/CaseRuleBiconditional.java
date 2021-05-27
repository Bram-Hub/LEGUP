package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleBiconditional extends CaseRule_GenericStatement {

    public CaseRuleBiconditional() {
        super(ShortTruthTableOperation.BICONDITIONAL,
                "Biconditional",
                trueCases,
                falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {
            {T, T},
            {F, F}
    };
    private static final ShortTruthTableCellType[][] falseCases = {
            {T, F},
            {F, T}
    };

}
