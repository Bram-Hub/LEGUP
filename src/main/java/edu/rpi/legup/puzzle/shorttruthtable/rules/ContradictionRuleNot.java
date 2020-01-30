package edu.rpi.legup.puzzle.shorttruthtable.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;

import java.util.Set;
import java.util.Iterator;


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