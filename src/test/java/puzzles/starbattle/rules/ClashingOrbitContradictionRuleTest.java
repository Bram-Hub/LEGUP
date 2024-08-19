//package puzzles.starbattle.rules;
//
//import edu.rpi.legup.puzzle.starbattle.rules.ClashingOrbitContradictionRule;
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
//public class ClashingOrbitContradictionRuleTest {
//
//    private static final ClashingOrbitContradictionRule RULE = new ClashingOrbitContradictionRule();
//    private static StarBattle starBattle;
//
//    @BeforeClass
//    public static void setUp() {
//        MockGameBoardFacade.getInstance();
//        starBattle = new StarBattle();
//    }
//
//    /*Tests the Clashing Orbit contradiction rule for directly adjacent stars not at the
//    edge of the board */
//    @Test
//    public void ClashingOrbitContradictionRule_DirectlyAdjacentCenter()
//        throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ClashingOrbitContradictionRule/DirectlyAdjacentCenter", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,1);
//        StarBattleCell cell2 = board.getCell(2,1);
//
//        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
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
//    /* Tests the Clashing Orbit contradiction rule for diagonally adjacent stars */
//    @Test
//    public void ClashingOrbitContradictionRule_DiagonallyAdjacent()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ClashingOrbitContradictionRule/DiagonallyAdjacent", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(1,1);
//        StarBattleCell cell2 = board.getCell(2,2);
//
//        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
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
//    /*Tests the Clashing Orbit contradiction rule for stars at the edge of the board */
//    @Test
//    public void ClashingOrbitContradictionRule_DirectlyAdjacentEdge()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ClashingOrbitContradictionRule/DirectlyAdjacentEdge", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//        StarBattleCell cell1 = board.getCell(0,0);
//        StarBattleCell cell2 = board.getCell(1,0);
//
//        Assert.assertNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
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
//    /*Tests the Clashing Orbit contradiction rule for a false contradiction. */
//    @Test
//    public void ClashingOrbitContradictionRule_FalseContradiction()
//            throws InvalidFileFormatException
//    {
//        TestUtilities.importTestBoard("puzzles/starbattle/rules/ClashingOrbitContradictionRule/FalseContradiction", starBattle);
//        TreeNode rootNode = starBattle.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
//
//        Assert.assertNotNull(RULE.checkContradiction((StarBattleBoard) transition.getBoard()));
//
//        for (int i = 0; i < board.getHeight(); ++i) {
//            for (int j = 0; j < board.getWidth(); ++j) {
//                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
//            }
//        }
//    }
//}
