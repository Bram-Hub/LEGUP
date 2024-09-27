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

public class ZeroOrOneCaseRule extends CaseRule {

    public ZeroOrOneCaseRule() {
        super(
                "BINA-CASE-0001",
                "Zero Or One",
                "Each blank cell is either a zero or a one",
                "edu/rpi/legup/images/binary/rules/ZeroOrOneCaseRule.png");
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule. This method is the one that should be overridden in child classes.
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

        BinaryCell mod1 = (BinaryCell) case1.getBoard().getModifiedData().iterator().next();
        BinaryCell mod2 = (BinaryCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getType() == BinaryType.ZERO && mod2.getType() == BinaryType.ONE)
                || (mod2.getType() == BinaryType.ZERO && mod1.getType() == BinaryType.ONE))) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must modify an empty cell.";
        }

        return null;
    }

    /**
     * Generates a {@link CaseBoard} that includes all blank cells from the given board that this
     * case rule can be applied to
     *
     * @param board The board to find locations where this case rule can be applied
     * @return A CaseBoard containing pickable elements where the case rule can be applied
     */
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

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(BinaryType.ONE.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(BinaryType.ZERO.toValue());
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
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
        return null;
    }
}
