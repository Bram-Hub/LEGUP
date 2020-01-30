package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleNot extends ContradictionRule_GenericStatement{


    public ContradictionRuleNot(){
        super("Contradicting Negation",
                "A negation and its following statement can not have the same truth value",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Not.png");
    }

    private static ShortTruthTableCellType[][] validPatterns = {
        {null, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.FALSE},
        {null, ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.TRUE}
    };


    char getOperationSymbol(){
        return ShortTruthTableStatement.NOT;
    }

    ShortTruthTableCellType[][] getContradictingPatterns(){
        return validPatterns;
    }

}