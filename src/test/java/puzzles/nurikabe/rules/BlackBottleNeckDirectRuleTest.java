package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.rules.BlackBottleNeckDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import java.awt.*;


public class BlackBottleNeckDirectRuleTest {

    private static final BlackBottleNeckDirectRule RULE = new BlackBottleNeckDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Black BottleNeck direct rule for a bottleneck in the center of the board
     */
    @Test
    public void BlackBottleNeckDirectRule_TwoSurroundBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBottleNeckDirectRule/SimpleBlackBottleNeck", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
        NurikabeCell cell = board.getCell(2,1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < board.getHeight(); i++) {
            for(int k = 0; k < board.getWidth(); k++) {
                Point point  = new Point(k, i);
                if(point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }


    /**
     * Tests the Black BottleNeck direct rule for a bottleneck in the corner of the board
     */
    @Test
    public void BlackBottleNeckDirectRule_CornerBottleneck() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBottleNeckDirectRule/CornerBottleNeck", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
        NurikabeCell cell = board.getCell(0,1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < board.getHeight(); i++){
            for(int k = 0; k < board.getWidth(); k++){
                Point point  = new Point(k, i);
                if(point.equals(cell.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }


    /**
     * Tests the Black BottleNeck direct rule for a false bottleneck
     */
    @Test
    public void BlackBottleNeckDirectRule_FalseBottleneck() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBottleNeckDirectRule/FalseBottleNeck", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
        NurikabeCell cell = board.getCell(0,1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        for(int i = 0; i < board.getHeight(); i++){
            for(int k = 0; k < board.getWidth(); k++){
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }
}


