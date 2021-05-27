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
import edu.rpi.legup.puzzle.nurikabe.rules.FillinWhiteBasicRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class FillinWhiteBasicRuleTest
{
    private static final FillinWhiteBasicRule RULE = new FillinWhiteBasicRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void FillinWhiteBasicRule_UnknownSurroundWhiteTest() throws InvalidFileFormatException
    {
//        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinWhiteBasicRule/UnknownSurroundWhite", nurikabe);
//        TreeNode rootNode = nurikabe.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
//        NurikabeCell cell = board.getCell(1,1);
//        cell.setData(NurikabeType.WHITE.toValue());
//        board.addModifiedData(cell);
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        Point location = new Point(1, 1);
//        for(int i = 0; i < board.getHeight(); i++) {
//            for(int k = 0; k < board.getWidth(); k++) {
//                Point point  = new Point(k, i);
//                if(point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
    }
}
