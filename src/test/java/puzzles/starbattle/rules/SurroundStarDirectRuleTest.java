package puzzles.starbattle.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.rules.SurroundStarDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SurroundStarDirectRuleTest {

    private static final SurroundStarDirectRule RULE = new SurroundStarDirectRule();
    private static StarBattle starbattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starbattle = new StarBattle();
    }

    @Test
    public void SurroundStarDirectRule_CenterStarOneTile() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/SurroundStarDirectRule/CenterStar", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(0,1);
        cell.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(0, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void SurroundStarDirectRule_CenterStarOneTileDiagonal() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/SurroundStarDirectRule/CenterStar", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(0,0);
        cell.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(0, 0);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void SurroundStarDirectRule_CenterStarAllTiles()
        throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/SurroundStarDirectRule/CenterStar", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (i != 1 || j != 1) {
                    StarBattleCell cell = board.getCell(i,j);
                    cell.setData(StarBattleCellType.BLACK.value);
                    board.addModifiedData(cell);
                }
            }
        }

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(1, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void SurroundStarDirectRule_CornerStar() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/SurroundStarDirectRule/CornerStar", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,1);
        cell1.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell1);
        StarBattleCell cell2 = board.getCell(1,0);
        cell2.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell2);
        StarBattleCell cell3 = board.getCell(1,1);
        cell3.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell3);

        Assert.assertNull(RULE.checkRule(transition));

        Point location1 = new Point(0, 1);
        Point location2 = new Point(1,0);
        Point location3 = new Point(1,1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location1) || point.equals(location2) || point.equals(location3)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void SurroundStarDirectRule_FalseSurroundStar()
        throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/SurroundStarDirectRule/CornerStar", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(2,0);
        cell.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }
}
