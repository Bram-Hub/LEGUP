package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.DirectRule_Generic;

public abstract class DirectRule_GenericIntroduction extends DirectRule_Generic {

    protected DirectRule_GenericIntroduction(
            String ruleID, String ruleName, ContradictionRule contradictionRule) {

        super(
                ruleID,
                ruleName + " Introduction",
                ruleName + " statements must have a valid pattern",
                "introduction/" + ruleName,
                contradictionRule,
                false);
    }
}
