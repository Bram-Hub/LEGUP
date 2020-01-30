package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.model.rules.ContradictionRule;

import java.awt.*;
import java.util.List;

public class BasicRuleAtomic extends BasicRule_Generic {

    private static final ContradictionRule correspondingContradictionRule = new ContradictionRuleAtomic();

    public BasicRuleAtomic() {
        super("Fill in all atoms",
                "If one atomic value is known, all can be filled in with that value.",
                "edu/rpi/legup/images/shorttruthtable/ruleimages/contradiction/And.png");
    }

    @Override
    ContradictionRule getContradictionRule(){
        return correspondingContradictionRule;
    }


    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node short truth table board used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
