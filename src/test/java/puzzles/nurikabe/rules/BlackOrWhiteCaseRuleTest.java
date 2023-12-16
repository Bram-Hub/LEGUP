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

        NurikabeType board1Type = caseBoard.getCell(0,0).getType();
        NurikabeType board2Type = caseBoard2.getCell(0,0).getType();

        Assert.assertTrue((board1Type.equals(NurikabeType.BLACK) || board1Type.equals(NurikabeType.WHITE)) &&
                (board2Type.equals(NurikabeType.BLACK) || board2Type.equals(NurikabeType.WHITE)));
        Assert.assertFalse(board1Type.equals(board2Type));

        Assert.assertEquals(caseBoard.getHeight(),caseBoard2.getHeight(), board.getHeight());
        Assert.assertEquals(caseBoard.getWidth(),caseBoard2.getWidth(), board.getWidth());

        for(int i=0; i<caseBoard.getHeight(); i++){
            for(int k=0; k<caseBoard.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(caseBoard.getCell(k,i).getLocation())){
                    continue;
                }
                Assert.assertTrue(caseBoard.getCell(k,i).equals(caseBoard2.getCell(k,i)));
            }
        }


    }
}
