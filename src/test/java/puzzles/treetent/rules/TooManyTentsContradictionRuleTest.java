package puzzles.treetent.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.TooManyTentsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class TooManyTentsContradictionRuleTest {

    private static final TooManyTentsContradictionRule RULE = new TooManyTentsContradictionRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    @Test
    public void TooManyTentsContradictionRule_Column() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsContradictionRuleColumn", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(0, 1);
        TreeTentCell cell2 = board.getCell(2, 1);

        Assert.assertNull(RULE.checkContradiction(board));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    // The TooManyTentsContradictionRule checks the col and row the cell is in
                    // Therefore, even if a cell(0, 0) is empty, it follows the contradiction rule because
                    // the row it is in follows the contradiciton rule. (And because cell(1, 0) has tent row tent total is 0)
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}

