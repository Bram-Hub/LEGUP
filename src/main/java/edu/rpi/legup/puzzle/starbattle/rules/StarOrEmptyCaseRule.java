package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;

import java.util.ArrayList;
import java.util.List;

public class StarOrEmptyCaseRule extends CaseRule {

    public StarOrEmptyCaseRule() {
        super("STBL-CASE-0002",
                "Star or Empty",
                "Each unknown space is either a star or empty.",
                "INSERT IMAGE NAME HERE");
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this rule. This method is
     * the one that should overridden in child classes.
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
        if (case1.getBoard().getModifiedData().size() != 1 ||
                case2.getBoard().getModifiedData().size() != 1) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
        }

        StarBattleCell mod1 = (StarBattleCell) case1.getBoard().getModifiedData().iterator().next();
        StarBattleCell mod2 = (StarBattleCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getType() == StarBattleCellType.STAR && mod2.getType() == StarBattleCellType.BLACK) ||
                (mod2.getType() == StarBattleCellType.STAR && mod1.getType() == StarBattleCellType.BLACK))) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must create a star cell and a black cell.";
        }

        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        StarBattleBoard starBattleBoard = (StarBattleBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(starBattleBoard, this);
        starBattleBoard.setModifiable(false);
        for (PuzzleElement element : starBattleBoard.getPuzzleElements()) {
            if (((StarBattleCell) element).getType() == StarBattleCellType.UNKNOWN) {
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board         the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(StarBattleCellType.STAR);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(StarBattleCellType.BLACK);
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
