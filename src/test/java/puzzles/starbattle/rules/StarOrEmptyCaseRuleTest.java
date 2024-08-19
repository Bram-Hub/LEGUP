package puzzles.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.starbattle.StarBattle;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import edu.rpi.legup.puzzle.starbattle.rules.StarOrEmptyCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import java.util.ArrayList;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StarOrEmptyCaseRuleTest {

    private static final StarOrEmptyCaseRule RULE = new StarOrEmptyCaseRule();
    private static StarBattle starBattle;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        starBattle = new StarBattle();
    }

    @Test
    public void StarOrEmptyCaseRuleTest_SimpleStarOrEmpty()
        throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/starbattle/rules/StarOrEmptyCaseRule/SimpleStarOrEmpty", starBattle);
        TreeNode rootNode = starBattle.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = board.getCell(0,0);
        ArrayList<Board> cases = RULE.getCases(board, cell);

        StarBattleBoard caseBoard1 = (StarBattleBoard) cases.get(0);
        StarBattleBoard caseBoard2 = (StarBattleBoard) cases.get(1);
        StarBattleCellType board1Type = caseBoard1.getCell(0, 0).getType();
        StarBattleCellType board2Type = caseBoard2.getCell(0, 0).getType();

        Assert.assertTrue(
                (board1Type.equals(StarBattleCellType.BLACK) || board1Type.equals(StarBattleCellType.STAR))
                        && (board2Type.equals(StarBattleCellType.BLACK)
                        || board2Type.equals(StarBattleCellType.STAR)));
        Assert.assertFalse(board1Type.equals(board2Type));
        Assert.assertEquals(caseBoard1.getHeight(), caseBoard2.getHeight(), board.getHeight());
        Assert.assertEquals(caseBoard1.getWidth(), caseBoard2.getWidth(), board.getWidth());

        for (int i = 0; i < caseBoard1.getHeight(); i++) {
            for (int k = 0; k < caseBoard1.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(caseBoard1.getCell(k, i).getLocation())) {
                    continue;
                }
                Assert.assertTrue(caseBoard1.getCell(k, i).equals(caseBoard2.getCell(k, i)));
            }
        }
    }

}
