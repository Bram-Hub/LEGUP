package puzzles.battleship.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.battleship.Battleship;
import edu.rpi.legup.puzzle.battleship.BattleshipBoard;
import edu.rpi.legup.puzzle.battleship.rules.AdjacentShipsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdjacentShipsContradictionRuleTest {
  private static final AdjacentShipsContradictionRule RULE = new AdjacentShipsContradictionRule();

  private static Battleship battleship;

  @BeforeClass
  public static void setUp() {
    MockGameBoardFacade.getInstance();
    battleship = new Battleship();
  }

  @Test
  public void OrthogonalAdjacentTest() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/battleship/rules" + "/AdjacentShipsContradictionRule/OrthogonalAdjacentBoards",
        battleship);
    TreeNode rootNode = battleship.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    BattleshipBoard board = (BattleshipBoard) transition.getBoard();

    Assert.assertNotNull(RULE.checkContradiction(board));
  }

  @Test
  public void InvalidOrthogonalAdjacentTest() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/battleship/rules"
            + "/AdjacentShipsContradictionRule"
            + "/InvalidOrthogonalAdjacentBoards",
        battleship);
    TreeNode rootNode = battleship.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    BattleshipBoard board = (BattleshipBoard) transition.getBoard();

    Assert.assertNull(RULE.checkContradiction(board));
  }

  @Test
  public void DiagonalAdjacentTest() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/battleship/rules" + "/AdjacentShipsContradictionRule" + "/DiagonalAdjacentBoards",
        battleship);
    TreeNode rootNode = battleship.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    BattleshipBoard board = (BattleshipBoard) transition.getBoard();

    Assert.assertNull(RULE.checkContradiction(board));
  }
}
