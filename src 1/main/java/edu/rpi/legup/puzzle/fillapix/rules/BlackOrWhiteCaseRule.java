package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;

import java.util.ArrayList;
import java.util.List;

public class BlackOrWhiteCaseRule extends CaseRule {
    public BlackOrWhiteCaseRule() {
        super("Black or White",
                "Each cell is either black or white.",
                "edu/rpi/legup/images/fillapix/cases/BlackOrWhite.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(fillapixBoard, this);
        fillapixBoard.setModifiable(false);
        for (PuzzleElement data : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) data;
            if (cell.getType() == FillapixCellType.UNKNOWN) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();

        Board case1 = board.copy();
        FillapixCell cell1 = (FillapixCell) case1.getPuzzleElement(puzzleElement);
        cell1.setType(FillapixCellType.BLACK);
        case1.addModifiedData(cell1);
        cases.add(case1);

        Board case2 = board.copy();
        FillapixCell cell2 = (FillapixCell) case2.getPuzzleElement(puzzleElement);
        cell2.setType(FillapixCellType.WHITE);
        case2.addModifiedData(cell2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2) {
            return "This case rule must have 2 children.";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1 ||
                case2.getBoard().getModifiedData().size() != 1) {
            return "This case rule must have 1 modified cell for each case.";
        }

        FillapixCell mod1 = (FillapixCell) case1.getBoard().getModifiedData().iterator().next();
        FillapixCell mod2 = (FillapixCell) case2.getBoard().getModifiedData().iterator().next();
        if (!mod1.getLocation().equals(mod2.getLocation())) {
            return "This case rule must modify the same cell for each case.";
        }

        if (!((mod1.getType() == FillapixCellType.BLACK && mod2.getType() == FillapixCellType.WHITE)
                || (mod2.getType() == FillapixCellType.BLACK && mod1.getType() == FillapixCellType.WHITE))) {
            return "This case rule must an empty cell and a lite cell.";
        }

        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}