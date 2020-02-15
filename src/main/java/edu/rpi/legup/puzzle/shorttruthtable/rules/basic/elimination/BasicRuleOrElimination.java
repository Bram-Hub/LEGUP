package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleOr;

public class BasicRuleOrElimination extends BasicRule_GenericElimination {

    public BasicRuleOrElimination() {
        super("Or", new ContradictionRuleOr());
    }

}