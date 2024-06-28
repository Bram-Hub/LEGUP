package puzzles.starbattle.rules;

import edu.rpi.legup.puzzle.starbattle.rules.ClashingOrbitContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.rules.TooFewStarsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TooFewStarsContradictionRuleTest {
    private static final TooFewStarsContradictionRule RULE = new TooFewStarsContradictionRule();
    private static StarBattle starBattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starBattle = new StarBattle();
    }

    /*Too few stars in column */
    @Test
    public void TooFewStarsContradictionRule_Column()
    throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooFewStarsContradictionRule/Column", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0);
        StarBattleCell cell2 = board.getCell(0,1);
        StarBattleCell cell3 = board.getCell(0,2);
        StarBattleCell cell4 = board.getCell(0,3);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }

    }

    /*Too few stars in row*/
    @Test
    public void TooFewStarsContradictionRule_Row()
    throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooFewStarsContradictionRule/Row", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0);
        StarBattleCell cell2 = board.getCell(1,0);
        StarBattleCell cell3 = board.getCell(2,0);
        StarBattleCell cell4 = board.getCell(3,0);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }

    /*Too few stars in region*/
    @Test
    public void TooFewStarsContradictionRule_Region()
    throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooFewStarsContradictionRule/Region", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0);
        StarBattleCell cell2 = board.getCell(0,1);
        StarBattleCell cell3 = board.getCell(1,0);
        StarBattleCell cell4 = board.getCell(1,1);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }

    /*False contradiction*/
    @Test
    public void TooFewStarsContradictionRule_FalseContradiction()
    throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooFewStarsContradictionRule/FalseContradiction", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();

        Assert.assertNotNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
            }
        }
    }

    @Test
    public void TooFewStarsContradictionRule_NotEnoughSpace()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooFewStarsContradictionRule/NotEnoughSpace", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
            }
        }
    }

    @Test
    public void TooFewStarsContradictionRule_TwoStarColumn()
        throws InvalidFileFormatException {

        TestUtilities.importTestBoard("puzzles/starbattle/rules/TooFewStarsContradictionRule/TwoStarColumn", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0);
        StarBattleCell cell2 = board.getCell(0,1);
        StarBattleCell cell3 = board.getCell(0,2);
        StarBattleCell cell4 = board.getCell(0,3);

        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }

}
