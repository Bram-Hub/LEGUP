package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.BasicRule_Generic;
import edu.rpi.legup.model.rules.ContradictionRule;

public abstract class BasicRule_GenericElimination extends BasicRule_Generic {

    public BasicRule_GenericElimination(String ruleName, ContradictionRule contradictionRule) {

        super(ruleName+" Elimination",
                ruleName+" statements must have a valid pattern",
                "elimination/"+ruleName,
                contradictionRule,
                true
        );
    }

}