package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

public class CaseRuleAnd extends CaseRule_GenericStatement {

    public CaseRuleAnd() {
        super("STTT-CASE-0001", ShortTruthTableOperation.AND, "And", trueCases, falseCases);
    }

    private static final ShortTruthTableCellType[][] trueCases = {{T, T}};
    private static final ShortTruthTableCellType[][] falseCases = {
        {F, U},
        {U, F}
    };
}
