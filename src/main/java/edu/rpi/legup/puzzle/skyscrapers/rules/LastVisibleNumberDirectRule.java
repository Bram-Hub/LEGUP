package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;
import java.util.ArrayList;

public class LastVisibleNumberDirectRule extends DirectRule {

  public LastVisibleNumberDirectRule() {
    super(
        "SKYS-BASC-0005",
        "Last Visible Number",
        "There is only one number for this cell that does not create a visibility contradiction",
        "edu/rpi/legup/images/skyscrapers/rules/OneEdge.png");
  }

  /**
   * Checks whether the child node logically follows from the parent node at the specific
   * puzzleElement index using this rule
   *
   * @param transition transition to check
   * @param puzzleElement index of the puzzleElement
   * @return null if the child node logically follow from the parent node at the specified
   *     puzzleElement, otherwise error message
   */
  @Override
  public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
    // last number for cell based upon preemptive visibility rules
    SkyscrapersBoard initialBoard = (SkyscrapersBoard) transition.getParents().get(0).getBoard();
    SkyscrapersCell initCell = (SkyscrapersCell) initialBoard.getPuzzleElement(puzzleElement);
    SkyscrapersBoard finalBoard = (SkyscrapersBoard) transition.getBoard();
    SkyscrapersCell finalCell = (SkyscrapersCell) finalBoard.getPuzzleElement(puzzleElement);
    if (initCell.getType() != SkyscrapersType.UNKNOWN
        || finalCell.getType() != SkyscrapersType.Number) {
      return super.getInvalidUseOfRuleMessage()
          + ": Modified cells must transition from unknown to number";
    }

    // set all rules used by case rule to false except for dupe, get all cases
    boolean dupeTemp = initialBoard.getDupeFlag();
    boolean viewTemp = initialBoard.getViewFlag();
    initialBoard.setDupeFlag(false);
    initialBoard.setViewFlag(true);
    NumberForCellCaseRule caseRule = new NumberForCellCaseRule();
    ArrayList<Board> candidates = caseRule.getCases(initialBoard, puzzleElement);
    initialBoard.setDupeFlag(dupeTemp);
    initialBoard.setViewFlag(viewTemp);

    // check if given value is the only remaining value
    if (candidates.size() == 1) {
      if (candidates.get(0).getPuzzleElement(puzzleElement).getData() == finalCell.getData()) {
        return null;
      }
      return super.getInvalidUseOfRuleMessage() + ": Wrong number in the cell.";
    }
    return super.getInvalidUseOfRuleMessage() + ":This cell is not forced.";
  }

  private boolean isForced(SkyscrapersBoard board, SkyscrapersCell cell) {
    SkyscrapersBoard emptyCase = board.copy();
    emptyCase.getPuzzleElement(cell).setData(SkyscrapersType.UNKNOWN.value);
    DuplicateNumberContradictionRule duplicate = new DuplicateNumberContradictionRule();
    if (duplicate.checkContradictionAt(emptyCase, cell) == null) {
      System.out.println("no contradiction ln");
      return true;
    }
    return false;
  }

  /**
   * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
   *
   * @param node tree node used to create default transition board
   * @return default board or null if this rule cannot be applied to this tree node
   */
  @Override
  public Board getDefaultBoard(TreeNode node) {
    SkyscrapersBoard initialBoard = (SkyscrapersBoard) node.getBoard();
    SkyscrapersBoard lightUpBoard = (SkyscrapersBoard) node.getBoard().copy();
    // System.out.println(lightUpBoard.getPuzzleElements().size());
    for (PuzzleElement element : lightUpBoard.getPuzzleElements()) {
      SkyscrapersCell cell = (SkyscrapersCell) element;
      if (cell.getType() == SkyscrapersType.UNKNOWN && isForced(initialBoard, cell)) {
        // cell.setData(SkyscrapersType.BULB.value);
        lightUpBoard.addModifiedData(cell);
      }
    }
    if (lightUpBoard.getModifiedData().isEmpty()) {
      return null;
    } else {
      return lightUpBoard;
    }
  }
}
