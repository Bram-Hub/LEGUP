package edu.rpi.legup.puzzle.shorttruthtable.rules.caserule;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import java.util.ArrayList;
import java.util.List;

public class CaseRuleAtomic extends CaseRule_Generic {

    public CaseRuleAtomic() {
        super(
                "STTT-CASE-0002",
                "Atomic",
                "True or False",
                "Each unknown cell must either be true or false");
    }

    // Adds all elements that can be selected for this case rule
    @Override
    public CaseBoard getCaseBoard(Board board) {
        ShortTruthTableBoard sttBoard = (ShortTruthTableBoard) board.copy();
        sttBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(sttBoard, this);
        for (PuzzleElement element : sttBoard.getPuzzleElements()) {
            if (((ShortTruthTableCell) element).getType() == ShortTruthTableCellType.UNKNOWN) {
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(ShortTruthTableCellType.TRUE);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(ShortTruthTableCellType.FALSE);
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 2 children";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1
            || case2.getBoard().getModifiedData().size() != 1) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 1 modified cell for each case";
        }

        ShortTruthTableCell mod1 = (ShortTruthTableCell) case1.getBoard().getModifiedData().iterator().next();
        ShortTruthTableCell mod2 = (ShortTruthTableCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must modify the same cell for each case";
        }

        if (!((mod1.getType() == ShortTruthTableCellType.FALSE && mod2.getType() == ShortTruthTableCellType.TRUE)
                || (mod2.getType() == ShortTruthTableCellType.FALSE && mod1.getType() == ShortTruthTableCellType.TRUE))) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have a True cell and a False cell";
        }
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
