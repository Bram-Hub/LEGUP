package puzzle.fillapix.rules;

import model.gameboard.Board;
import model.rules.BasicRule;
import model.tree.TreeTransition;
import puzzle.fillapix.FillapixBoard;
import puzzle.fillapix.FillapixCell;

import java.util.ArrayList;

public class FinishWithBlackBasicRule extends BasicRule {
    public FinishWithBlackBasicRule() {
        super("Finish with Black",
                "The remaining unknowns around and on a cell must be black to satisfy the number",
                "images/fillapix/rules/FinishWithBlack.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex) {
        FillapixBoard fillapixBoard = (FillapixBoard) transition.getBoard();
        int width = fillapixBoard.getWidth();
        int height = fillapixBoard.getHeight();
        FillapixCell cell = fillapixBoard.getCell(elementIndex%width,elementIndex/width);

        // start up case rule for each one
        // if that leads to a contradiction,
        // it must be the other one in the case
        // check them that way

        BlackOrWhiteCaseRule blackOrWhite = new BlackOrWhiteCaseRule();
        TooFewBlackCellsContradictionRule tooFewBlackCells = new TooFewBlackCellsContradictionRule();

        FillapixBoard currentBoard = (FillapixBoard) transition.getParentNode().getBoard();
        // Keep in mind, this rule is called FinishWithBlack so we're only looking at the cases where
        // we color the cells black and therefore are doing a lot of extra work!
        // Since we know we are comparing against black cells, we can avoid a lot of unnecessary board copying
        // Copying boards unnecessarily is inefficient and can slow the program down
        // even though they are logically equivalent
        for(int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int x = cell.getLocation().x + i;
                int y = cell.getLocation().y + j;
                if (x > -1 && x < width && y > -1 && y < height) {
                    // boolean parentCellUnknown = ((FillapixBoard) transition.getParentNode().getBoard()).getCell(x,y).isUnknown();
                    // if (parentCellUnknown && !fillapixBoard.getCell(x,y).isBlack()) {
                    //     return "All the changes you made must be black to apply this rule.";
                    // }

                    ArrayList<Board> cases = blackOrWhite.getCases(currentBoard, x*width+y);
                    for (Board caseBoard: cases) {
                        String contradiction = tooFewBlackCells.checkContradictionAt((FillapixBoard) caseBoard,x*width+y);
                        FillapixCell caseCell = ((FillapixBoard) caseBoard).getCell(x,y);
                        if (caseCell.hasSameState(fillapixBoard.getCell(x,y))) {
                            if (contradiction==null) { // is a contradiction
                                return "Incorrect use of Finish with Black, your answer leads to a contradiction.";
                            } else {
                                currentBoard = (FillapixBoard) caseBoard;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean doDefaultApplication(TreeTransition transition) {return false; }

    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex) {return false; }
}