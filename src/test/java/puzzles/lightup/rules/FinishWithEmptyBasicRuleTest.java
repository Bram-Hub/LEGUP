package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.FinishWithEmptyBasicRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import org.junit.Assert;

public class FinishWithEmptyBasicRuleTest {
    private static final FinishWithEmptyBasicRule RULE = new FinishWithEmptyBasicRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    public void FinishWithEmptyWithThreeTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/FinishWithEmptyBasicRule/FinishWithEmptyWithThree", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 
        
        //get board state 
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        //change the board's cells considering the FinishWithEmpty rule to empty
        LightUpCell cell1 = board.getCell(1,2);
        cell1.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell1);
        
        //confirm there is a logical following of the FinishWithBulbs rule
        Assert.assertNull(RULE.checkRule(transition));

        //only the cell listed above should change following the rule
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if (i == 2 && j == 1){
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

    @Test
    public void FinishWithEmptyTestWithOne() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/FinishWithEmptyBasicRule/FinishWithEmptyWithOne", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 
        
        //get board state 
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        //change the board's cells considering the FinishWithEmpty rule to empty
        LightUpCell cell1 = board.getCell(0,1);
        cell1.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell1);

        LightUpCell cell2 = board.getCell(2,1);
        cell2.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell2);

        LightUpCell cell3 = board.getCell(1,2);
        cell3.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell3);
        
        //confirm there is a logical following of the FinishWithBulbs rule
        Assert.assertNull(RULE.checkRule(transition));

        //only the three cells listed above should change following the rule
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 1 && j == 0) || (i == 1 && j == 2) || (i == 2 && j == 1)){
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
