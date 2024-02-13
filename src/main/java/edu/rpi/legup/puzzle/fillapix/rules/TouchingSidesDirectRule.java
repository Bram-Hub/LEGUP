package edu.rpi.legup.puzzle.fillapix.rules;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TouchingSidesDirectRule extends DirectRule {
  public TouchingSidesDirectRule() {
    super(
        "FPIX-BASC-0004",
        "Touching Sides",
        "Clues with touching sides have the same difference in black cells in their unshared regions as the difference in their numbers",
        "edu/rpi/legup/images/fillapix/rules/TouchingSides.png");
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
    /* remove any number cell that does not have another number cell adjacent
     * to it on the opposite side of the modified cell */
    Iterator<FillapixCell> itr = adjCells.iterator();
    while (itr.hasNext()) {
      // calculate x and y offset of adjCell from cell
      FillapixCell adjCell = itr.next();
      int xOffset = adjCell.getLocation().x - cell.getLocation().x;
      int yOffset = adjCell.getLocation().y - cell.getLocation().y;

      boolean found = false;
      // check vertically for numbered cell in opposite direction of cell
      if (adjCell.getLocation().x + xOffset >= 0
          && adjCell.getLocation().x < parentBoard.getWidth()) {
        int adjNum =
            parentBoard
                .getCell(adjCell.getLocation().x + xOffset, adjCell.getLocation().y)
                .getNumber();
        if (adjNum >= 0 && adjNum < 10) {
          found = true;
        }
      }
      // check horizontally for numbered cell in opposite direction of cell
      if (adjCell.getLocation().y + yOffset >= 0
          && adjCell.getLocation().y < parentBoard.getHeight()) {
        int adjNum =
            parentBoard
                .getCell(adjCell.getLocation().x, adjCell.getLocation().y + yOffset)
                .getNumber();
        if (adjNum >= 0 && adjNum < 10) {
          found = true;
        }
      }

      // if no horizontally or vertically adjacent cell on opposite side of 'cell' has number,
      // then adjCell is not valid, so should be removed
      if (!found) {
        itr.remove();
      }
    }

    // change the cell to the opposite color
    if (cell.getType() == FillapixCellType.BLACK) {
      parentCell.setCellType(FillapixCellType.WHITE);
    } else {
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
