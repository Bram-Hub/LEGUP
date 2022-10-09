package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;


import java.util.ArrayList;
import java.util.List;

public abstract class CaseRule_Generic extends CaseRule {

    public CaseRule_Generic(String ruleID, String ruleName, String title, String description) {
        super(ruleID, title, description, "edu/rpi/legup/images/shorttruthtable/ruleimages/case/"+ruleName+".png");
    }



    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition)
    {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2)
            return "This case rule must have 2 children.";

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1 || case2.getBoard().getModifiedData().size() != 1)
            return "This case rule must have 1 modified cell for each case.";

        ShortTruthTableCell mod1 = (ShortTruthTableCell) case1.getBoard().getModifiedData().iterator().next();
        ShortTruthTableCell mod2 = (ShortTruthTableCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation()))
            return "This case rule must modify the same cell for each case.";

        boolean firstPossibility = mod1.getType() == ShortTruthTableCellType.TRUE && mod2.getType() == ShortTruthTableCellType.FALSE;
        boolean secondPossibility = mod1.getType() == ShortTruthTableCellType.FALSE && mod2.getType() == ShortTruthTableCellType.TRUE;
        if (!firstPossibility && !secondPossibility)
            return "This case rule must an empty true or false cell.";

        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return checkRuleRaw(transition);
    }
}
