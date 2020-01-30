package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleAnd extends ContradictionRule_GenericStatement{


    public ContradictionRuleAnd(){
        super("Contradicting And",
                "An AND statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/And.png");
    }

    private static ShortTruthTableCellType[][] validPatterns = {
            {null,                          ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE},
            {ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE,  null,                        },
            {ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE },
    };


    char getOperationSymbol(){
        return ShortTruthTableStatement.AND;
    }

    ShortTruthTableCellType[][] getContradictingPatterns(){
        return validPatterns;
    }

}