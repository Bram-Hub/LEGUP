package edu.rpi.legup.puzzle.fillapix.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;

public class TouchingCornersDirectRule extends DirectRule {
    public TouchingCornersDirectRule() {
        super("FPIX-BASC-0005",
                "Touching Corners",
                "Clues with touching corners have the same difference in black cells in their unshared regions as the difference in their numbers",
                "edu/rpi/legup/images/fillapix/rules/TouchingCorners.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        FillapixBoard board = (FillapixBoard) transition.getBoard();
        FillapixBoard parentBoard = (FillapixBoard) transition.getParents().get(0).getBoard().copy();
        FillapixCell cell = (FillapixCell) board.getPuzzleElement(puzzleElement);
        FillapixCell parentCell = (FillapixCell) parentBoard.getPuzzleElement(puzzleElement);

        // cell has to have been empty before
        if (parentCell.getType() != FillapixCellType.UNKNOWN) {
            return super.getInvalidUseOfRuleMessage();
        }

        // parentBoard cannot have any contradictions
        if (FillapixUtilities.checkBoardForContradiction(parentBoard)) {
            return super.getInvalidUseOfRuleMessage();
        }

        // get all adjCells that have a number
        ArrayList<FillapixCell> adjCells = FillapixUtilities.getAdjacentCells(parentBoard, parentCell);
        adjCells.removeIf(x -> x.getNumber() < 0 || x.getNumber() >= 10);
        /* remove any number cell that does not have another number cell diagonally
         * adjacent to it on the opposite side of the modified cell */
        Iterator<FillapixCell> itr = adjCells.iterator();
        while (itr.hasNext()) {
            FillapixCell adjCell = itr.next();

            boolean found = false;
            ArrayList<FillapixCell> adjAdjCells = FillapixUtilities.getAdjacentCells(parentBoard, adjCell);
            for (FillapixCell adjAdjCell : adjAdjCells) {
                if (adjAdjCell.getLocation().x != adjCell.getLocation().x &&
                        adjAdjCell.getLocation().y != adjCell.getLocation().y &&
                        adjAdjCell.getNumber() >= 0 && adjAdjCell.getNumber() < 10 &&
                        adjAdjCell.getIndex() != parentCell.getIndex()) {
                    // adjAdjCell is diagonally adjacent to adjCell && it has a 
                    // number && it is not parentCell
                    found = true;
                }
            }

            // does not qualify for this rule
            if (!found) {
                itr.remove();
            }
        }

        // change the cell to the opposite color
        if (cell.getType() == FillapixCellType.BLACK) {
            parentCell.setCellType(FillapixCellType.WHITE);
        }
        else {
            parentCell.setCellType(FillapixCellType.BLACK);
        }
        // check for some contradiction in all cases
        parentBoard.addModifiedData(parentCell);
        CaseRule completeClue = new SatisfyClueCaseRule();
        List<Board> caseBoards;
        for (FillapixCell adjCell : adjCells) {
            caseBoards = completeClue.getCases(parentBoard, adjCell);
            boolean found = true;
            for (Board b : caseBoards) {
                if (!FillapixUtilities.checkBoardForContradiction((FillapixBoard) b)) {
                    found = false;
                }
            }
            if (found) {
                return null;
            }
        }

        return super.getInvalidUseOfRuleMessage();
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
       return null;
    }
}