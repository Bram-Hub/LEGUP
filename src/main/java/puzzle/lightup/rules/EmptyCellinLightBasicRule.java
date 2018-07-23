package puzzle.lightup.rules;

import model.gameboard.Element;
import model.rules.BasicRule;
import model.rules.RegisterRule;
import model.rules.RuleType;
import model.tree.TreeTransition;
import puzzle.lightup.LightUp;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import puzzle.lightup.LightUpCellType;


@RegisterRule(puzzleName = LightUp.class, ruleType = RuleType.BASIC)
public class EmptyCellinLightBasicRule extends BasicRule
{

    public EmptyCellinLightBasicRule()
    {
        super("Empty Cells in Light", "Cells in light must be empty.", "images/lightup/rules/EmptyCellInLight.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param element index of the element
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, Element element)
    {
        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        initialBoard.fillWithLight();
        LightUpCell initCell = (LightUpCell) initialBoard.getElementData(element);
        LightUpCell finalCell = (LightUpCell) transition.getBoard().getElementData(element);
        if(finalCell.getType() == LightUpCellType.EMPTY && initCell.getType() == LightUpCellType.UNKNOWN && initCell.isLite())
        {
            return null;
        }
        return "Cell is not forced to be empty";
    }

    /**
     * Checks whether the child node logically follows from the parent node using this rule
     * and if so will perform the default application of the rule
     *
     * @param transition transition to apply default application
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
     * @param element
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, Element element)
    {
        return false;
    }
}
