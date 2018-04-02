package puzzle.lightup.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.CaseRule;
import model.tree.TreeTransition;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import puzzle.lightup.LightUpCellType;

import java.util.ArrayList;

public class LightOrEmptyCaseRule extends CaseRule
{

    public LightOrEmptyCaseRule()
    {
        super("Light or Empty", "Each blank cell is either a light or empty.", "images/lightup/cases/LightOrEmpty.png");
    }

    @Override
    public Board getCaseBoard(Board board)
    {
        LightUpBoard caseBoard = (LightUpBoard) board.copy();
        caseBoard.setCaseRule(this);
        caseBoard.setModifiable(false);
        for(ElementData data: caseBoard.getElementData())
        {
            if(((LightUpCell)data).getType() == LightUpCellType.UNKNOWN)
            {
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
        Board case1 = board.copy();
        ElementData data1 = case1.getElementData(elementIndex);
        data1.setValueInt(-4);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        ElementData data2 = case2.getElementData(elementIndex);
        data2.setValueInt(-3);
        case2.addModifiedData(data2);
        cases.add(case2);

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
}
