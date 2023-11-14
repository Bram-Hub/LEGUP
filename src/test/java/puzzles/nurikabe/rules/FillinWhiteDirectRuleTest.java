package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.FillinWhiteDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;

import java.awt.*;


public class FillinWhiteDirectRuleTest {
    private static final FillinWhiteDirectRule RULE = new FillinWhiteDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Fillin White direct rule for a square surrounded by white in the middle of the board
     */
    @Test
    public void FillinWhiteDirectRule_UnknownSurroundWhiteTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinWhiteDirectRule/UnknownSurroundWhite", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1,1);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(1, 1);
        for(int i = 0; i < board.getHeight(); i++) {
            for(int k = 0; k < board.getWidth(); k++) {
                Point point  = new Point(k, i);
                if(point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the Fillin White direct rule for a square surrounded by white in the top left corner of the board
     */
    @Test
    public void FillinWhiteDirectRule_CornerTest1() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinWhiteDirectRule/CornerWhite1",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(0,0);
        cell.setData(NurikabeType.WHITE.toValue());
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
     * Tests the Fillin White direct rule for a square surrounded by white in the bottom right corner of the board
     */
    @Test
    public void FillinWhiteDirectRule_CornerTest2() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinWhiteDirectRule/CornerWhite2",nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(2,2);
        cell.setData(NurikabeType.WHITE.toValue());
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
     * Tests the Fillin White direct rule for a false application of the rule
     */
    @Test
    public void FillinWhiteDirectRule_FalseTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinWhiteDirectRule/FalseFillinWhite", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1,1);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        Point location = new Point(1, 1);
        for(int i = 0; i < board.getHeight(); i++) {
            for(int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
            }
        }
    }

}
