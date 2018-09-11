package puzzle.treetent.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentType;

import java.awt.*;

public class TouchingTentsContradictionRule extends ContradictionRule
{

    public TouchingTentsContradictionRule()
    {
        super("Touching Tents", "Tents cannot touch other tents.", "images/treetent/contra_adjacentTents.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific element index using this rule
     *
     * @param transition   transition to check contradiction
     * @param elementIndex index of the element
     *
     * @return null if the transition contains a contradiction at the specified element,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, int elementIndex)
    {
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell cell = (TreeTentCell)board.getElementData(elementIndex);
        return (cell.getType() != TreeTentType.TENT || !checkAdjacentTents(board,cell.getLocation()))?"Does not contain a contradiction":null;
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

    public boolean checkAdjacentTents(TreeTentBoard board, Point loc){
        for(int i = -1;i <= 1; i++){
            for(int k = -1; k <= 1; k++){
                TreeTentCell cell = board.getCell(loc.x+i,loc.y+k);
                if((i!=0 || k!=0) && cell != null && cell.getType() == TreeTentType.TENT){
                    System.out.println("Contradiction: Touching tents");
                    return true;
                }
            }
        }
        return false;
    }

}
