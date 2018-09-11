package puzzle.treetent.rules;

import model.gameboard.ElementData;
import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentType;

import java.util.ArrayList;

public class LastCampingSpotBasicRule extends BasicRule
{

    public LastCampingSpotBasicRule()
    {
        super("Last Camping Spot", "If an unlinked tree is adjacent to only one blank cell and not adjacent to any unlinked tents, the blank cell must be a tent.", "images/treetent/oneTentPosition.png");
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
        ContradictionRule contra1 = new NoTentForTreeContradictionRule();

        TreeTentBoard destBoardState = (TreeTentBoard) transition.getBoard();
        TreeTentBoard origBoardState = (TreeTentBoard) transition.getParentNode().getBoard();

        TreeTentCell cell = (TreeTentCell)destBoardState.getElementData(elementIndex);

        if(cell.getType() != TreeTentType.TENT){
            return "Only tent cells are allowed for this rule!";
        }
        ArrayList<ElementData> mod_data = destBoardState.getModifiedData();
        for(ElementData data: mod_data){
            if(data.getIndex() == -1){
                System.out.println("Link found at 4");
                return "Only tent cells are allowed for this rule!";
            }
        }

        TreeTentBoard modified = origBoardState.copy();
        TreeTentCell modCell = (TreeTentCell) modified.getElementData(elementIndex);

        modCell.setValueInt(TreeTentType.GRASS.toValue());

        if(contra1.checkContradiction(new TreeTransition(null,modified)) == null){
            return null;
        } else{
            return "Does not contain a contradiction at this index";
        }
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
