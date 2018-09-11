package puzzle.treetent.rules;

import model.gameboard.ElementData;
import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentType;

import java.util.ArrayList;

public class FinishWithGrassBasicRule extends BasicRule
{

    public FinishWithGrassBasicRule()
    {
        super("Finish with Grass", "Grass can be added to finish a row or column that has reached its tent limit.", "images/treetent/finishGrass.png");
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
        ContradictionRule contra1 = new TooManyTentsContradictionRule();

        TreeTentBoard destBoardState = (TreeTentBoard) transition.getBoard();
        TreeTentBoard origBoardState = (TreeTentBoard) transition.getParentNode().getBoard();

        TreeTentCell cell = (TreeTentCell)destBoardState.getElementData(elementIndex);

        if(cell.getType() != TreeTentType.GRASS){

            return "Only grass cells are allowed for this rule!";
        }
        ArrayList<ElementData> mod_data = destBoardState.getModifiedData();
        for(ElementData data: mod_data){
            if(data.getIndex() == -1){
                System.out.println("Link found at 3");
                return "Only grass cells are allowed for this rule!";
            }
        }

        TreeTentBoard modified = origBoardState.copy();
        TreeTentCell modCell = (TreeTentCell) modified.getElementData(elementIndex);
        modCell.setValueInt(TreeTentType.TENT.toValue());

        if(contra1.checkContradiction(new TreeTransition(null,modified)) == null){
            System.out.println("Valid");
            return null;
        } else{
            System.out.println("No contra");
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
