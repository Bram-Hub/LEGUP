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

public class ZeroOrOneCaseRule extends CaseRule {

    public ZeroOrOneCaseRule() {
        super(
                "BINA-CASE-0001",
                "Zero Or One",
                "Each blank cell is either a zero or a one",
                "edu/rpi/legup/images/binary/rules/ZeroOrOneCaseRule.png");
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
