//package puzzles.starbattle.rules;
//
//import edu.rpi.legup.model.tree.TreeNode;
//import edu.rpi.legup.model.tree.TreeTransition;
//import edu.rpi.legup.puzzle.starbattle.StarBattle;
//import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
//import edu.rpi.legup.puzzle.starbattle.rules.RegionsWithinRowsDirectRule;
//import edu.rpi.legup.save.InvalidFileFormatException;
//import legup.MockGameBoardFacade;
//import legup.TestUtilities;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.awt.*;
//
//public class RegionsWithinRowsDirectRuleTest {
//
//    private static final RegionsWithinRowsDirectRule RULE = new RegionsWithinRowsDirectRule();
//    private static StarBattle starbattle;
//
//    @BeforeClass
//    public static void setUp() {
//        MockGameBoardFacade.getInstance();
//        starbattle = new StarBattle();
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_OneRegionOneCell()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/OneRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(2,0);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(2,0);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_PartialRegionOneCell()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/PartialRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(2,0);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(2,0);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_PartialRegionTwo()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/PartialRegionTwoCells", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,0);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(2,0);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(1,0);
//        Point location2 = new Point(2,0);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location1) || point.equals(location2)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_TwoRegionsOneCell()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/TwoRegionsOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(3,1);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(3,1);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_StarOverlap()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/StarOverlap", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(3,1);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(3,1);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_FalseRegionsWithinRows1()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/OneRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(2,0);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(2,1);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        Point location = new Point(2,0);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_FalseRegionsWithinRows2()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/OneRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(2,0);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(1,0);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        Point location = new Point(2,0);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void RegionsWithinRowsDirectRule_FalseStarOverlap()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinRowsDirectRule/FalseStarOverlap", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(3,1);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//            }
//        }
//    }
//
//}
