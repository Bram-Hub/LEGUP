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

public class MirrorDirectRule extends DirectRule {
    public MirrorDirectRule() {
        super("FPIX-BASC-0003",
                "Mirror",
                "Two adjacent clues with the same value must have the same number of black squares in their unshared regions",
                "edu/rpi/legup/images/fillapix/rules/Mirror.png");
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

        // find all cells adjacent to cell that are numbered
        ArrayList<FillapixCell> adjCells = FillapixUtilities.getAdjacentCells(parentBoard, parentCell);
        ArrayList<FillapixCell> adjNums = new ArrayList<FillapixCell>();
        for (int i=0; i < adjCells.size(); i++) {
            if ((adjCells.get(i)).getNumber() >= 0 && adjCells.get(i).getNumber() < 10) {
                adjNums.add(adjCells.get(i));
            }
        }
        // the numbered cells must be next to another numbered cell of the same value
        Iterator<FillapixCell> itr = adjNums.iterator();
        while (itr.hasNext()) {
            FillapixCell adjNum = itr.next();
            adjCells = FillapixUtilities.getAdjacentCells(parentBoard, adjNum);
            boolean found = false;
            for (FillapixCell adjCell : adjCells) {
                if (adjCell.getNumber() == adjNum.getNumber() && adjCell.getIndex() != adjNum.getIndex()) {
                    found = true;
                }
            }
            if (!found) {
                itr.remove();
            }
        }

        // change the color of the  parentCell, and check if there exists a valid board
        if (cell.getType() == FillapixCellType.BLACK) {
            parentCell.setType(FillapixCellType.WHITE);
        } else {
            parentCell.setType(FillapixCellType.BLACK);
        }
        parentBoard.addModifiedData(parentCell);
        CaseRule completeClue = new CompleteClueCaseRule();
        List<Board> caseBoards;
        for (FillapixCell adjNum : adjNums) {
            caseBoards = completeClue.getCases(parentBoard, adjNum);
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