package edu.rpi.legup.model.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.ArrayList;

import static edu.rpi.legup.model.rules.RuleType.MERGE;

public class MergeRule extends Rule
{
    /**
     * MergeRule Constructor - merges to board states together
     */
    public MergeRule()
    {
        super("Merge Rule", "Merge any number of nodes into one", "edu/rpi/legup/images/Legup/MergeRule.png");
        this.ruleType = MERGE;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule.
     * This method is the one that should overridden in child classes
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     * This method is the one that should overridden in child classes
     *
     * @param transition   transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return null;
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
        for(PuzzleElement puzzleElement : mergeBoard.getPuzzleElements())
        {
            boolean allSame = true;
            for(TreeNode n : nodes)
            {
                allSame &= puzzleElement.equalsData(n.getBoard().getPuzzleElement(puzzleElement));
            }
            if(!allSame)
            {

            }
        }
        return mergeBoard;
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
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement)
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
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param puzzleElement equivalent puzzleElement
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
    }
}
