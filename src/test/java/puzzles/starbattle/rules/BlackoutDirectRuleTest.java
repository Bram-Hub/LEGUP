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
        StarBattleCell cell1 = board.getCell(1,0);
        cell1.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell2 = board.getCell(2,0);
        cell2.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell3 = board.getCell(3,0);
        cell3.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell4 = board.getCell(0,1);
        cell4.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell5 = board.getCell(0,2);
        cell5.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell6 = board.getCell(0,3);
        cell6.setData(StarBattleCellType.BLACK.value);
        
        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);
        board.addModifiedData(cell5);
        board.addModifiedData(cell6);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); ++i) {
            for (int j = 0; j < board.getWidth(); ++j) {
                Point point = new Point(j,i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation()) ||
                        point.equals(cell5.getLocation()) || point.equals(cell6.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(j, i)));
                }
            }
        }
    }

}
