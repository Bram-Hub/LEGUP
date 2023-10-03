package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumberForCellCaseRule extends CaseRule {

    public NumberForCellCaseRule() {
        super("SKYS-CASE-0001", "Number For Cell",
                "A blank cell must have height of 1 to n.",
                "edu/rpi/legup/images/skyscrapers/cases/NumberForCell.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        SkyscrapersBoard lightUpBoard = (SkyscrapersBoard) board.copy();
        lightUpBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(lightUpBoard, this);
        for (PuzzleElement data : lightUpBoard.getPuzzleElements()) {
            if (((SkyscrapersCell) data).getType() == SkyscrapersType.UNKNOWN) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board         the current board state
     * @param puzzleElement puzzleElement to determine the possible cases for
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();

        Set<Integer> candidates = new HashSet<Integer>();
        for (int i = 1; i <= skyscrapersboard.getWidth(); i++) {
            Board newCase = board.copy();
            PuzzleElement newCell = newCase.getPuzzleElement(puzzleElement);
            newCell.setData(i);
            newCase.addModifiedData(newCell);

            //if flags
            boolean passed = true;
            if (skyscrapersboard.getDupeFlag()) {
                DuplicateNumberContradictionRule DupeRule = new DuplicateNumberContradictionRule();
                passed = passed && DupeRule.checkContradictionAt(newCase, newCell) != null;
            }
            if (skyscrapersboard.getViewFlag()) {
                PreemptiveVisibilityContradictionRule ViewRule = new PreemptiveVisibilityContradictionRule();
                passed = passed && ViewRule.checkContradictionAt(newCase, newCell) != null;
            }
            //how should unresolved be handled? should it be?
            if (passed) {
                cases.add(newCase);
            }
        }

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
        if (childTransitions.size() == 0) {
            return "This case rule must have at least one child.";
        }
        else {
            if (childTransitions.size() != getCases(transition.getBoard(), childTransitions.get(0).getBoard().getModifiedData().iterator().next()).size()) {
                return "Wrong number of children.";
            }
        }


        //TreeTransition case1 = childTransitions.get(0);
        //TreeTransition case2 = childTransitions.get(1);
        TreeTransition case1 = childTransitions.get(0);
        SkyscrapersCell mod1 = (SkyscrapersCell) case1.getBoard().getModifiedData().iterator().next();
        for (int i = 0; i < childTransitions.size(); i++) {
            TreeTransition case2 = childTransitions.get(i);
            if (case2.getBoard().getModifiedData().size() != 1) {
                return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
            }
            SkyscrapersCell mod2 = (SkyscrapersCell) case2.getBoard().getModifiedData().iterator().next();
            if (!mod1.getLocation().equals(mod2.getLocation())) {
                return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
            }
            if (!(mod2.getType() == SkyscrapersType.Number)) {
                return super.getInvalidUseOfRuleMessage() + ": This case rule must assign a number.";
            }
        }
        //System.out.println("no contradiction");
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRaw(transition);
    }
}
