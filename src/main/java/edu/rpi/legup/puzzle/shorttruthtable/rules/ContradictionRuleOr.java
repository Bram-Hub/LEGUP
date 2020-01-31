package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleOr extends ContradictionRule_GenericStatement{


    public ContradictionRuleOr(){
        super("Contradicting Or",
                "An OR statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Or.png");
    }

    private static ShortTruthTableCellType[][] validPatterns = {
            {null,                          ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE },
            {ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE, null,                        },
            {ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE},
    };


    char getOperationSymbol(){
        return ShortTruthTableStatement.OR;
    }

    ShortTruthTableCellType[][] getContradictingPatterns(){
        return validPatterns;
    }

}