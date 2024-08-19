//package puzzles.starbattle.rules;
//
//import edu.rpi.legup.puzzle.starbattle.rules.FinishWithStarsDirectRule;
//import edu.rpi.legup.model.tree.TreeNode;
//import edu.rpi.legup.model.tree.TreeTransition;
//import edu.rpi.legup.puzzle.starbattle.StarBattle;
//import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
//import edu.rpi.legup.save.InvalidFileFormatException;
//import java.awt.*;
//import legup.MockGameBoardFacade;
//import legup.TestUtilities;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class FinishWithStarsDirectRuleTest {
//    private static final FinishWithStarsDirectRule RULE = new FinishWithStarsDirectRule();
//    private static StarBattle starBattle;
//
//    @BeforeClass
//    public static void setUp() {
//        MockGameBoardFacade.getInstance();
//        starBattle = new StarBattle();
//    }
//
//    /* Finish With Stars Direct Rule where star is in the corner and only the row is blacked out */
//    @Test
//    public void FinishWithStarsDirectRuleTestCornerRow()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/FinishWithStarsDirectRule/CornerRow", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(0,0);
//        cell1.setData(StarBattleCellType.STAR.value);
//
//        board.addModifiedData(cell1);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Point point = new Point(j,i);
//                if (point.equals(cell1.getLocation())) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//                else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//            }
//        }
//    }
//
//    /* Finish With Stars Direct Rule where star is in the corner and only the column is blacked out */
//    @Test
//    public void FinishWithStarsDirectRuleTestCornerColumn()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/FinishWithStarsDirectRule/CornerColumn", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(0,0);
//        cell1.setData(StarBattleCellType.STAR.value);
//
//        board.addModifiedData(cell1);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Point point = new Point(j,i);
//                if (point.equals(cell1.getLocation())) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//                else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//            }
//        }
//    }
//
//    /* Finish With Stars Direct Rule where star is in the corner and only the column is blacked out */
//    @Test
//    public void FinishWithStarsDirectRuleTestCornerRowColumn()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/FinishWithStarsDirectRule/CornerRowColumn", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(0,0);
//        cell1.setData(StarBattleCellType.STAR.value);
//
//        board.addModifiedData(cell1);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Point point = new Point(j,i);
//                if (point.equals(cell1.getLocation())) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//                else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//            }
//        }
//    }
//    /* Finish With Stars Direct Rule where star is in the corner and only the column is blacked out */
//    @Test
//    public void FinishWithStarsDirectRuleTestRegion()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/FinishWithStarsDirectRule/Region", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,1);
//        cell1.setData(StarBattleCellType.STAR.value);
//
//        board.addModifiedData(cell1);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Point point = new Point(j,i);
//                if (point.equals(cell1.getLocation())) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//                else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//            }
//        }
//    }
//    /* Finish With Stars Direct Rule where there are two stars in two different mostly blacked out regions */
//    @Test
//    public void FinishWithStarsDirectRuleTestDoubleRegion()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/FinishWithStarsDirectRule/DoubleRegion", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,1);
//        cell1.setData(StarBattleCellType.STAR.value);
//        StarBattleCell cell2 = board.getCell(2,3);
//        cell2.setData(StarBattleCellType.STAR.value);
//
//        board.addModifiedData(cell1);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Point point = new Point(j,i);
//                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//                else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//                }
//            }
//        }
//    }
//
//    /* Finish With Stars Direct Rule where there are two stars in two different mostly blacked out regions */
//    @Test
//    public void FinishWithStarsDirectRuleTestFalse()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/FinishWithStarsDirectRule/False", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(2,0);
//        cell1.setData(StarBattleCellType.STAR.value);
//
//        board.addModifiedData(cell1);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//            }
//        }
//    }
//
//}
