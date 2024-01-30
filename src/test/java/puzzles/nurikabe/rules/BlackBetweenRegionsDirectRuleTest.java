package puzzles.nurikabe.rules;

//import javafx.scene.layout.Pane;

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
import edu.rpi.legup.puzzle.nurikabe.rules.BlackBetweenRegionsDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class BlackBetweenRegionsDirectRuleTest {

    private static final BlackBetweenRegionsDirectRule RULE = new BlackBetweenRegionsDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Black Between Regions direct rule for regions that are diagonal to each other (diagonal going from top left to bottom right)
     */
    @Test
    public void BlackBetweenRegionsDirectRule_DiagonalBlackBetweenRegions1Test() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsDirectRule/DiagonalBlackBetweenRegions1", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell1 = board.getCell(2, 1);
        cell1.setData(NurikabeType.BLACK.toValue());
        NurikabeCell cell2 = board.getCell(1, 2);
        cell2.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell1);
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
     * Tests the Black Between Regions direct rule for regions that are diagonal to each other (diagonal going from bottom left to top right)
     */
    @Test
    public void BlackBetweenRegionsDirectRule_DiagonalBlackBetweenRegions2Test() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsDirectRule/DiagonalBlackBetweenRegions2", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell1 = board.getCell(1, 1);
        cell1.setData(NurikabeType.BLACK.toValue());
        NurikabeCell cell2 = board.getCell(2, 2);
        cell2.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell1);
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
     * Tests the Black Between Regions direct rule for regions that are horizontally opposite each other
     */
    @Test
    public void BlackBetweenRegionsDirectRule_HorizontalBlackBetweenRegionsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsDirectRule/HorizontalBlackBetweenRegions", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell = board.getCell(1, 1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the Black Between Regions direct rule for regions that are vertically opposite each other
     */
    @Test
    public void BlackBetweenRegionsDirectRule_VerticalBlackBetweenRegionsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsDirectRule/VerticalBlackBetweenRegions", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell = board.getCell(1, 1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the Black Between Regions direct rule for a false application of the rule, where a black tile is enclosed by one region
     */
    @Test
    public void BlackBetweenRegionsDirectRule_FalseBlackBetweenRegionsTest() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsDirectRule/FalseBlackBetweenRegions",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1,1);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
            }
        }
    }
}
