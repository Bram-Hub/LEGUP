package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;

public class ContradictionRuleBiconditional extends ContradictionRule_GenericStatement{


    public ContradictionRuleBiconditional(){
        super("Contradicting Biconditional",
                "A Biconditional statement must have a contradicting pattern",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/Biconditional.png");
    }

    private static ShortTruthTableCellType[][] validPatterns = {
            {ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE},
            {ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.TRUE },
            {ShortTruthTableCellType.FALSE, ShortTruthTableCellType.FALSE, ShortTruthTableCellType.FALSE},
            {ShortTruthTableCellType.TRUE,  ShortTruthTableCellType.FALSE, ShortTruthTableCellType.TRUE },
    };


    char getOperationSymbol(){
        return ShortTruthTableStatement.BICONDITIONAL;
    }

    ShortTruthTableCellType[][] getContradictingPatterns(){
        return validPatterns;
    }

}