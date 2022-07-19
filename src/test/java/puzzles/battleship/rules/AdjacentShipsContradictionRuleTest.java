package puzzles.battleship.rules;

import edu.rpi.legup.puzzle.battleship.BattleshipType;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.battleship.Battleship;
import edu.rpi.legup.puzzle.battleship.BattleshipBoard;
import edu.rpi.legup.puzzle.battleship.BattleshipCell;
import edu.rpi.legup.puzzle.battleship.rules.AdjacentShipsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class AdjacentShipsContradictionRuleTest
{
    private static final AdjacentShipsContradictionRule RULE
            = new AdjacentShipsContradictionRule();

    private static Battleship battleship;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        battleship = new Battleship();
    }

    @Test
    public void OrthogonalAdjacentTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/battleship/rules/" +
                "AdjacentShipsContradictionRule/OrthogonalAdjacentBoards",
                battleship);
        TreeNode rootNode = battleship.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        BattleshipBoard board = (BattleshipBoard)transition.getBoard();
        BattleshipCell cell1 = board.getCell(1, 1);
        BattleshipCell cell2 = board.getCell(1, 2);
        BattleshipCell cell3 = board.getCell(2, 1);
        BattleshipCell cell4 = board.getCell(2, 2);

        Assert.assertNull(RULE.checkContradiction(
                (BattleshipBoard)transition.getBoard()));


    }
}
