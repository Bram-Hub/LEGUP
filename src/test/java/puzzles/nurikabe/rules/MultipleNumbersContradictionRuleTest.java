package puzzles.nurikabe.rules;

import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.MultipleNumbersContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class MultipleNumbersContradictionRuleTest {

    private static final MultipleNumbersContradictionRule RULE = new MultipleNumbersContradictionRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Multiple Numbers contradiction rule for a single region with multiple numbers
     */
    @Test
    public void MultipleNumbersContradictionRule_TwoSurroundBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/MultipleNumbersContradictionRule/MultipleNumbers", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((NurikabeBoard) transition.getBoard()));

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(new Point(0, 0)) || point.equals(new Point(2, 0))) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the Multiple Numbers contradiction rule for a more complex regions with multiple numbers
     */
    @Test
    public void MultipleNumbersContradictionRule_ComplexRegion() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/MultipleNumbersContradictionRule/ComplexRegion",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(0,0);
        NurikabeCell cell2 = board.getCell(2,0);
        NurikabeCell cell3 = board.getCell(4,0);
        NurikabeCell cell4 = board.getCell(2,3);
        NurikabeCell cell5 = board.getCell(4,4);

        Assert.assertNull(RULE.checkContradiction((NurikabeBoard)transition.getBoard()));
        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) || point.equals(cell3.getLocation())
                   || point.equals(cell4.getLocation()) || point.equals(cell5.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
            }
        }
    }

    /**
     * Tests the Multiple Numbers contradiction rule for two regions with one number each, separated diagonally
     */
    @Test
    public void MultipleNumbersContradictionRule_FalseContradiction() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/MultipleNumbersContradictionRule/FalseContradiction", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkContradiction((NurikabeBoard) transition.getBoard()));

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
            }
        }
    }

}
