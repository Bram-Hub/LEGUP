package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import java.util.List;

public class SegmentTypeCaseRule extends CaseRule {
  public SegmentTypeCaseRule() {
    super(
        "BTSP-CASE-0001",
        "Segment Type",
        "",
        "edu/rpi/legup/images/battleship/cases/SegmentType.png");
  }

  /**
   * Checks whether the {@link TreeTransition} logically follows from the parent node using this
   * rule. This method is the one that should overridden in child classes.
   *
   * @param transition transition to check
   * @return null if the child node logically follow from the parent node, otherwise error message
   */
  @Override
  public String checkRuleRaw(TreeTransition transition) {
    return null;
  }

  /**
   * Checks whether the child node logically follows from the parent node at the specific
   * puzzleElement index using this rule. This method is the one that should overridden in child
   * classes.
   *
   * @param transition transition to check
   * @param puzzleElement equivalent puzzleElement
   * @return null if the child node logically follow from the parent node at the specified
   *     puzzleElement, otherwise error message
   */
  @Override
  public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
    return null;
  }

  /**
   * Gets the case board that indicates where this case rule can be applied on the given {@link
   * Board}.
   *
   * @param board board to find locations where this case rule can be applied
   * @return a case board
   */
  @Override
  public CaseBoard getCaseBoard(Board board) {
    return null;
  }

  /**
   * Gets the possible cases for this {@link Board} at a specific {@link PuzzleElement} based on
   * this case rule.
   *
   * @param board the current board state
   * @param puzzleElement equivalent puzzleElement
   * @return a list of elements the specified could be
   */
  @Override
  public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
    return null;
  }
}
