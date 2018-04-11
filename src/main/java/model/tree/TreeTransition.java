package model.tree;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.Rule;
import model.rules.RuleType;

public class TreeTransition extends TreeElement
{
    private TreeNode parentNode;
    private TreeNode childNode;
    private Rule rule;

    /**
     * TreeTransition Constructor - create a transition from one node to another
     *
     * @param parentNode parentNode tree node associated with the transition
     * @param board board state of the transition
     */
    public TreeTransition(TreeNode parentNode, Board board)
    {
        super(TreeElementType.TRANSITION);
        this.parentNode = parentNode;
        this.childNode = null;
        this.board = board;
        this.rule = null;
    }

    /**
     * Recursively propagates the change of data down the tree
     *
     * @param data data of the change made
     */
    public void propagateChanges(ElementData data)
    {
        if(childNode != null)
        {
            childNode.getBoard().notifyChange(data.copy());
            for(TreeTransition child : childNode.getChildren())
            {
                child.getBoard().notifyChange(data.copy());
                child.propagateChanges(data);
            }
        }
    }

    /**
     * Determines if this tree node leads to a contradiction. Every path from this tree node
     * must lead to a contradiction including all of its children
     *
     * @return true if this tree node leads to a contradiction, false otherwise
     */
    public boolean leadsToContradiction()
    {
        if(isJustified() && isCorrect() && rule.getRuleType() == RuleType.CONTRADICTION)
        {
            return true;
        }
        else if(childNode == null)
        {
            return false;
        }
        else
        {
            return childNode.leadsToContradiction() && isJustified() && isCorrect();
        }
    }

    public boolean isValid()
    {
        return isJustified() && isCorrect() && childNode != null && childNode.isValid();
    }

    /**
     * Gets the parentNode tree node of this transition
     *
     * @return parentNode tree node
     */
    public TreeNode getParentNode()
    {
        return parentNode;
    }

    /**
     * Sets the parentNode tree node of this transition
     *
     * @param parentNode parentNode tree node
     */
    public void setParentNode(TreeNode parentNode)
    {
        this.parentNode = parentNode;
    }

    /**
     * Gets the childNode tree node of this transition
     *
     * @return childNode tree node
     */
    public TreeNode getChildNode()
    {
        return childNode;
    }

    /**
     * Sets the childNode tree node of this transition
     *
     * @param childNode childNode tree node
     */
    public void setChildNode(TreeNode childNode)
    {
        this.childNode = childNode;
    }

    /**
     * Gets the rule associated with this transition
     *
     * @return rule of this transition
     */
    public Rule getRule()
    {
        return rule;
    }

    /**
     * Sets the rule associated with this transition
     *
     * @param rule rule of this transition
     */
    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    /**
     * Gets whether this transition is correctly justified
     *
     * @return true if this transition is correctly justified, false otherwise
     */
    public boolean isCorrect()
    {
        return isJustified() && rule.checkRule(this) == null;
    }

    /**
     * Gets whether this transition is justified
     *
     * @return true if this transition is justified, false otherwise
     */
    public boolean isJustified()
    {
        return rule != null;
    }
}
