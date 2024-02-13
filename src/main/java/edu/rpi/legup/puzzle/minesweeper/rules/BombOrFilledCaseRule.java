package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileType;

import java.util.ArrayList;
import java.util.List;

public class BombOrFilledCaseRule extends CaseRule {

    public BombOrFilledCaseRule() {
        super("NURI-CASE-0001",
                "Bomb or Filled",
                "Each blank cell is either a bomb or empty.",
                "edu/rpi/legup/images/nurikabe/cases/BlackOrWhite.png");
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

        MinesweeperCell mod1 = (MinesweeperCell) case1.getBoard().getModifiedData().iterator().next();
        MinesweeperCell mod2 = (MinesweeperCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getTileType() == MinesweeperTileType.BOMB && mod2.getTileType() == MinesweeperTileType.EMPTY) ||
                (mod2.getTileType() == MinesweeperTileType.EMPTY && mod1.getTileType() == MinesweeperTileType.BOMB))) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must an empty white and black cell.";
        }

        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(minesweeperBoard, this);
        minesweeperBoard.setModifiable(false);
        for (PuzzleElement element : minesweeperBoard.getPuzzleElements()) {
            if (((MinesweeperCell) element).getTileType() == MinesweeperTileType.UNSET) {
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
        data1.setData(MinesweeperTileType.EMPTY.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(MinesweeperTileType.BOMB.toValue());
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
