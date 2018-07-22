package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzle.nurikabe.Nurikabe;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;
import puzzle.nurikabe.rules.FillinBlackBasicRule;
import save.InvalidFileFormatException;

public class FillinBlackBasicRuleTest
{

    private static final FillinBlackBasicRule RULE = new FillinBlackBasicRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void FillinBlackBasicRule_UnknownSurroundBlackTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FillinBlackBasicRule/UnknownSurroundBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1,1);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == 4)
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }
    }
}
