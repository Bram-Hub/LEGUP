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
import edu.rpi.legup.puzzle.nurikabe.rules.BlackBetweenRegionsBasicRule;
import edu.rpi.legup.save.InvalidFileFormatException;

public class BlackBetweenRegionsBasicRuleTest
{

    private static final BlackBetweenRegionsBasicRule RULE = new BlackBetweenRegionsBasicRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void BlackBetweenRegionsBasicRule_DiagonalBlackBetweenRegions1Test() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsBasicRule/DiagonalBlackBetweenRegions1", nurikabe);
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

        for(int i = 0; i < board.getElementCount(); i++)
        {
            if(i == cell1.getIndex() || i == cell2.getIndex())
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
    public void BlackBetweenRegionsBasicRule_DiagonalBlackBetweenRegions2Test() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsBasicRule/DiagonalBlackBetweenRegions2", nurikabe);
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

        for(int i = 0; i < board.getElementCount(); i++)
        {
            if(i == cell1.getIndex() || i == cell2.getIndex())
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
    public void BlackBetweenRegionsBasicRule_HorizontalBlackBetweenRegionsTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsBasicRule/HorizontalBlackBetweenRegions", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell = board.getCell(1, 1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < board.getElementCount(); i++)
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
    public void BlackBetweenRegionsBasicRule_VerticalBlackBetweenRegionsTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackBetweenRegionsBasicRule/VerticalBlackBetweenRegions", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell = board.getCell(1, 1);
        cell.setData(NurikabeType.BLACK.toValue());

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < board.getElementCount(); i++)
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
