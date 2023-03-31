package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.DirectRule_Generic;
import edu.rpi.legup.model.rules.ContradictionRule;

public abstract class DirectRule_GenericElimination extends DirectRule_Generic {

    public DirectRule_GenericElimination(String ruleID, String ruleName, ContradictionRule contradictionRule) {

        super(ruleID, ruleName + " Elimination",
                ruleName + " statements must have a valid pattern",
                "elimination/" + ruleName,
                contradictionRule,
                true
        );
    }

}