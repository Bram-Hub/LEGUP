package puzzle.treetent.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.CaseRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentLine;
import puzzle.treetent.TreeTentType;

import java.awt.*;
import java.util.ArrayList;

public class LinkTentCaseRule extends CaseRule
{

    public LinkTentCaseRule()
    {
        super("Links from tent", "A tent must link to exactly one adjacent tree.", "images/treetent/caseLinkTent.png");
    }

    @Override
    public Board getCaseBoard(Board board)
    {
        TreeTentBoard caseBoard = ((TreeTentBoard) board).copy();
        caseBoard.setCaseRule(this);
        caseBoard.setModifiable(false);
        for(ElementData data: caseBoard.getElementData()){
            TreeTentCell cell = (TreeTentCell) data;
            ArrayList<Integer> branches = branchable(caseBoard,cell.getLocation());
            if(cell.getType() == TreeTentType.TENT && !cell.isLinked() && branches.size() > 0){
                data.setCaseApplicable(true);
            }
        }
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
        ArrayList<Board> cases = new ArrayList<>();
        ArrayList<Integer> branches = branchable((TreeTentBoard) board,((TreeTentCell) board.getElementData(elementIndex)).getLocation());

        for(int branchIndex: branches){
            TreeTentBoard branchCase = ((TreeTentBoard) board).copy();
            TreeTentCell data = (TreeTentCell) branchCase.getElementData(branchIndex);
//            data.setValueInt(TreeTentType.TENT.toValue());
//            branchCase.addModifiedData(data);
            branchCase.getLines().add(new TreeTentLine((TreeTentCell) branchCase.getElementData(branchIndex),(TreeTentCell) branchCase.getElementData(elementIndex)));
            data.setLink(true);
            ((TreeTentCell) branchCase.getElementData(elementIndex)).setLink(true);
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

    public ArrayList<Integer> branchable(TreeTentBoard board, Point loc){
        ArrayList<TreeTentCell> adjacent = new ArrayList<>();
        ArrayList<Integer> branchableCellIndex = new ArrayList<>();
        adjacent.add(board.getCell(loc.x+1,loc.y));
        adjacent.add(board.getCell(loc.x-1,loc.y));
        adjacent.add(board.getCell(loc.x,loc.y+1));
        adjacent.add(board.getCell(loc.x,loc.y-1));
        for(TreeTentCell cell: adjacent){
            if(cell != null && (cell.getType() == TreeTentType.TREE && !cell.isLinked())){
                branchableCellIndex.add(cell.getIndex());
            }
        }
        return branchableCellIndex;

    }
}
