package puzzles.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.rules.NumberForCellCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.util.ArrayList;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NumberForCellCaseRuleTest {

    private static final NumberForCellCaseRule RULE = new NumberForCellCaseRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    // basic, max cases
    @Test
    public void NumberForCellCaseRule_BasicEmpty() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(0, 0));

        Assert.assertEquals(board.getWidth(), cases.size());

        for (int i = 0; i < board.getWidth(); i++) {
            SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
            PuzzleElement changedCell = expected.getCell(0, 0);
            changedCell.setData(i + 1);
            expected.addModifiedData(changedCell);

            boolean exists = false;
            for (Board caseBoard : cases) {
                if (expected.equalsBoard(caseBoard)) {
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);
        }
    }

    // dupe, max cases
    @Test
    public void NumberForCellCaseRule_DupeEmpty() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(true);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(0, 0));

        Assert.assertEquals(board.getWidth(), cases.size());

        for (int i = 0; i < board.getWidth(); i++) {
            SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
            PuzzleElement changedCell = expected.getCell(0, 0);
            changedCell.setData(i + 1);
            expected.addModifiedData(changedCell);

            boolean exists = false;
            for (Board caseBoard : cases) {
                if (expected.equalsBoard(caseBoard)) {
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);
        }
    }

    // dupe, 1 case
    @Test
    public void NumberForCellCaseRule_DupeSingular() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/common/3-0RowOpening", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(true);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(2, 3));

        Assert.assertEquals(1, cases.size());

        SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
        PuzzleElement changedCell = expected.getCell(2, 3);
        changedCell.setData(1);
        expected.addModifiedData(changedCell);

        Assert.assertTrue(expected.equalsBoard(cases.get(0)));
    }

    // dupe, no cases
    @Test
    public void NumberForCellCaseRule_DupeNone() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1RowContradiction",
                skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(true);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(2, 3));

        Assert.assertEquals(0, cases.size());
    }

    // visibility, max cases
    @Test
    public void NumberForCellCaseRule_ViewEmpty() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(1, 4));

        Assert.assertEquals(4, cases.size());

        for (int i = 0; i < board.getWidth() - 1; i++) {
            SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
            PuzzleElement changedCell = expected.getCell(1, 4);
            changedCell.setData(i + 1);
            expected.addModifiedData(changedCell);

            boolean exists = false;
            for (Board caseBoard : cases) {
                if (expected.equalsBoard(caseBoard)) {
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);
        }
    }

    // visibility, 1 case
    @Test
    public void NumberForCellCaseRule_ViewSingular() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/common/3-0RowOpening", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(2, 3));

        Assert.assertEquals(1, cases.size());

        SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
        PuzzleElement changedCell = expected.getCell(2, 3);
        changedCell.setData(1);
        expected.addModifiedData(changedCell);

        Assert.assertTrue(expected.equalsBoard(cases.get(0)));
    }

    // visibility, no cases
    @Test
    public void NumberForCellCaseRule_ViewNone() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1RowContradiction",
                skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCases(board, board.getCell(2, 3));

        Assert.assertEquals(0, cases.size());
    }
}
