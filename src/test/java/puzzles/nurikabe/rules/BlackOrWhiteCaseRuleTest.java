package puzzles.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.rules.BlackOrWhiteCaseRule;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.TooFewSpacesContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.util.ArrayList;

import java.awt.*;

public class BlackOrWhiteCaseRuleTest {

    private static final BlackOrWhiteCaseRule RULE = new BlackOrWhiteCaseRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Black Or White case rule by ensuring that it results in two  children, that contain a modified cell that is either black or white
     */
    @Test
    public void TooFewSpacesContradictionRule_TwoSurroundBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackOrWhiteCaseRule/SimpleBlackOrWhite", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = board.getCell(0,0);
        ArrayList<Board> cases = RULE.getCases(board,cell);

        Assert.assertEquals(2,cases.size());

        NurikabeBoard caseBoard = (NurikabeBoard) cases.get(0);
        NurikabeBoard caseBoard2 = (NurikabeBoard) cases.get(1);

        Integer v1 = new Integer(0);
        Integer v2 = new Integer(-1);

        Assert.assertTrue((caseBoard.getCell(0,0).getData().equals(v1) || caseBoard.getCell(0,0).equals(v2)) &&
                (caseBoard2.getCell(0,0).getData().equals(v1) || caseBoard2.getCell(0,0).getData().equals(v2)));
        Assert.assertFalse(caseBoard.getCell(0,0).getData().equals(caseBoard2.getCell(0,0).getData()));


    }
}
