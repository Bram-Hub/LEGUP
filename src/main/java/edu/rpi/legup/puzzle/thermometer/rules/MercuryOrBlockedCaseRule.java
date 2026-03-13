package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.*;
import java.util.ArrayList;
import java.util.List;

// TODO: Rule is untested
public class MercuryOrBlockedCaseRule extends CaseRule {
    public MercuryOrBlockedCaseRule() {
        super(
                "THERM-CASE-0001",
                "Mercury or Blocked",
                "Each unassigned tile must be filled with mercury or blocked.",
                "edu/rpi/legup/images/thermometer/MercOrBlocked.png");
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
        Board case1 = board.copy();
        ThermometerCell data1 = (ThermometerCell) case1.getPuzzleElement(puzzleElement);
        data1.setFill(ThermometerFill.FILLED);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        ThermometerCell data2 = (ThermometerCell) case2.getPuzzleElement(puzzleElement);
        data2.setFill(ThermometerFill.BLOCKED);
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }
}
