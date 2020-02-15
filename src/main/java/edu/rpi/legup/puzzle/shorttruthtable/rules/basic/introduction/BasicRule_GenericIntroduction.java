package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.BasicRule_Generic;
import edu.rpi.legup.model.rules.ContradictionRule;

public abstract class BasicRule_GenericIntroduction extends BasicRule_Generic {

    protected BasicRule_GenericIntroduction(String ruleName, ContradictionRule contradictionRule) {

        super(ruleName+" Introduction",
                ruleName+" statements must have a valid pattern",
                "introduction/"+ruleName,
                contradictionRule,
                false
        );
    }

}