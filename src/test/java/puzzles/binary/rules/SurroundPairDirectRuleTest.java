package puzzles.binary.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.Binary;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;
import edu.rpi.legup.puzzle.binary.rules.SurroundPairDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SurroundPairDirectRuleTest {

    private static final SurroundPairDirectRule RULE = new SurroundPairDirectRule();
    private static Binary binary;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        binary = new Binary();
    }

    /**
     * Tests the SurroundPair direct rule for surrounding a pair of zeros with ones
     */
    @Test
    public void SurroundPairDirectRule_SurroundTwoZerosWithTwoOnes() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/binary/rules/SurroundPairDirectRule/SurroundTwoZerosWithTwoOnes", binary);

        TreeNode rootNode = binary.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        BinaryBoard board = (BinaryBoard) transition.getBoard();
        BinaryCell cell1 = board.getCell(0, 0);
        cell1.setData(BinaryType.ONE.toValue());
        board.addModifiedData(cell1);

        BinaryCell cell2 = board.getCell(3, 0);
        cell1.setData(BinaryType.ONE.toValue());
        board.addModifiedData(cell2);

        //Assert.assertNull(RULE.checkRule(transition));

//        Point location1 = new Point(0, 0);
//        Point location2 = new Point(3, 0);
//        for (int i = 0; i < board.getHeight(); i++) {
//            for (int k = 0; k < board.getWidth(); k++) {
//                Point point = new Point(k, i);
//                if (point.equals(location1) || point.equals(location2)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
    }

}
