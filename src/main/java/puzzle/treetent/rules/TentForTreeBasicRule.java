package puzzle.treetent.rules;

import model.gameboard.ElementData;
import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.treetent.TreeTentBoard;
import puzzle.treetent.TreeTentCell;
import puzzle.treetent.TreeTentLine;
import puzzle.treetent.TreeTentType;

import java.awt.*;
import java.util.ArrayList;

public class TentForTreeBasicRule extends BasicRule
{

    public TentForTreeBasicRule()
    {
        super("Tent for Tree", "If only one unlinked tent and no blank cells are adjacent to an unlinked tree, the unlinked tree must link to the unlinked tent.", "images/treetent/NewTreeLink.png");
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

        if(elementIndex != -1){
            return "Only links are allowed for this rule!";
        }
        ArrayList<TreeTentLine> line_data = new ArrayList<TreeTentLine>();
        ArrayList<ElementData> mod_data = destBoardState.getModifiedData();
        for(ElementData data: mod_data){
            if(data.getIndex() != -1) {
                return "Only links are allowed for this rule";
            }
            else{
                Point c1 = ((TreeTentLine) data).getC1().getLocation();
                Point c2 = ((TreeTentLine) data).getC2().getLocation();
                if(origBoardState.getCell(c1.x,c1.y).getType()==TreeTentType.TENT){
                    TreeTentBoard modified = origBoardState.copy();
                    int index = origBoardState.getCell(c1.x,c1.y).getIndex();
                    TreeTentCell modCell = (TreeTentCell) modified.getElementData(index);
                    modCell.setValueInt(TreeTentType.GRASS.toValue());

                    if(contra1.checkContradiction(new TreeTransition(null,modified)) == null){
                        return null;
                    } else{
                        return "Does not contain a contradiction at this index";
                    }

                }
                else{
                    TreeTentBoard modified = origBoardState.copy();
                    int index = origBoardState.getCell(c2.x,c2.y).getIndex();
                    TreeTentCell modCell = (TreeTentCell) modified.getElementData(index);
                    modCell.setValueInt(TreeTentType.GRASS.toValue());

                    if(contra1.checkContradiction(new TreeTransition(null,modified)) == null){
                        return null;
                    } else{
                        return "Does not contain a contradiction at this index";
                    }

                }
                //return "Only grass cells are allowed for this rule!";
            }
        }
//        TreeTentBoard modified = origBoardState.copy();
//        TreeTentCell modCell = (TreeTentCell) modified.getElementData(elementIndex);
//        modCell.setValueInt(TreeTentType.TENT.toValue());
//
//        if(contra1.checkContradiction(new TreeTransition(null,modified)) == null){
//            return null;
//        } else{
//            return "Does not contain a contradiction at this index";
//        }
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
}
