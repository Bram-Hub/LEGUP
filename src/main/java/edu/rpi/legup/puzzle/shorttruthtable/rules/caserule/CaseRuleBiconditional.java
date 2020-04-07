package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class CaseRuleBiconditional extends CaseRule_GenericStatement {

    public CaseRuleBiconditional() {
        super(ShortTruthTableOperation.BICONDITIONAL,
                "True or False",
                "Each unknown statement must be either true or false",
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
