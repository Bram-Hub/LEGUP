package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.TreeTentClue;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.lang.Object;

public class FillinRowCaseRule extends CaseRule
{

    public FillinRowCaseRule()
    {
        super("Fill In row",
                "A row must have the number of tents of its clue.",
                "edu/rpi/legup/images/treetent/case_rowcount.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board)
    {
        TreeTentBoard treeTentBoard = ((TreeTentBoard) board).copy();
        treeTentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(treeTentBoard, this);
        for(TreeTentClue element: treeTentBoard.getRowClues()){
            TreeTentClue cell = element;
            ArrayList<PuzzleElement> branches =branchable(treeTentBoard, element, true);
            if(cell.getType() == TreeTentType.CLUE_EAST && branches.size() > 0){
                caseBoard.addPickableElement(element);
            }
        }
        for(TreeTentClue element: treeTentBoard.getColClues()){
            TreeTentClue cell = element;
            ArrayList<PuzzleElement> branches = branchable(treeTentBoard, element, false);
            if(cell.getType() == TreeTentType.CLUE_SOUTH && branches.size() > 0){
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;
    }

    public ArrayList<PuzzleElement> branchable(TreeTentBoard board, TreeTentClue clue, boolean isEast){
        ArrayList<PuzzleElement> fill = new ArrayList<>();
        int size = isEast?board.getWidth():board.getHeight();
        int clueLoc = clue.getClueIndex();
        int tents = board.getRowCol(clueLoc-1, TreeTentType.TENT,isEast).size();
        List<TreeTentCell> unknowns = board.getRowCol(clueLoc-1, TreeTentType.UNKNOWN, isEast);
        if(tents > clue.getData()){
            return fill;
        }
        for(int i = 0;i < unknowns.size();i++){
            if(isValid(board,unknowns.get(i).getLocation())){
                fill.add(unknowns.get(i));
            }
        }
        return fill;
    }
    public boolean isValid(TreeTentBoard board, Point loc){
            ArrayList<TreeTentCell> adjacent = new ArrayList<>();
            adjacent.add(board.getCell(loc.x+1,loc.y));
            adjacent.add(board.getCell(loc.x-1,loc.y));
            adjacent.add(board.getCell(loc.x,loc.y+1));
            adjacent.add(board.getCell(loc.x,loc.y-1));
            for(TreeTentCell cell: adjacent){
                if(cell != null && (cell.getType() == TreeTentType.TREE && !cell.isLinked())){
                    return true;
                }
            }
            return false;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board        the current board state
     * @param puzzleElement equivalent puzzleElement
     *
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement)
    {

        ArrayList<Board> cases = new ArrayList<>();
        TreeTentClue clue = (TreeTentClue) puzzleElement;
        boolean isEast = clue.getType() == TreeTentType.CLUE_EAST;
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        List<PuzzleElement> branches = branchable(treeTentBoard, clue, isEast);
        int len = branches.size();
        int[] validCells = new int[len];
        ArrayList<Integer> adjCells = new ArrayList<>();
        for(int i = 0; i < len; i++){
            TreeTentCell cell = (TreeTentCell) branches.get(i);
            System.out.println(cell.getLocation());
            validCells[i] = isEast?cell.getLocation().x:cell.getLocation().y;
            if(i != 0){
                if(validCells[i]-validCells[i-1]==1){
                    adjCells.add(i-1);
                }
            }

        }
        int tents = treeTentBoard.getRowCol(clue.getClueIndex()-1, TreeTentType.TENT,isEast).size();
        System.out.println(adjCells);
//        System.out.println(validCells);
        ArrayList<String> combinations = new ArrayList<>();
        for(int i = 0; i < Math.pow(2,len); i++)
        {
            boolean skip = false;
            String format="%0"+len+"d";
            String binary = String.format(format,Integer.valueOf(Integer.toBinaryString(i)));
            if(clue.getData()-tents != StringUtils.countMatches(binary,'1')){
                skip = true;
            }
            outerloop:
            for(int k = 0; k < adjCells.size(); k++){
                System.out.println('k');
                System.out.println(k);
                System.out.println(binary);
                System.out.println(binary.charAt(adjCells.get(k)));
                if (binary.charAt(adjCells.get(k)) == '1' && binary.charAt(adjCells.get(k)+1) == '1') {
                    skip = true;
                }
            }
            if(!skip) {
                combinations.add(binary);
            }
            //System.out.println(binary);
        }
        System.out.println(combinations);
        for(int i = 0; i < combinations.size(); i++){
            TreeTentBoard branchCase = (TreeTentBoard) board.copy();
            String combo = combinations.get(i);
            for(int k = 0; k < combo.length(); k++){
                if(combo.charAt(k) == '0'){
                    continue;
                }
                TreeTentCell cell;
                if(isEast) {
                    cell = (TreeTentCell) branchCase.getCell(validCells[k], clue.getClueIndex() - 1);
                }
                else{
                    cell = (TreeTentCell) branchCase.getCell(clue.getClueIndex()-1,validCells[k]);
                }
                cell.setData(TreeTentType.TENT.value);
                branchCase.addModifiedData(cell);
            }
            cases.add(branchCase);
        }
        return cases;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return null;
    }
}
