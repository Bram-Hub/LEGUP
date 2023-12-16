package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.EmptyCornersDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import org.junit.Assert;

public class EmptyCornersDirectRuleTest {
    private static final EmptyCornersDirectRule RULE = new EmptyCornersDirectRule();
    private static LightUp lightUp;


    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    public void EmptyCornersTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/EmptyCornersDirectRule/EmptyCorners", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        //get board state 
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        //change the board's cells considering the EmptyCorners rule to empty
        LightUpCell cell1 = board.getCell(2,2);
        cell1.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell1);

        LightUpCell cell2 = board.getCell(0,2);
        cell2.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell2);

        LightUpCell cell3 = board.getCell(4,3);
        cell3.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell3);

        //confirm there is a logical following of the EmptyCorners rule
        Assert.assertNull(RULE.checkRule(transition));

        //this should not be accepted, the cell should remain unknown
        LightUpCell cell4 = board.getCell(4,5);
        cell4.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell4);

        //this should not be accepted, the cell should be empty but not because of this rule
        LightUpCell cell5 = board.getCell(4,1);
        cell5.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell5);

        Assert.assertNotNull(RULE.checkRule(transition));

        //confirm the two expected cells are emptied USING THE RULE
        // and none of the rest are (others can be empty just not by the same rule)
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 2 && j == 0) || (i == 2 && j == 2) || (i==3 && j==4)){
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}