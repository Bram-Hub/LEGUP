package puzzles.skyscrapers.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.rules.NEdgeDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NEdgeDirectTest {

    private static final NEdgeDirectRule RULE = new NEdgeDirectRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    // -> row, empty -> full
    @Test
    public void NEdgeDirectRule_RightRowTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < 5; i++) {
            SkyscrapersCell cell = board.getCell(i, 0);
            cell.setData(i + 1);
            board.addModifiedData(cell);
        }

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if (i == 0) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // <-row, partial -> partial
    @Test
    public void NEdgeDirectRule_LeftRowTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/NEdgeDirectRule/LeftRowPartial", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell cell = board.getCell(1, 3);
        cell.setData(2);

        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // up col, partial -> full
    @Test
    public void NEdgeDirectRule_UpColTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/NEdgeDirectRule/UpColPartial", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 0; i < 2; i++) {
            SkyscrapersCell cell = board.getCell(1, i);
            cell.setData(i + 1);
            board.addModifiedData(cell);
        }

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if (k == 1 && i < 2) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    // down col, empty -> partial
    @Test
    public void NEdgeDirectRule_DownColTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/NEdgeDirectRule/DownColEmpty", skyscrapers);
        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
        for (int i = 1; i < 5; i++) {
            SkyscrapersCell cell = board.getCell(3, i);
            cell.setData(5 - i);
            board.addModifiedData(cell);
        }

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                if (k == 3 && i > 0) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
