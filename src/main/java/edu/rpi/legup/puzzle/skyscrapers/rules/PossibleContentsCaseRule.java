package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class PossibleContentsCaseRule extends CaseRule {

    public PossibleContentsCaseRule() {
        super("SKYS-CASE-0001", "Possible Contents",
                "Each blank cell is could have height of 1 to n.",
                "edu/rpi/legup/images/skyscrapers/PossibleContents.png");
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
            candidates.add(i);
        }

        for (int i = 0; i < skyscrapersboard.getWidth(); i++) {
            SkyscrapersCell c = skyscrapersboard.getCell(i, loc.y);
            if (c.getType() == SkyscrapersType.Number) {
                candidates.remove(c.getData());
            }
        }
        for (int i = 0; i < skyscrapersboard.getHeight(); i++) {
            SkyscrapersCell c = skyscrapersboard.getCell(loc.x, i);
            if (c.getType() == SkyscrapersType.Number) {
                candidates.remove(c.getData());
            }
        }

        Iterator<Integer> it = candidates.iterator();
        while (it.hasNext()) {
            Board case1 = board.copy();
            PuzzleElement data = case1.getPuzzleElement(puzzleElement);
            data.setData(it.next());
            case1.addModifiedData(data);
            cases.add(case1);
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
            //System.out.println("0");
            return "This case rule must have at least one child.";
        }


        //TreeTransition case1 = childTransitions.get(0);
        //TreeTransition case2 = childTransitions.get(1);
        TreeTransition case1 = childTransitions.get(0);
        SkyscrapersCell mod1 = (SkyscrapersCell) case1.getBoard().getModifiedData().iterator().next();
        for (int i = 0; i < childTransitions.size(); i++) {
            TreeTransition case2 = childTransitions.get(i);
            if (case2.getBoard().getModifiedData().size() != 1) {
                //System.out.println("1");
                return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
            }
            SkyscrapersCell mod2 = (SkyscrapersCell) case2.getBoard().getModifiedData().iterator().next();
            if (!mod1.getLocation().equals(mod2.getLocation())) {
                //System.out.println("2");
                return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
            }
            if (!(mod2.getType() == SkyscrapersType.Number)) {
                //System.out.println("3");
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
