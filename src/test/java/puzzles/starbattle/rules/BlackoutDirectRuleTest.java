package puzzles.starbattle.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.rules.BlackoutDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlackoutDirectRuleTest {

    private static final BlackoutDirectRule RULE = new BlackoutDirectRule();
    private static StarBattle starbattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starbattle = new StarBattle();
    }

    @Test
    public void BlackoutDirectRuleTest_ColumnBlackout() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/starbattle/rules/BlackoutDirectRule/ColumnBlackout", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();

        StarBattleCell cell1 = board.getCell(1, 1);
        cell1.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell2 = board.getCell(1, 2);
        cell2.setData(StarBattleCellType.BLACK.value);
        StarBattleCell cell3 = board.getCell(1, 3);
        cell3.setData(StarBattleCellType.BLACK.value);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
