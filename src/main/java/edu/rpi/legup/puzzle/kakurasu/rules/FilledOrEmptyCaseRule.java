package edu.rpi.legup.puzzle.kakurasu.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.kakurasu.KakurasuBoard;
import edu.rpi.legup.puzzle.kakurasu.KakurasuCell;
import edu.rpi.legup.puzzle.kakurasu.KakurasuType;

import java.util.ArrayList;
import java.util.List;

public class FilledOrEmptyCaseRule extends CaseRule {

    public FilledOrEmptyCaseRule() {
        super(
                "KAKU-CASE-0001",
                "Filled or Empty",
                "Each blank cell is either filled or empty.",
                "edu/rpi/legup/images/kakurasu/temp.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        KakurasuBoard kakurasuBoard = (KakurasuBoard) board.copy();
        kakurasuBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(kakurasuBoard, this);
        for (PuzzleElement element : kakurasuBoard.getPuzzleElements()) {
            if (((KakurasuCell) element).getType() == KakurasuType.UNKNOWN) {
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
        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(KakurasuType.FILLED);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(KakurasuType.EMPTY);
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
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
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 1 modified cell for each case.";
        }

        KakurasuCell mod1 = (KakurasuCell) case1.getBoard().getModifiedData().iterator().next();
        KakurasuCell mod2 = (KakurasuCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getType() == KakurasuType.FILLED && mod2.getType() == KakurasuType.EMPTY)
                || (mod2.getType() == KakurasuType.FILLED && mod1.getType() == KakurasuType.EMPTY))) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have a filled and an empty cell.";
        }

        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRaw(transition);
    }
}
