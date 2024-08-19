//package puzzles.starbattle.rules;
//
//import edu.rpi.legup.model.tree.TreeNode;
//import edu.rpi.legup.model.tree.TreeTransition;
//import edu.rpi.legup.puzzle.starbattle.StarBattle;
//import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
//import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
//import edu.rpi.legup.puzzle.starbattle.rules.ColumnsWithinRowsDirectRule;
//import edu.rpi.legup.save.InvalidFileFormatException;
//import legup.MockGameBoardFacade;
//import legup.TestUtilities;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.awt.*;
//
//public class ColumnsWithinRowsDirectRuleTest {
//
//    private static final ColumnsWithinRowsDirectRule RULE = new ColumnsWithinRowsDirectRule();
//    private static StarBattle starbattle;
//
//    @BeforeClass
//    public static void setUp() {
//        MockGameBoardFacade.getInstance();
//        starbattle = new StarBattle();
//    }
//
//    @Test
//    public void ColumnsWithinRowsDirectRule_OneColumn()
//        throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/OneColumn", starbattle);
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
//        Point location1 = new Point(1, 0);
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
//    public void ColumnsWithinRowsDirectRule_TwoColumns()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/TwoColumns", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(2,1);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(2,0);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(2, 1);
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
//    public void ColumnsWithinRowsDirectRule_NonAdjacentColumns()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/NonAdjacentColumns", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,0);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(1,2);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(1, 0);
//        Point location2 = new Point(1,2);
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
//    public void ColumnsWithinRowsDirectRule_TwoColumnsStarOverlap()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/TwoColumnsStarOverlap", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(2,1);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//        StarBattleCell cell2 = board.getCell(3,0);
//        cell2.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell2);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(2, 1);
//        Point location2 = new Point(3,0);
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
//    public void ColumnsWithinRowsDirectRule_FalseColumnsWithinRows1()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/OneColumn", starbattle);
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
//        StarBattleCell cell3 = board.getCell(1,1);
//        cell3.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell3);
//
//        Assert.assertNotNull(RULE.checkRule(transition));
//
//        Point location1 = new Point(1, 0);
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
//    public void ColumnsWithinRowsDirectRule_PartialCover()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/OneColumn", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,0);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(1,0);
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
//
//    @Test
//    public void ColumnsWithinRowsDirectRule_FalseColumnsWithinRows3()
//            throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRowsDirectRule/FalseStarOverlap", starbattle);
//        TreeNode rootNode = starbattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,0);
//        cell1.setData(StarBattleCellType.BLACK.value);
//        board.addModifiedData(cell1);
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
