package puzzles.starbattle.rules;

import edu.rpi.legup.puzzle.starbattle.rules.TooManyStarsContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TooManyStarsContradictionRuleTests {
    private static final TooManyStarsContradictionRule RULE = new TooManyStarsContradictionRule();
    private static StarBattle starBattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starBattle = new StarBattle();
    }

    /* Tests the Too Many Stars contradiction rule where a region has
    more stars than the puzzle number */
    @Test
    public void TooManyStarsContradictionRule_RegionOverloaded()
            throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooManyStarsContradictionRule/RegionOverloaded", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(2,1);
        StarBattleCell cell2 = board.getCell(0,2);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }

    /* Tests the Too Many Stars contradiction rule where a column has
    more stars than the puzzle number */
    @Test
    public void TooManyStarsContradictionRule_ColumnOverloaded()
            throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooManyStarsContradictionRule/ColumnOverloaded", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0);
        StarBattleCell cell2 = board.getCell(0,3);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }
    /* Tests the Too Many Stars contradiction rule where a row has
    more stars than the puzzle number */
    @Test
    public void TooManyStarsContradictionRule_RowOverloaded()
            throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooManyStarsContradictionRule/RowOverloaded", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0);
        StarBattleCell cell2 = board.getCell(3,0);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }
    /* Tests the Too Many Stars contradiction rule where it is used incorrectly */
    @Test
    public void TooManyStarsContradictionRule_Correct()
            throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooManyStarsContradictionRule/Correct", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));

            }
        }
    }
}
