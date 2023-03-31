package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.EmptyCornersBasicRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import org.junit.Assert;

public class EmptyCornersBasicRuleTest {
    private static final EmptyCornersBasicRule RULE = new EmptyCornersBasicRule();
    private static LightUp lightUp;


    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    public void EmptyCornersTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/EmptyCornersBasicRule/EmptyCorners", lightUp);
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

        //confirm there is a logical following of the EmptyCorners rule
        Assert.assertNull(RULE.checkRule(transition));

        //confirm the two expected cells are emptied USING THE RULE
        // and none of the rest are (others can be empty just not by the same rule)
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 2 && j == 0) || (i == 2 && j == 2)){
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}
/*
 * public GridCell getCell(int x, int y) {
        if (y * dimension.width + x >= puzzleElements.size() || x >= dimension.width ||
                y >= dimension.height || x < 0 || y < 0) {
            System.err.printf("not in bounds, bounds are %dx%d\n", dimension.width, dimension.height);        
            return null;
        }
        return (GridCell) puzzleElements.get(y * dimension.width + x);
    }
 * 
 * 
 * 
 */