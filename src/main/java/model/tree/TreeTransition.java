package model.tree;

import model.gameboard.Board;
import model.gameboard.Element;
import model.rules.Rule;
import model.rules.RuleType;

import java.util.ArrayList;

public class TreeTransition extends TreeElement
{
    private ArrayList<TreeNode> parents;
    private TreeNode childNode;
    private Rule rule;
    private boolean isCorrect;
    private boolean isVerified;

    /**
     * TreeTransition Constructor - create a transition from one node to another
     *
     * @param board board state of the transition
     */
    public TreeTransition(Board board)
    {
        super(TreeElementType.TRANSITION);
        this.parents = new ArrayList<>();
        this.childNode = null;
        this.board = board;
        this.rule = null;
        this.isCorrect = false;
        this.isVerified = false;
    }

    /**
     * TreeTransition Constructor - create a transition from one node to another
     *
     * @param parent parent tree node associated with the transition
     * @param board board state of the transition
     */
    public TreeTransition(TreeNode parent, Board board)
    {
        this(board);
        this.parents.add(parent);
    }

    /**
     * Recursively propagates the change of element down the tree
     *
     * @param data element of the change made
     */
    public void propagateChanges(Element data)
    {
        board.notifyChange(data);
        isVerified = false;
        if(childNode != null)
        {
            childNode.getBoard().notifyChange(data.copy());
            for(TreeTransition child : childNode.getChildren())
            {
                child.propagateChanges(data.copy());
            }
        }
    }

    /**
     * Determines if this tree node leads to a contradiction. Every path from this tree node
     * must lead to a contradiction including all of its children
     *
     * @return true if this tree node leads to a contradiction, false otherwise
     */
    @Override
    public boolean isContradictoryBranch()
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
            return childNode.isContradictoryBranch() && isJustified() && isCorrect();
        }
    }

    /**
     * Recursively determines if the sub tree rooted at this tree element is valid by checking
     * whether this tree element and all descendants of this tree element is justified
     * and justified correctly
     *
     * @return true if this tree element and all descendants of this tree element is valid,
     * false otherwise
     */
    @Override
    public boolean isValid()
    {
        return isJustified() && isCorrect() && childNode != null && childNode.isValid();
    }

    /**
     * Gets the parent tree nodes of this transition
     *
     * @return parent tree nodes of this tree transition
     */
    public ArrayList<TreeNode> getParents()
    {
        return parents;
    }

    /**
     * Sets the parent tree nodes of this transition
     *
     * @param parents parents tree nodes of this tree transition
     */
    public void setParents(ArrayList<TreeNode> parents)
    {
        this.parents = parents;
    }

    /**
     * Adds a parent tree node to this tree transition
     *
     * @param parent parent tree node to add
     */
    public void addParent(TreeNode parent)
    {
        parents.add(parent);
    }

    /**
     * Removes a parent tree node to this tree transition
     *
     * @param parent parent tree node to remove
     */
    public void removeParent(TreeNode parent)
    {
        parents.remove(parent);
    }

    /**
     * Determines if the specified tree node is a parent of this transition
     *
     * @param parent tree node that could be a parent
     * @return true if the specified tree node is a parent of this transition, false otherwise
     */
    public boolean isParent(TreeNode parent)
    {
        return parents.contains(parent);
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
        isVerified = false;
    }

    /**
     * Gets whether this transition is correctly justified
     *
     * @return true if this transition is correctly justified, false otherwise
     */
    public boolean isCorrect()
    {
        if(!isVerified)
        {
            isCorrect = rule.checkRule(this) == null;
            isVerified = true;
        }
        return isJustified() && isCorrect;
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
