package model.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.TreeNode;
import model.tree.TreeTransition;

import java.util.ArrayList;

import static model.rules.RuleType.MERGE;

public class MergeRule extends Rule
{
    /**
     * MergeRule Constructor - merges to board states together
     */
    public MergeRule()
    {
        super(null, null, null);
        this.ruleType = MERGE;
    }

    public Board getMergedBoard(ArrayList<TreeNode> nodes)
    {
        if(nodes.isEmpty())
        {
            return null;
        }
        else if(nodes.size() == 1)
        {
            return nodes.get(0).getBoard().copy();
        }
        Board mergeBoard = nodes.get(0).getBoard().copy();
        for(ElementData data : mergeBoard.getElementData())
        {
            boolean allSame = true;
            for(TreeNode n : nodes)
            {
                allSame &= data.equals(n.getBoard().getElementData(data.getIndex()));
            }
            if(!allSame)
            {

            }
        }
        return mergeBoard;
    }

    public String canMergeNodes(ArrayList<TreeNode> nodes)
    {
        boolean allLeafNode = true;
        for(TreeNode n : nodes)
        {
            allLeafNode &= n.getChildren().isEmpty();
        }
        if(!allLeafNode)
        {
            return "All nodes must be leaf nodes";
        }
        return null;
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
