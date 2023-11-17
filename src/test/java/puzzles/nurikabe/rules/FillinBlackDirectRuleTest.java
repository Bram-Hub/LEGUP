package puzzles.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.rules.FillinBlackDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class FillinBlackDirectRuleTest {

    private static final FillinBlackDirectRule RULE = new FillinBlackDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Fillin Black direct rule for a square surrounded by black in the middle of the board
     */
    @Test
    public void FillinBlackDirectRule_UnknownSurroundBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinBlackDirectRule/UnknownSurroundBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1, 1);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(1, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the Fillin Black direct rule for a square surrounded by black in the top left corner of the board
     */
    @Test
    public void FillinBlackDirectRule_CornerTest1() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinBlackDirectRule/CornerBlack1",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(0,0);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));
        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(cell.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
            }
        }
    }

    /**
     * Tests the Fillin Black direct rule for a square surrounded by black in the bottom right corner of the board
     */
    @Test
    public void FillinBlackDirectRule_CornerTest2() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinBlackDirectRule/CornerBlack2",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(2,2);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));
        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(cell.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
            }
        }
    }

    /**
     * Tests the Fillin Black direct rule for a false application of the rule
     */
    @Test
    public void FillinBlackDirectRule_FalseTest() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinBlackDirectRule/FalseFillinBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1, 1);
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
