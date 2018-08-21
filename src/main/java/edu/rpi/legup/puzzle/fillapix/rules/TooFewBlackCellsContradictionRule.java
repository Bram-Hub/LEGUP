package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;

public class TooFewBlackCellsContradictionRule extends ContradictionRule
{

    public TooFewBlackCellsContradictionRule()
    {
        super("Too Few Black Cells",
                "There may not be fewer black cells than the number.",
                "edu/rpi/legup/images/fillapix/contradictions/TooFewBlackCells.png");
    }

    @Override
    public String checkContradiction(TreeTransition transition)
    {
        FillapixBoard fillapixBoard = (FillapixBoard) transition.getBoard();
        int rowSize = fillapixBoard.getWidth();
        int colSize = fillapixBoard.getHeight();
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                FillapixCell cell = fillapixBoard.getCell(i,j);
                if (cell.getData() != -1) {
                    int numBlackCells = fillapixBoard.getNumCells(cell, FillapixCell.BLACK);
                    int numUnknownCells = fillapixBoard.getNumCells(cell, FillapixCell.UNKNOWN);
                    if (numBlackCells+numUnknownCells < cell.getData()) {
                        return null;
                    }
                }
            }
        }
        return "Board does not contain a contradiction";
    }

    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        FillapixBoard fillapixBoard = (FillapixBoard) transition.getBoard();
        int width = fillapixBoard.getWidth();
        FillapixCell cell = (FillapixCell) fillapixBoard.getPuzzleElement(puzzleElement);
        if (cell.getData() != -1) {
            int numBlackCells = fillapixBoard.getNumCells(cell, FillapixCell.BLACK);
            int numUnknownCells = fillapixBoard.getNumCells(cell, FillapixCell.UNKNOWN);
            if (numBlackCells+numUnknownCells < cell.getData()) {
                return null;
            }
        }
        return "Board does not contain a contradiction";
    }

    public String checkContradictionAt(FillapixBoard fillapixBoard, int elementIndex)
    {
        int width = fillapixBoard.getWidth();
        FillapixCell cell = fillapixBoard.getCell(elementIndex%width,elementIndex/width);
        if (cell.getData() != -1) {
            int numBlackCells = fillapixBoard.getNumCells(cell, FillapixCell.BLACK);
            int numUnknownCells = fillapixBoard.getNumCells(cell, FillapixCell.UNKNOWN);
            if (numBlackCells+numUnknownCells < cell.getData()) {
                return null;
            }
        }
        return "Board does not contain a contradiction";
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
