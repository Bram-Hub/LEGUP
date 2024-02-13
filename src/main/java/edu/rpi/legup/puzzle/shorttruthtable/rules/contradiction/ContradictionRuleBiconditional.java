package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableOperation;

public class ContradictionRuleBiconditional extends ContradictionRule_GenericStatement {

  public ContradictionRuleBiconditional() {
    super(
        "STTT-CONT-0003",
        "Contradicting Biconditional",
        "A Biconditional statement must have a contradicting pattern",
        "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Biconditional.png",
        ShortTruthTableOperation.BICONDITIONAL,
        new ShortTruthTableCellType[][] {
          {T, T, F},
          {F, T, T},
          {T, F, T},
          {F, F, F}
        });
  }
}
