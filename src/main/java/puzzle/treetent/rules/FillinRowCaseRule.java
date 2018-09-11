package puzzle.treetent.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.CaseRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentClue;
import puzzle.treetent.TreeTentType;


import java.awt.*;
import java.util.ArrayList;

public class FillinRowCaseRule extends CaseRule
{

    public FillinRowCaseRule()
    {
        super("Fill In row", "A row must have the number of tents of its clue.", "images/treetent/case_rowcount.png");
    }

    @Override
    public Board getCaseBoard(Board board)
    {
        TreeTentBoard caseBoard = ((TreeTentBoard) board).copy();
        caseBoard.setCaseRule(this);
        caseBoard.setModifiable(false);
        for(TreeTentClue clue : caseBoard.getEast())
        {
            clue.setCaseApplicable(true);
        }
        for(TreeTentClue clue : caseBoard.getSouth())
        {
            clue.setCaseApplicable(true);
        }
//        for(ElementData data: caseBoard.getElementData())
//        {
//            TreeTentCell cell = (TreeTentCell) data;
//            if(cell.getType() == TreeTentType.CLUE_EAST) {
//                System.out.println("clue east");
//                data.setCaseApplicable(true);
//                //Pair<Integer, ArrayList<Integer>> branch = rowBranch(caseBoard, cell.getValueInt());
////                if (getUnknown(caseBoard, cell.getValueInt(), true).size() > 0 && !(branch.getKey() < 0 || branch.getKey() > branch.getValue().size())) {
////                    data.setCaseApplicable(true);
////                }
//            }else if(cell.getType() == TreeTentType.CLUE_SOUTH){
//                data.setCaseApplicable(true);
//                //Pair<Integer, ArrayList<Integer>> branch = colBranch(caseBoard, cell.getValueInt());
////                if (getUnknown(caseBoard, cell.getValueInt(), false).size() > 0 && !(branch.getKey() < 0 || branch.getKey() > branch.getValue().size())) {
////                    data.setCaseApplicable(true);
////                }
//            }
//        }


        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board        the current board state
     * @param elementIndex element to determine the possible cases for
     *
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, int elementIndex)
    {
//        ArrayList<Board> cases = new ArrayList<>();
//        Pair<Integer,ArrayList<Integer>> branches;
//        ArrayList<Integer> unknowns;
//        if(((TreeTentCell) board.getElementData(elementIndex)).getType() == TreeTentType.CLUE_EAST){
//            branches = rowBranch((TreeTentBoard) board,board.getElementData(elementIndex).getValueInt());
//            unknowns = getUnknown((TreeTentBoard) board, board.getElementData(elementIndex).getValueInt(),true);
//        } else{
//            branches = colBranch((TreeTentBoard) board,board.getElementData(elementIndex).getValueInt());
//            unknowns = getUnknown((TreeTentBoard) board, board.getElementData(elementIndex).getValueInt(),false);
//        }
//        int tentNeeded = branches.getKey();
//        ArrayList<Integer> validCells = branches.getValue();
//
//        for(int branchIndex: validCells){
//            TreeTentBoard branchCase = ((TreeTentBoard) board).copy();
//            TreeTentCell data = (TreeTentCell) branchCase.getElementData(branchIndex);
//
//        }
//        Board boardCase = board.copy();
//        TreeTentClue data1 = (TreeTentClue) boardCase.getElementData(elementIndex);
//        if(data1.getType() == TreeTentType.CLUE_EAST){
//            for(int i = 0; i < ((TreeTentBoard) board).getWidth();i++){
//
//            }
//        }
//
//
        return null;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleAt(TreeTransition transition, int elementIndex)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node using this rule
     * and if so will perform the default application of the rule
     *
     * @param transition transition to apply default application
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplication(TreeTransition transition)
    {
        return false;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the
     * specific element index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param elementIndex
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex)
    {
        return false;
    }


//    public Pair<Integer,ArrayList<Integer>>rowBranch(TreeTentBoard board, int row){
//        ArrayList<Integer> valid_cell = new ArrayList<>();
//        int tentCount = 0;
//        for(int i = 0;i < board.getWidth();i++){
//            if(board.getCell(i,row).getType() == TreeTentType.TENT){
//                tentCount += 1;
//            }
//            else if(board.getCell(i,row).getType() == TreeTentType.UNKNOWN && checkTreeforTent(board,new Point(i,row))){
//                valid_cell.add(board.getCell(i,row).getIndex());
//            }
//        }
//        int tentsNeeded = board.getEast().get(row).getValueInt() - tentCount;
//        return new Pair<>(tentsNeeded,valid_cell);
//    }
//
//    public Pair<Integer,ArrayList<Integer>>colBranch(TreeTentBoard board, int col){
//        ArrayList<Integer> valid_cell = new ArrayList<>();
//        int tentCount = 0;
//        for(int i = 0;i < board.getHeight();i++){
//            if(board.getCell(col,i).getType() == TreeTentType.TENT){
//                tentCount += 1;
//            }
//            else if(board.getCell(col,i).getType() == TreeTentType.UNKNOWN && checkTreeforTent(board,new Point(col,i))){
//                valid_cell.add(board.getCell(col,i).getIndex());
//            }
//        }
//        int tentsNeeded = board.getSouth().get(col).getValueInt() - tentCount;
//        return new Pair<>(tentsNeeded,valid_cell);
//    }


    public boolean checkTreeforTent(TreeTentBoard board, Point loc){
        ArrayList<TreeTentCell> adjacent = new ArrayList<>();
        adjacent.add(board.getCell(loc.x+1,loc.y));
        adjacent.add(board.getCell(loc.x-1,loc.y));
        adjacent.add(board.getCell(loc.x,loc.y+1));
        adjacent.add(board.getCell(loc.x,loc.y-1));
        for(TreeTentCell cell: adjacent){
            if (cell != null && (cell.getType() == TreeTentType.TREE && !cell.isLinked())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getUnknown(TreeTentBoard board, int num, boolean isEast){
        ArrayList<Integer> blankCells = new ArrayList<>();
        if(isEast) {
            for (int i = 0; i < board.getWidth(); i++) {
                if (board.getCell(i, num).getType() == TreeTentType.UNKNOWN) {
                    blankCells.add(board.getCell(i,num).getIndex());
                }
            }
        }else{
            for(int i = 0;i < board.getHeight();i++){
                if(board.getCell(num,i).getType() == TreeTentType.UNKNOWN){
                    blankCells.add(board.getCell(num,i).getIndex());
                }
            }
        }
        return blankCells;
    }

    public int getNumCombination(int numSpace, int numTent){
        int combinations;
        int count = 1;
        int numer=1;
        int denom=1;
        for(int i = 1; i <= numTent;i++){
            numer *= numSpace-(i-1);
            denom *= i;
        }
        return numer/denom;
    }
//    public ArrayList<ArrayList<Integer>> getPermutation(int numSpace, int numTent){
//        int combinations = getNumCombination(numSpace,numTent);
//        for(int i = 0; i < combinations){
//
//        }
//    }
}
