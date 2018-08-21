package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;

import java.util.ArrayList;

public class FinishWithWhiteBasicRule extends BasicRule
{
    public FinishWithWhiteBasicRule()
    {
        super("Finish with White",
                "The remaining unknowns around and on a cell must be white to satisfy the number",
                "edu/rpi/legup/images/fillapix/rules/FinishWithWhite.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        FillapixBoard fillapixBoard = (FillapixBoard) transition.getBoard();
        int width = fillapixBoard.getWidth();
        int height = fillapixBoard.getHeight();
        FillapixCell cell = (FillapixCell) fillapixBoard.getPuzzleElement(puzzleElement);

        BlackOrWhiteCaseRule blackOrWhite = new BlackOrWhiteCaseRule();
        TooManyBlackCellsContradictionRule tooManyBlackCells = new TooManyBlackCellsContradictionRule();

        FillapixBoard currentBoard = (FillapixBoard) transition.getParents().get(0).getBoard();
        // See note in Finish with Black because the same thing applies here for this method
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                int x = cell.getLocation().x + i;
                int y = cell.getLocation().y + j;
                if(x > -1 && x < width && y > -1 && y < height)
                {
                    // boolean parentCellUnknown = ((FillapixBoard) transition.getParents().getBoard()).getCell(x,y).isUnknown();
                    // if (parentCellUnknown && !fillapixBoard.getCell(x,y).isWhite()) {
                    //     return "All the changes you made must be white to apply this rule.";
                    // }

                    FillapixCell curCell = currentBoard.getCell(x, y);
                    ArrayList<Board> cases = blackOrWhite.getCases(currentBoard, curCell);
                    for(Board caseBoard : cases)
                    {
                        String contradiction = tooManyBlackCells.checkContradictionAt((FillapixBoard) caseBoard, x * width + y);
                        FillapixCell caseCell = ((FillapixBoard) caseBoard).getCell(x, y);
                        if(caseCell.hasSameState(fillapixBoard.getCell(x, y)))
                        {
                            if(contradiction == null)
                            { // is a contradiction
                                return "Incorrect use of Finish with Black, your answer leads to a contradiction.";
                            }
                            else
                            {
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
    public boolean doDefaultApplication(TreeTransition transition)
    {
        return false;
    }

    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
    }
}