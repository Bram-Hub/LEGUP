package puzzles.starbattle.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.rules.ColumnsWithinRegionsDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

public class ColumnsWithinRegionsDirectRuleTest {

    private static final ColumnsWithinRegionsDirectRule RULE = new ColumnsWithinRegionsDirectRule();
    private static StarBattle starbattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starbattle = new StarBattle();
    }

    //single column w/in single region one square outside
    @Test
    public void ColumnsWithinRegionsDirectRule_OneColumnOneCell()
        throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/ColumnsWithinRegionsDirectRule/OneColumnOneCell", starbattle);
        TreeNode rootNode = starbattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(1,0);
        cell.setData(StarBattleCellType.BLACK.value);
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        Point location = new Point(1, 0);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }


}
