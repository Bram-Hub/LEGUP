//package puzzles.nurikabe.rules;
//
//import edu.rpi.legup.model.gameboard.Board;
//import edu.rpi.legup.puzzle.nurikabe.*;
//import edu.rpi.legup.puzzle.nurikabe.rules.BlackOrWhiteCaseRule;
//import edu.rpi.legup.utility.DisjointSets;
//import legup.MockGameBoardFacade;
//import legup.TestUtilities;
//import edu.rpi.legup.model.tree.TreeNode;
//import edu.rpi.legup.model.tree.TreeTransition;
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import edu.rpi.legup.puzzle.nurikabe.rules.TooFewSpacesContradictionRule;
//import edu.rpi.legup.save.InvalidFileFormatException;
//
//import java.util.ArrayList;
//
//import java.awt.*;
//import java.util.Set;
//
//public class FinishRoomCaseRuleTest {
//
//    private static final FinishRoomCaseRule RULE = new FinishRoomCaseRule();
//    private static Nurikabe nurikabe;
//
//    @BeforeClass
//    public static void setUp() {
//        MockGameBoardFacade.getInstance();
//        nurikabe = new Nurikabe();
//    }
//
//    /**
//     * Tests the Finish Room case rule by ensuring that it results in 5 or less children, that contain a modified cell that is white
//     */
//    @Test
////    public void TooFewSpacesContradictionRule_TwoSurroundBlackTest() throws InvalidFileFormatException {
//    public void FinishRoomCaseRule_FinishRoomCaseRuleBaseTest() throws InvalidFileFormatException {
//        TestUtilities.importTestBoard("puzzles/nurikabe/rules/FinishRoomCaseRule/FinishRoomCaseRuleBase", nurikabe); //gotta make my own
//        TreeNode rootNode = nurikabe.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
//        NurikabeCell cell = board.getCell(6,4);
//        ArrayList<Board> cases = RULE.getCases(board,cell);
//
//        Assert.assertEquals(2,cases.size());
//
//        NurikabeBoard caseBoard = (NurikabeBoard) cases.get(0);
//        NurikabeBoard caseBoard2 = (NurikabeBoard) cases.get(1);
//
//        NurikabeType board1Type = caseBoard.getCell(5,5).getType();
//        NurikabeType board2Type = caseBoard2.getCell(6,6).getType();
//
//        Assert.assertTrue((board1Type.equals(NurikabeType.WHITE) && board2Type.equals(NurikabeType.WHITE)));
//
//        Assert.assertEquals(caseBoard.getHeight(),caseBoard2.getHeight(), board.getHeight());
//        Assert.assertEquals(caseBoard.getWidth(),caseBoard2.getWidth(), board.getWidth());
//
//        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(caseBoard); //gathers regions
//        Set<NurikabeCell> disRow = regions.getSet(caseBoard.getCell(5, 5));
//        Assert.assertEquals(disRow.size(), 3);
//
//        DisjointSets<NurikabeCell> regions2 = NurikabeUtilities.getNurikabeRegions(caseBoard2); //gathers regions
//        Set<NurikabeCell> disRow2 = regions2.getSet(caseBoard2.getCell(6, 6));
//        Assert.assertEquals(disRow2.size(), 3);
//
//        for(int i=0; i<caseBoard.getHeight(); i++){
//            for(int k=0; k<caseBoard.getWidth(); k++){
//                Point point = new Point(k,i);
//                if(point.equals(caseBoard.getCell(k,i).getLocation())){
//                    continue;
//                }
//                Assert.assertTrue(caseBoard.getCell(k,i).equals(caseBoard2.getCell(k,i))
//                        || caseBoard2.getCell(k,i).equals(caseBoard2.getCell(k,i)));
//            }
//        }
//
//
//
//
//        NurikabeCell cell2 = board.getCell(4,2);
//        ArrayList<Board> cases2 = RULE.getCases(board,cell2);
//
//        Assert.assertEquals(8,cases2.size());
//
//        NurikabeBoard caseyBoard = (NurikabeBoard) cases2.get(0);
//        NurikabeBoard caseyBoard2 = (NurikabeBoard) cases2.get(1);
//        NurikabeBoard caseyBoard3 = (NurikabeBoard) cases2.get(2);
//        NurikabeBoard caseyBoard4 = (NurikabeBoard) cases2.get(3);
//        NurikabeBoard caseyBoard5 = (NurikabeBoard) cases2.get(4);
//        NurikabeBoard caseyBoard6 = (NurikabeBoard) cases2.get(5);
//        NurikabeBoard caseyBoard7 = (NurikabeBoard) cases2.get(6);
//        NurikabeBoard caseyBoard8 = (NurikabeBoard) cases2.get(7);
//
//        NurikabeType boardy1Type = caseyBoard.getCell(5,5).getType();
//        NurikabeType boardy2Type = caseyBoard2.getCell(6,6).getType();
//        NurikabeType boardy3Type = caseyBoard.getCell(5,5).getType();
//        NurikabeType boardy4Type = caseyBoard2.getCell(6,6).getType();
//        NurikabeType boardy5Type = caseyBoard.getCell(5,5).getType();
//        NurikabeType boardy6Type = caseyBoard2.getCell(6,6).getType();
//        NurikabeType boardy7Type = caseyBoard.getCell(5,5).getType();
//        NurikabeType boardy8Type = caseyBoard2.getCell(6,6).getType();
//
//
//    }
//}
