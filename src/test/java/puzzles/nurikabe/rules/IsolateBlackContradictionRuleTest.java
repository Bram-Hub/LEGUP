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
import edu.rpi.legup.puzzle.nurikabe.rules.IsolateBlackContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class IsolateBlackContradictionRuleTest {

    private static final IsolateBlackContradictionRule RULE = new IsolateBlackContradictionRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Isolate Black contradiction rule for a black square in the corner, separated by a diagonal of white squares
     */
    @Test
    public void IsolateBlackContradictionRule_SimpleIsolateBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/IsolateBlackContradictionRule/SimpleIsolateBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(0,0);
        NurikabeCell cell2 = board.getCell(2,2);

        Assert.assertNull(RULE.checkContradiction((NurikabeBoard)transition.getBoard()));

        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k,i)));
                }
            }
        }
    }

    /**
     * Tests the Isolate Black contradiction rule for diagonally connected black squares
     */
    @Test
    public void IsolateBlackContradictionRule_DiagonalBlackTest() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/IsolateBlackContradictionRule/DiagonalIsolateBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell1 = board.getCell(0,0);
        NurikabeCell cell2 = board.getCell(1,1);
        NurikabeCell cell3 = board.getCell(2,2);

        Assert.assertNull(RULE.checkContradiction((NurikabeBoard)transition.getBoard()));

        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k,i)));
                }
            }
        }
    }

    /**
     * Tests the Isolate Black contradiction rule for a false contradiction
     */
    @Test
    public void IsolateBlackContradictionRule_FalseIsolateBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/IsolateBlackContradictionRule/FalseIsolateBlack",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        Assert.assertNotNull(RULE.checkRule(transition));
        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
            }
        }
    }
}
