package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.*;
import edu.rpi.legup.puzzle.fillapix.rules.FinishWithBlackDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithBlackDirectRuleTest {

    public static final FinishWithBlackDirectRule RULE = new FinishWithBlackDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        fillapix = new Fillapix();
    }

    @Test
    public void FinishWithBlackDirectRule_OneUnsetOneBlackOneNumberTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/fillapix/rules/FinishWithBlackDirectRule/FinishWithBlack1.txt", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard();

        FillapixCell cell1 = board.getCell(1, 0);
        cell1.setData(FillapixTileData.black());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithBlackDirectRule_FourUnsetOneBlackOneNumberTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/fillapix/rules/FinishWithBlackDirectRule/FinishWithBlack2.txt", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard();

        FillapixCell cell1 = board.getCell(0, 1);
        cell1.setData(FillapixTileData.black());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    @Test
    public void FinishWithBlackDirectRule_FourUnsetFourBlacksOneNumberTest3()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/fillapix/rules/FinishWithBlackDirectRule/FinishWithBlack3.txt", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard();

        FillapixCell cell1 = board.getCell(1, 0);
        cell1.setData(FillapixTileData.black());
        FillapixCell cell2 = board.getCell(2, 0);
        cell2.setData(FillapixTileData.black());
        FillapixCell cell3 = board.getCell(0, 1);
        cell3.setData(FillapixTileData.black());
        FillapixCell cell4 = board.getCell(0, 2);
        cell4.setData(FillapixTileData.black());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())
                        || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
