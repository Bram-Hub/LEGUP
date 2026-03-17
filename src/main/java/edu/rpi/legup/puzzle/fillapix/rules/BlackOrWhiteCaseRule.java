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

public class BlackOrWhiteCaseRule extends CaseRule {
    public BlackOrWhiteCaseRule() {
        super(
                "FPIX-CASE-0001",
                "Black or White",
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
        if (puzzleElement == null) {
            return cases;
        }

        Board case1 = board.copy();
        FillapixCell cell1 = (FillapixCell) case1.getPuzzleElement(puzzleElement);
        cell1.setCellType(FillapixCellType.BLACK);
        case1.addModifiedData(cell1);
        cases.add(case1);

        Board case2 = board.copy();
        FillapixCell cell2 = (FillapixCell) case2.getPuzzleElement(puzzleElement);
        cell2.setCellType(FillapixCellType.WHITE);
        case2.addModifiedData(cell2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
