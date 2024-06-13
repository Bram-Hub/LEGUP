package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;
import java.util.ArrayList;
import java.util.List;

public class OneOrZeroCaseRule extends CaseRule {

    public OneOrZeroCaseRule() {
        super(
                "BINA-CASE-0001",
                "One or Zero",
                "Each blank cell is either a one or a zero.",
                "edu/rpi/legup/images/binary/rules/OneOrZeroCaseRule.png");
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 2 children.";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1
                || case2.getBoard().getModifiedData().size() != 1) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
        }

        BinaryCell mod1 = (BinaryCell) case1.getBoard().getModifiedData().iterator().next();
        BinaryCell mod2 = (BinaryCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getType() == BinaryType.ZERO && mod2.getType() == BinaryType.ONE)
                || (mod2.getType() == BinaryType.ZERO && mod1.getType() == BinaryType.ONE))) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify an empty cell.";
        }

        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        BinaryBoard binaryBoard = (BinaryBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(binaryBoard, this);
        binaryBoard.setModifiable(false);
        for (PuzzleElement element : binaryBoard.getPuzzleElements()) {
            if (((BinaryCell) element).getType() == BinaryType.UNKNOWN) {
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
        data1.setData(BinaryType.ZERO.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(BinaryType.ONE.toValue());
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
