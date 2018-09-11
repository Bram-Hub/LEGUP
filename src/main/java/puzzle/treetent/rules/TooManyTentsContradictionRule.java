package puzzle.treetent.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentType;

public class TooManyTentsContradictionRule extends ContradictionRule
{

    public TooManyTentsContradictionRule()
    {
        super("Too Many Tents", "Rows and columns cannot have more tents than their clue.", "images/treetent/too_many_tents.png");
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
        int tentCount = 0;
        for(int i = 0; i < board.getWidth();i++){
            int tentLimit = board.getSouth().get(i).getValueInt();
            tentCount = 0;
            for(int k = 0; k < board.getHeight();k++){
                TreeTentType cellType = board.getCell(i,k).getType();
                if(cellType == TreeTentType.TENT) {
                    tentCount += 1;
                }
            }
            if(tentCount > tentLimit){
                //System.out.println("Contradiction: Too many tents in the column " + tentCount + " " + tentLimit);
                return null;
            }
            //System.out.println("Tent limit at " + i  + " is " + tentLimit);
        }
        for(int i = 0; i < board.getHeight();i++){
            int tentLimit = board.getEast().get(i).getValueInt();
            tentCount = 0;
            for(int k = 0; k < board.getWidth();k++){
                TreeTentType cellType = board.getCell(k,i).getType();
                if(cellType == TreeTentType.TENT) {
                    tentCount += 1;
                }
            }
            if(tentCount > tentLimit){
                //System.out.println("Contradiction: Too many tents in the row " + tentCount + " " + tentLimit);
                return null;
            }
        }
        return "Does not contain a contradiction";
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
}
