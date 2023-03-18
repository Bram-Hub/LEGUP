package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.EmptyCellinLightBasicRule;
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

public class EmptyCellinLightBasicRuleTest {
    private static final EmptyCellinLightBasicRule RULE = new EmptyCellinLightBasicRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }

    @Test
    //tests a 3x3 board with with a 0 black tile in the center and lightbulbs in top left and bototm right
    //confirms the rest of the tiles must be empty
    public void EmptyCellinLightBasicRule() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/lightup/rules/EmptyCellinLightBasicRule/EmptyCells", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 
        
        //get board state 
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        //change the board's cells considering the emptycellinlight rule
        LightUpCell cell2 = board.getCell(1,0);
        cell2.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell2);

        LightUpCell cell3 = board.getCell(0,1);
        cell3.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell3);

        LightUpCell cell4 = board.getCell(2,0);
        cell4.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell4);

        LightUpCell cell5 = board.getCell(0,2);
        cell5.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell5);

        LightUpCell cell6 = board.getCell(1,2);
        cell6.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell6);

        LightUpCell cell7 = board.getCell(2,1);
        cell7.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell7);

        //confirm there is a logical following of the EmptyCellinLight rule
        Assert.assertNull(RULE.checkRule(transition));

        //cells (0,0) and (2,2) are not empty because they have lightbulbs, and (1,1) 
        //because it is a black tile. Confirm the rest are empty
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 0 && j == 0) || (i == 2 && j == 2) || (i == 1 && j == 1)){
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}
