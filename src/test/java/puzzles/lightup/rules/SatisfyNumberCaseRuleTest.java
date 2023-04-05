package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.SatisfyNumberCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Iterator;


public class SatisfyNumberCaseRuleTest {
    private static final SatisfyNumberCaseRule RULE = new SatisfyNumberCaseRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }
    
    @Test
    public void SatisfyNumberTest() throws InvalidFileFormatException {
        //i should manually create all boards that COULD POSSIBLY be cases of the rule
        //then get all the cases from the getcase function and make sure they all exist in the list returned by getcases
        //shouldnt need to check for contradictions... maybe ask bram in case
        //puzzle elements are basically just cells 
        
        TestUtilities.importTestBoard("puzzles/lightup/rules/SatisfyNumberCaseRule/SatisfyNumber", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 

        //get all new board states using caserule builtin function
        LightUpBoard b = (LightUpBoard) transition.getBoard();
        LightUpCell numbered_cell = b.getCell(1,0); //the tile cell
        ArrayList<Board> cases = RULE.getCases(b, numbered_cell);//C MUST BE THE NUMBERED TILE, NOT ANY RANDOM EMPTY ONE
        System.out.println("cases" + cases);

        //assert correct number of cases
        Assert.assertEquals(2, cases.size());

        //make a list of boards that I expect
        LightUpCell change_cell;
        LightUpBoard case1 = (LightUpBoard) transition.getBoard();
        LightUpBoard case2 = (LightUpBoard) transition.getBoard();

        //change the cells of the first new case board
        change_cell = case1.getCell(0,0);
        change_cell.setData(LightUpCellType.BULB.value);
        case1.addModifiedData(change_cell);

        change_cell = case1.getCell(1,1);
        change_cell.setData(LightUpCellType.EMPTY.value);
        case1.addModifiedData(change_cell);

        //change the cells of the second new case board
        change_cell = case2.getCell(0,0);
        change_cell.setData(LightUpCellType.EMPTY.value);
        case2.addModifiedData(change_cell);

        change_cell = case2.getCell(1,1);
        change_cell.setData(LightUpCellType.BULB.value);
        case2.addModifiedData(change_cell);

        //check each board I expect and make sure it exists in returned board list   
        Assert.assertTrue(cases.contains((Board) case1));/////////////either the .contains doesnt find the case valid or something else
        Assert.assertTrue(cases.contains((Board) case2));//try printing out each board and confirm their values are what I think they are

        
    }
}

    /*
    @Test
    public void SatisfyNumberTest() throws InvalidFileFormatException {
        //make two mock boards that have lightbulbs in different locations
        //run contradiction tests on both and confirm they are both valid
        //might be able to call other functions within that function to confirm it is valid
        
        CaseRule caseRule = (CaseRule) RULE;
        
        TestUtilities.importTestBoard("puzzles/lightup/rules/SatisfyNumberCaseRule/SatisfyNumber", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 

        //make new board states
        LightUpBoard board1 = (LightUpBoard) transition.getBoard();///////////////there exists a getcaseboard(), but the nurikabe just uses getboard
        LightUpBoard board2 = (LightUpBoard) transition.getBoard();//there also exists a getcases function that seems to give me all cases possible
        //CaseBoard caseBoard = caseRule.getCaseBoard(transition.getBoard());///////////////////
        //ArrayList<LightUpBoard> cases = getCases(transition.getBoard(), elementView.getPuzzleElement());///AutoCsaaeRuleCommand.java

        //change the boards cells considering the SatisfyNumber case rule
        //board 1 cells
        LightUpCell b1c1 = board1.getCell(0,0);
        LightUpCell b1c3 = board1.getCell(1,1);

        //board 2 cells
        LightUpCell b2c1 = board1.getCell(0,0);
        LightUpCell b2c3 = board1.getCell(1,1);

        //set lightbulbs of both boards
        b1c1.setData(LightUpCellType.BULB.value);
        b2c3.setData(LightUpCellType.BULB.value);

        //set the empty spaces of both
        b1c3.setData(LightUpCellType.EMPTY.value);
        b2c1.setData(LightUpCellType.EMPTY.value);

        //set the boards to be the new branched versions
        board1.addModifiedData(b1c1);
        board1.addModifiedData(b1c3);
        board2.addModifiedData(b2c1);
        board2.addModifiedData(b2c3);

        //confirm there is a logical following of the SatisfyNumberCaseRule
        //Assert.assertNull(RULE.checkRule(transition));//////////////////////////might need to check contradiction instead like in nurikabe

        //test every cell compared to transition (root node) for board1
        LightUpCell c;
        for (int i = 0; i < board1.getHeight(); i++) {
            for (int j = 0; j < board1.getWidth(); j++) {
                c = board1.getCell(j, i);
                if (!(i == 1 && j == 0)){
                    //logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    //does not use the rule to logically follow (0,1)
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }

        //test every cell compared to transition (root node) for board2
        for (int i = 0; i < board2.getHeight(); i++) {
            for (int j = 0; j < board2.getWidth(); j++) {
                c = board2.getCell(j, i);
                if (!(i == 1 && j == 0)){
                    //logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    //does not use the rule to logically follow (0,1)
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    
    }
}
*/