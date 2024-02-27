package puzzles.lightup.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import edu.rpi.legup.puzzle.lightup.rules.FinishWithEmptyDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithEmptyDirectRuleTest {
    private static final FinishWithEmptyDirectRule RULE = new FinishWithEmptyDirectRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() { lightUp = new LightUp(); }

    @Test
    public void FinishEmptyTestWithOne() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/FinishWithEmptyDirectRule/FinishWithEmptyWithOne", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        //get board state
        LightUpBoard board = (LightUpBoard) transition.getBoard();
        // The board has a "1" clue at (1,1) with a bulb one space above at (1,0);

        //change the board's cells considering the FinishWithEmpty rule to empty
        LightUpCell cell1 = board.getCell(0,1);  //left
        cell1.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell1);

        LightUpCell cell2 = board.getCell(2,1);  //right
        cell2.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell2);

        LightUpCell cell3 = board.getCell(1,2);  //below
        cell3.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell3);

        //confirm there is a logical following of the FinishWithEmpty rule
        Assert.assertNull(RULE.checkRule(transition));

        //check every square except the top center (2,0)
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

    @Test
    public void FinishEmptyTestWithThree() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/FinishWithEmptyDirectRule/FinishWithEmptyWithThree", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        //get board state
        LightUpBoard board = (LightUpBoard) transition.getBoard();
        // The board has a "3" clue at (1,1) with a bulbs at (0,1), (2,1), and (1,2)

        //change the board's cells considering the FinishWithBulbs rule to empty
        LightUpCell cell1 = board.getCell(1,0);
        cell1.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell1);

        //confirm there is a logical following of the FinishWithBulbs rule
        Assert.assertNull(RULE.checkRule(transition));

        //check every square for logical following
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
}
