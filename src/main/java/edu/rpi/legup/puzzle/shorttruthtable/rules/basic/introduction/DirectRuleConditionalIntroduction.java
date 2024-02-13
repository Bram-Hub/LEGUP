package edu.rpi.legup.puzzle.shorttruthtable.rules.basic.introduction;

import edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction.ContradictionRuleConditional;

public class DirectRuleConditionalIntroduction extends DirectRule_GenericIntroduction {

  public DirectRuleConditionalIntroduction() {
    super("STTT-BASC-0009", "Conditional", new ContradictionRuleConditional());
  }
}
