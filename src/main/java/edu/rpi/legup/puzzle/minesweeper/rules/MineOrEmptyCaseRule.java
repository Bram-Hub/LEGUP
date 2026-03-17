package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import java.util.ArrayList;

public class MineOrEmptyCaseRule extends CaseRule {

    public MineOrEmptyCaseRule() {
        super(
                "MINE-CASE-0001",
                "Mine or Empty",
                "An unset cell can either be a mine or empty.",
                "edu/rpi/legup/images/minesweeper/cases/MineOrEmpty.png");
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
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();

        Board case1 = board.copy();
        MinesweeperCell cell1 = (MinesweeperCell) case1.getPuzzleElement(puzzleElement);
        cell1.setModifiable(false);
        cell1.setData(MinesweeperTileData.mine());
        case1.addModifiedData(cell1);
        cases.add(case1);

        Board case2 = board.copy();
        MinesweeperCell cell2 = (MinesweeperCell) case2.getPuzzleElement(puzzleElement);
        cell2.setModifiable(false);
        cell2.setData(MinesweeperTileData.empty());
        case2.addModifiedData(cell2);
        cases.add(case2);
        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
