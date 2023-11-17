package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.rules.CannotReachCellDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import java.awt.*;

public class CannotReachCellDirectRuleTest {

    private static final CannotReachCellDirectRule RULE = new CannotReachCellDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the Cannot Reach Cell direct rule for a simple unreachable cell
     */
    @Test
    public void CannotReachCellDirectRule_SimpleUnreachableCell() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/CannotReachCellDirectRule/SimpleUnreachableCell", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
        NurikabeCell cell = board.getCell(0,0);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Point point = new Point(k,i);
                if(point.equals(cell.getLocation())){
                    Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
                else{
                    Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
                }
            }
        }
    }

    /**
     * Tests the Cannot Reach Cell direct rule for a more complex board with all cells reachable
     */
    @Test
    public void CannotReachCellDirectRule_AllCellsReachable() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/CannotReachCellDirectRule/AllCellsReachable", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();

        NurikabeCell cell1 = board.getCell(2,3);
        NurikabeCell cell2 = board.getCell(0,1);
        NurikabeCell cell3 = board.getCell(4,1);


        for(int i=0; i<board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k,i);
                if(!(point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) || point.equals(cell3.getLocation()))){
                    NurikabeCell newCell = board.getCell(k, i);
                    newCell.setData(NurikabeType.BLACK.toValue());
                    board.addModifiedData(newCell);
                }
            }
        }

        Assert.assertNotNull(RULE.checkRule(transition));

        for(int i=0; i<board.getHeight(); i++){
            for(int k=0; k<board.getWidth(); k++){
                Assert.assertNotNull(RULE.checkRuleAt(transition,board.getCell(k,i)));
            }
        }
    }
}
