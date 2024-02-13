package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.elimination;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleNot;

public class DirectRuleNotElimination extends DirectRule_GenericElimination {

  public DirectRuleNotElimination() {
    super("STTT-BASC-0005", "Not", new ContradictionRuleNot());
  }
}
