package puzzle.treetent.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentType;

public class TooFewTentsContradictionRule extends ContradictionRule
{

    public TooFewTentsContradictionRule()
    {
        super("Too Few Tents", "Rows and columns cannot have fewer tents than their clue.", "images/treetent/too_few_tents.png");
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
        int x = cell.getLocation().x;
        int y = cell.getLocation().y;
        int rowTentCount = 0;
        int colTentCount = 0;
        int rowLimit = board.getEast().get(x).getValueInt();
        int colLimit = board.getSouth().get(y).getValueInt();
        for(int i = 0; i < board.getWidth();i++){
            if(board.getCell(x,i).getType() == TreeTentType.TENT || board.getCell(x,i).getType() == TreeTentType.UNKNOWN)
                rowTentCount += 1;
        }
        for(int i = 0; i < board.getHeight(); i++){
            if(board.getCell(i,y).getType() == TreeTentType.TENT || board.getCell(x,i).getType() == TreeTentType.UNKNOWN)
                colTentCount += 1;
        }
        return(rowTentCount < rowLimit || colTentCount < colLimit)?null:"Does not contain a contradiction";
//        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
//        TreeTentCell cell = (TreeTentCell)board.getElementData(elementIndex);
//        int tentCount = 0;
//        for(int i = 0; i < board.getWidth();i++){
//            int tentLimit = board.getSouth().get(i).getValueInt();
//            tentCount = 0;
//            for(int k = 0; k < board.getHeight();k++){
//                TreeTentType cellType = board.getCell(i,k).getType();
//                if(cellType != TreeTentType.GRASS) {
//                    tentCount += 1;
//                }
//            }
//            if(tentCount < tentLimit){
//                System.out.println("Contradiction: Too few tents in the column "+tentCount );
//                return null;
//            }
//            //System.out.println("Tent limit at " + i  + " is " + tentLimit);
//        }
//        for(int i = 0; i < board.getHeight();i++){
//            int tentLimit = board.getEast().get(i).getValueInt();
//            tentCount = 0;
//            for(int k = 0; k < board.getWidth();k++){
//                TreeTentType cellType = board.getCell(i,k).getType();
//                if(cellType != TreeTentType.GRASS) {
//                    tentCount += 1;
//                }
//            }
//            if(tentCount < tentLimit){
//                System.out.println("Contradiction: Too few tents in the row");
//                return null;
//            }
//        }
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
