package puzzles.skyscrapers.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.rules.CellForNumberCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class CellForNumberCaseRuleTest {

    private static final CellForNumberCaseRule RULE = new CellForNumberCaseRule();
    private static Skyscrapers skyscrapers;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        skyscrapers = new Skyscrapers();
    }

    //basic, max cases
    @Test
    public void CellForNumberCaseRule_BasicEmpty() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getNorthClues().get(0), 1);

        Assert.assertEquals(board.getWidth(), cases.size());

        for(int i=0;i<board.getWidth();i++){
            SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
            PuzzleElement changedCell = expected.getCell(0,i);
            changedCell.setData(1);
            expected.addModifiedData(changedCell);

            boolean exists = false;
            for(Board caseBoard: cases){
                if(expected.equalsBoard(caseBoard)){
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);
        }
    }

    //dupe, max cases
    @Test
    public void CellForNumberCaseRule_DupeEmpty() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(true);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getNorthClues().get(0), 1);

        Assert.assertEquals(board.getWidth(), cases.size());

        for(int i=0;i<board.getWidth();i++){
            SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
            PuzzleElement changedCell = expected.getCell(0,i);
            changedCell.setData(1);
            expected.addModifiedData(changedCell);

            boolean exists = false;
            for(Board caseBoard: cases){
                if(expected.equalsBoard(caseBoard)){
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);
        }
    }

    //dupe, 1 case
    @Test
    public void CellForNumberCaseRule_DupeSingular() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/CaseRules/3-1Opening", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(true);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getWestClues().get(3),1);

        Assert.assertEquals(1, cases.size());

        SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
        PuzzleElement changedCell = expected.getCell(2,3);
        changedCell.setData(1);
        expected.addModifiedData(changedCell);

        Assert.assertTrue(expected.equalsBoard(cases.get(0)));
    }

    //dupe, no cases
    @Test
    public void CellForNumberCaseRule_DupeNone() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1RowContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(true);
        board.setViewFlag(false);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getWestClues().get(3),1);

        Assert.assertEquals(0, cases.size());
    }

    //visibility, max cases
    @Test
    public void CellForNumberCaseRule_ViewEmpty() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getWestClues().get(1), 1);

        Assert.assertEquals(board.getWidth(), cases.size());

        for(int i=0;i<board.getWidth();i++){
            SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
            PuzzleElement changedCell = expected.getCell(i,1);
            changedCell.setData(1);
            expected.addModifiedData(changedCell);

            boolean exists = false;
            for(Board caseBoard: cases){
                if(expected.equalsBoard(caseBoard)){
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);
        }
    }

    //visibility, 1 Case, direct
    @Test
    public void CellForNumberCaseRule_ViewSingular() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/CaseRules/3-1Opening", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getWestClues().get(3),1);

        Assert.assertEquals(1, cases.size());

        SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
        PuzzleElement changedCell = expected.getCell(2,3);
        changedCell.setData(1);
        expected.addModifiedData(changedCell);

        Assert.assertTrue(expected.equalsBoard(cases.get(0)));
    }

    //visibility, 1 Case, implied
    @Test
    public void CellForNumberCaseRule_ImpliedViewSingular() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getWestClues().get(0),5);

        Assert.assertEquals(1, cases.size());

        SkyscrapersBoard expected = ((SkyscrapersBoard) transition.getBoard()).copy();
        PuzzleElement changedCell = expected.getCell(4,0);
        changedCell.setData(5);
        expected.addModifiedData(changedCell);

        Assert.assertTrue(expected.equalsBoard(cases.get(0)));
    }

    //visibility, no cases
    @Test
    public void CellForNumberCaseRule_ViewNone() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/skyscrapers/rules/UnresolvedContradictionRules/3-1RowContradiction", skyscrapers);

        TreeNode rootNode = skyscrapers.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();

        board.setDupeFlag(false);
        board.setViewFlag(true);

        ArrayList<Board> cases = RULE.getCasesFor(board,board.getWestClues().get(3),1);

        Assert.assertEquals(0, cases.size());
    }
}