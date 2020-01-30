package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleConditional extends ContradictionRule_GenericStatement{


    public ContradictionRuleConditional(){
        super("Contradicting Conditional",
                "A Conditional statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/And.png");
    }

    private static ShortTruthTableCellType[][] validPatterns = {
            {null,                          ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE },
            {ShortTruthTableCellType.FALSE, ShortTruthTableCellType.FALSE, null,                        },
            {ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE},
    };


    char getOperationSymbol(){
        return ShortTruthTableStatement.CONDITIONAL;
    }

    ShortTruthTableCellType[][] getContradictingPatterns(){
        return validPatterns;
    }

}