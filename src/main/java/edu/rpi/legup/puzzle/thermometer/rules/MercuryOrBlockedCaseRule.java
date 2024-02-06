package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.*;

import java.util.ArrayList;
import java.util.List;

public class MercuryOrBlockedCaseRule extends CaseRule {
    public MercuryOrBlockedCaseRule() {
        super("MOB",
                "Mercury or Blocked",
                "Each unassigned tile must be filled with mercury or blocked.",
                "edu/rpi/legup/images/thermometers/cases/MercuryOrBlocked.png");
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 2 children.";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1 ||
                case2.getBoard().getModifiedData().size() != 1) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
        }

        ThermometerCell mod1 = (ThermometerCell) case1.getBoard().getModifiedData().iterator().next();
        ThermometerCell mod2 = (ThermometerCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getFill() == ThermometerFill.BLOCKED && mod2.getFill() == ThermometerFill.FILLED) ||
                (mod2.getFill() == ThermometerFill.BLOCKED && mod1.getFill() == ThermometerFill.FILLED))) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have a filled or blocked cell.";
        }

        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        ThermometerBoard thermometerBoard = (ThermometerBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(thermometerBoard, this);
        thermometerBoard.setModifiable(false);
        for (PuzzleElement element : thermometerBoard.getPuzzleElements()) {
            if (((ThermometerCell) element).getFill() == ThermometerFill.UNKNOWN) {
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(ThermometerType.HEAD);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(ThermometerFill.BLOCKED);
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
