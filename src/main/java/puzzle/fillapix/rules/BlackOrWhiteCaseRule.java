package puzzle.fillapix.rules;

import model.gameboard.Board;
import model.gameboard.CaseBoard;
import model.gameboard.Element;
import model.rules.CaseRule;
import model.tree.TreeTransition;
import puzzle.fillapix.Fillapix;
import puzzle.fillapix.FillapixBoard;
import puzzle.fillapix.FillapixCell;

import java.util.ArrayList;

public class BlackOrWhiteCaseRule extends CaseRule {
    public BlackOrWhiteCaseRule() {
        super("Black or White",
                "Each cell is either black or white.",
                "images/fillapix/cases/BlackOrWhite.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(fillapixBoard, this);
        fillapixBoard.setModifiable(false);
        for (Element data: fillapixBoard.getElementData()) {
            FillapixCell cell = (FillapixCell) data;
            if(FillapixCell.isUnknown(cell.getData())) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, int elementIndex){
        ArrayList<Board> cases = new ArrayList<>();

        Board case1 = board.copy();
        FillapixCell cell1 = (FillapixCell) case1.getElementData(elementIndex);
        int case1Value = cell1.getData() + FillapixCell.BLACK;
        case1.getElementData(elementIndex).setData(case1Value);
        case1.getElementData(elementIndex).setModified(true);
        cases.add(case1);

        Board case2 = board.copy();
        FillapixCell cell2 = (FillapixCell) case2.getElementData(elementIndex);
        int case2Value = cell2.getData() + FillapixCell.BLACK + FillapixCell.WHITE;
        case2.getElementData(elementIndex).setData(case2Value);
        case2.getElementData(elementIndex).setModified(true);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRule(TreeTransition transition) { return null; }

    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex) { return null; }

    @Override
    public boolean doDefaultApplication(TreeTransition transition) { return false; }

    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex) { return false; }
}