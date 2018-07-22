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
import puzzle.nurikabe.rules.PreventBlackSquareBasicRule;
import save.InvalidFileFormatException;

public class PreventBlackSquareBasicRuleTest
{

    private static final PreventBlackSquareBasicRule RULE = new PreventBlackSquareBasicRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void PreventBlackSquareBasicRule_BottomLeftWhiteBlackSquareTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/PreventBlackSquareBasicRule/BottomLeftWhiteBlackSquare", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(0,1);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell.getIndex())
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }
    }

    @Test
    public void PreventBlackSquareBasicRule_BottomRightWhiteBlackSquareTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/PreventBlackSquareBasicRule/BottomRightWhiteBlackSquare", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1,1);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell.getIndex())
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }
    }

    @Test
    public void PreventBlackSquareBasicRule_TopLeftWhiteBlackSquareTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/PreventBlackSquareBasicRule/TopLeftWhiteBlackSquare", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(0,0);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell.getIndex())
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }
    }

    @Test
    public void PreventBlackSquareBasicRule_TopRightWhiteBlackSquareTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/PreventBlackSquareBasicRule/TopRightWhiteBlackSquare", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(1,0);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell.getIndex())
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
