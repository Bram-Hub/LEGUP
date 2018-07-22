package puzzle.nurikabe.rules;

import model.gameboard.Board;
import model.gameboard.CaseBoard;
import model.gameboard.ElementData;
import model.rules.CaseRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;

import java.util.ArrayList;

public class BlackOrWhiteCaseRule extends CaseRule
{

    public BlackOrWhiteCaseRule()
    {
        super("Black or White", "Each blank cell is either black or white.", "images/nurikabe/cases/BlackOrWhite.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board)
    {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(nurikabeBoard, this);
        nurikabeBoard.setModifiable(false);
        for(ElementData data: nurikabeBoard.getElementData())
        {
            if(((NurikabeCell)data).getType() == NurikabeType.UNKNOWN)
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
        data1.setValueInt(NurikabeType.WHITE.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        ElementData data2 = case2.getElementData(elementIndex);
        data2.setValueInt(NurikabeType.BLACK.toValue());
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
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
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
