package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileType;

import java.util.ArrayList;
import java.util.List;

public class BombOrFilledCaseRule extends CaseRule {

    public BombOrFilledCaseRule() {
        super("MINE-CASE-0001", "Bomb Or Filled",
                "A cell can either be a bomb or filled.\n",
                "edu/rpi/legup/images/minesweeper/cases/BombOrFilled.jpg");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(minesweeperBoard, this);
        minesweeperBoard.setModifiable(false);
        for (PuzzleElement data : minesweeperBoard.getPuzzleElements()) {
            MinesweeperCell cell = (MinesweeperCell) data;
            if (cell.getData().isUnset()) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();

        Board case1 = board.copy();
        MinesweeperCell cell1 = (MinesweeperCell) case1.getPuzzleElement(puzzleElement);
        cell1.setData(MinesweeperTileData.bomb());
        case1.addModifiedData(cell1);
        cases.add(case1);

        Board case2 = board.copy();
        MinesweeperCell cell2 = (MinesweeperCell) case2.getPuzzleElement(puzzleElement);
        cell2.setData(MinesweeperTileData.empty());
        case2.addModifiedData(cell2);
        cases.add(case2);
        return cases;
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
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 1 modified cell for each case.";
        }

        MinesweeperCell mod1 = (MinesweeperCell) case1.getBoard().getModifiedData().iterator().next();
        MinesweeperCell mod2 = (MinesweeperCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getData().isBomb() && mod2.getData().isEmpty())
                || (mod2.getData().isBomb() && mod1.getData().isEmpty()))) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must an empty cell and a bomb cell.";
        }

        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
