//package puzzles.starbattle.rules;
//
//import edu.rpi.legup.model.tree.TreeNode;
//import edu.rpi.legup.model.tree.TreeTransition;
//import edu.rpi.legup.puzzle.starbattle.StarBattle;
//import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
//import edu.rpi.legup.puzzle.starbattle.rules.RegionsWithinColumnsDirectRule;
//import edu.rpi.legup.save.InvalidFileFormatException;
//import legup.MockGameBoardFacade;
//import legup.TestUtilities;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.awt.*;
//
//public class RegionsWithinColumnsDirectRuleTest {
//
//    private static final RegionsWithinColumnsDirectRule RULE = new RegionsWithinColumnsDirectRule();
//    private static StarBattle starbattle;
//
//    @BeforeClass
//    public static void setUp() {
//        MockGameBoardFacade.getInstance();
//        starbattle = new StarBattle();
//    }
//
//    @Test
//    public void RegionsWithinColumnsDirectRule_OneRegionOneCell()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/OneRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(0,2);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(0,2);
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
//    public void RegionsWithinColumnsDirectRule_OneRegionTwoCells()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/OneRegionTwoCells", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(0,1);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(0,2);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(0,1);
//        Point location2 = new Point(0,2);
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
//    public void RegionsWithinColumnsDirectRule_PartialRegionOneCell()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/PartialRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(0,2);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(0,2);
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
//    public void RegionsWithinColumnsDirectRule_TwoRegionsOneCell()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/TwoRegionsOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(1,3);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(1,3);
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
//    public void RegionsWithinColumnsDirectRule_TwoRegionsTwoCells()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/TwoRegionsTwoCells", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,1);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(1,3);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(1,1);
//        Point location2 = new Point(1,3);
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
//    public void RegionsWithinColumnsDirectRule_TwoRegionsTwoCells2()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/TwoRegionsTwoCells2", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(0,3);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(1,3);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(0,3);
//        Point location2 = new Point(1,3);
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
//    public void RegionsWithinColumnsDirectRule_TwoRegionsStarOverlap()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/TwoRegionsStarOverlap", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(1,1);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(1,1);
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
//    public void RegionsWithinColumnsDirectRule_FalseRegionsWithinColumns1()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/OneRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(0,2);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//        StarBattleCell cell2 = board.getCell(1,2);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        Point location = new Point(0,2);
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
//    public void RegionsWithinColumnsDirectRule_FalseRegionsWithinColumns2()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/OneRegionOneCell", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(0,2);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//        StarBattleCell cell2 = board.getCell(0,1);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        Point location = new Point(0,2);
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
//    public void RegionsWithinColumnsDirectRule_FalseRegionsWithinColumns3()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/FalseStarOverlap", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(1,1);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//        StarBattleCell cell2 = board.getCell(1,3);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
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
//    @Test
//    public void RegionsWithinColumnsDirectRule_FalseRegionsWithinColumns4()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/RegionsWithinColumnsDirectRule/TwoRegionsTwoCells2", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell = board.getCell(0,3);
//        cell.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(0,3);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k,i);
//                if (point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//                else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
//    }
//}
