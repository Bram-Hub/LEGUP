package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;

import java.awt.*;

public class TooManyBlackCellsContradictionRule extends ContradictionRule
{

    public TooManyBlackCellsContradictionRule()
    {
        super("Too Many Black Cells",
                "There may not be more black cells than the number",
                "edu/rpi/legup/images/fillapix/contradictions/TooManyBlackCells.png");
    }

    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement) {
        FillapixBoard fillapixBoard = (FillapixBoard) transition.getBoard();
        FillapixCell cell = (FillapixCell) fillapixBoard.getPuzzleElement(puzzleElement);

        Point loc = cell.getLocation();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(loc.x + i, loc.y + j);
                if (adjCell != null) {
                    int cellNum = adjCell.getNumber();
                    if (cellNum >= 0) {
                        int numBlackCells = fillapixBoard.getNumCells(adjCell, FillapixCellType.BLACK);
                        if (numBlackCells > cellNum) {
                            return null;
                        }
                    }
                }
            }
        }
        return "Board does not contain a contradiction";
    }
}
