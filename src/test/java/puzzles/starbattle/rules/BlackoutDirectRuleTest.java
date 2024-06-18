package puzzles.starbattle.rules;

import edu.rpi.legup.puzzle.starbattle.rules.BlackoutDirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlackoutDirectRuleTest {
    private static final BlackoutDirectRule RULE = new BlackoutDirectRule();
    private static StarBattle starBattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starBattle = new StarBattle();
    }

    /* Blackout Direct Rule where star is in the corner */
    @Test
    public void BlackoutDirectRuleTestCorner()
            throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/BlackoutDirectRule/Corner", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell1 = board.getCell(0,0); /*
        StarBattleCell cell2 = board.getCell(0,1);
        StarBattleCell cell3 = board.getCell(1,0);
        StarBattleCell cell4 = board.getCell(1,1); */

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) /*|| point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation()) */) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                /*
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                */
            }
        }
    }

}
