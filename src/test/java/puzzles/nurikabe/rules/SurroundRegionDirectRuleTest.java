package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.rules.SurroundRegionDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class SurroundRegionDirectRuleTest {

    private static final SurroundRegionDirectRule RULE = new SurroundRegionDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Surround Region direct rule for a white square in the middle of the board
     */
    @Test
    public void SurroundRegionDirectRule_SurroundRegionBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/SurroundRegionDirectRule/SurroundRegionBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(1, 0);
        cell1.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell1);
        NurikabeCell cell2 = board.getCell(0, 1);
        cell2.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell2);
        NurikabeCell cell3 = board.getCell(2, 1);
        cell3.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell3);
        NurikabeCell cell4 = board.getCell(1, 2);
        cell4.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the Surround Region direct rule for a white square in the corner of the board
     */
    @Test
    public void SurroundRegionDirectRule_SurroundRegionBlackInCornerTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/SurroundRegionDirectRule/SurroundRegionBlackInCorner", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(1, 0);
        cell1.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell1);
        NurikabeCell cell2 = board.getCell(0, 1);
        cell2.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }


    /**
     * Tests the Surround Region direct rule for a false application of the rule
     */
    @Test
    public void SurroundRegionDirectRule_FalseSurroundRegionBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/SurroundRegionDirectRule/FalseSurroundRegion", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
            }
        }
    }


}
