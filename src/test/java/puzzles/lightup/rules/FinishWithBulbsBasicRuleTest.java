package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.FinishWithBulbsBasicRule;
import edu.rpi.legup.model.PuzzleImporter;
import legup.MockGameBoardFacade;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import org.junit.Assert;

public class FinishWithBulbsBasicRuleTest {
    private static final FinishWithBulbsBasicRule RULE = new FinishWithBulbsBasicRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }

    @Test
    public void FinishBulbTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/FinishWithBulbsBasicRule/FinishWithBulbs", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 
        
        //get board state 
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        //change the board's cells considering the FinishWithBulbs rule to empty
        LightUpCell cell1 = board.getCell(1,0);
        cell1.setData(LightUpCellType.BULB.value);
        board.addModifiedData(cell1);

        //confirm there is a logical following of the FinishWithBulbs rule
        Assert.assertNull(RULE.checkRule(transition));

        //check every square except the top center (2,0)
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if (i == 0 && j == 1){
                    //logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    //does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    //even though this test isnt a completely filled board because it is unsolveable, it tests FinishBulbs properly
    @Test
    public void FinishBulbTestWithThree() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/FinishWithBulbsBasicRule/FinishWithBulbsWithThree", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 
        
        //get board state 
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        //change the board's cells considering the FinishWithBulbs rule to empty
        LightUpCell cell1 = board.getCell(1,2);
        cell1.setData(LightUpCellType.BULB.value);
        board.addModifiedData(cell1);

        LightUpCell cell2 = board.getCell(0,1);
        cell2.setData(LightUpCellType.BULB.value);
        board.addModifiedData(cell2);

        LightUpCell cell3 = board.getCell(2,1);
        cell3.setData(LightUpCellType.BULB.value);
        board.addModifiedData(cell3);

        //confirm there is a logical following of the FinishWithBulbs rule
        Assert.assertNull(RULE.checkRule(transition));

        //check every square for logical following
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 1 && j == 2) || (i == 2 && j == 1) || (i == 1 && j == 0)){
                    //logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    //does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}
