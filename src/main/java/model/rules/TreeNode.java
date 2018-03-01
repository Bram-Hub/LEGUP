package model.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;

import java.awt.*;
import java.util.ArrayList;

public class TreeNode
{
    private Board board;
    private ArrayList<TreeNode> parents;
    private ArrayList<TreeNode> children;
    private Rule rule;
    private boolean isCorrect;
    private boolean isStateSelected;

    /**
     * TreeNode Constructor - Creates a tree node whenever a rule has been made
     *
     * @param board board associated with this tree node
     * @param rule rule that was used to get from the parent(s) to this tree node
     */
    public TreeNode(Board board, Rule rule)
    {
        this.board = board;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.rule = rule;
        this.isCorrect = false;
        this.isStateSelected = false;
    }

    /**
     * Propagates the changes of data into the children boards
     *
     * @param index index of the data to propagate changes
     */
    public void propagateChanges(int index)
    {
        propagateChanges(board.getElementData(index));
    }

    private void propagateChanges(ElementData data)
    {
        for(TreeNode child: children)
        {
            child.propagateChanges(data);
            child.getBoard().setElementData(data.getIndex(), data.copy());
            if(child.getRule() != null)
                child.setCorrect(child.getRule().checkRule(board, child.getBoard()) == null);
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
        if(rule.getRuleType() == RuleType.CONTRADICTION && isCorrect)
        {
            return true;
        }
        else
        {
            boolean leadsToContra = true;
            for(TreeNode child: children)
            {
                leadsToContra &= child.leadsToContradiction();
            }
            return leadsToContra;
        }
    }

    /**
     * Gets the board at this TreeNode
     *
     * @return the board at this TreeNode
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * Sets the board at this TreeNOde
     *
     * @param board the board at this TreeNode
     */
    public void setBoard(Board board)
    {
        this.board = board;
    }

    /**
     * Adds a parent to this tree node
     *
     * @param parent parent to add
     */
    public void addParent(TreeNode parent)
    {
        parents.add(parent);
    }

    /**
     * Removes a parent to this tree node
     *
     * @param parent parent to remove
     */
    public void removeParent(TreeNode parent)
    {
        parents.remove(parent);
    }

    /**
     * Determines if the specified tree node is a parent of this node
     *
     * @param parent tree node that could be a parent
     * @return true if the specified tree node is a panrent of this node, false otherwise
     */
    public boolean isParent(TreeNode parent)
    {
        return children.contains(parent);
    }

    /**
     * Adds a child to this tree node
     *
     * @param child child to add
     */
    public void addChild(TreeNode child)
    {
        children.add(child);
    }

    /**
     * Removes a child to this tree node
     *
     * @param child child to remove
     */
    public void removeChild(TreeNode child)
    {
        children.remove(child);
    }

    /**
     * Determines if the specified tree node is a child of this node
     *
     * @param child tree node that could be a child
     * @return true if the specified tree node is a child of this node, false otherwise
     */
    public boolean isChild(TreeNode child)
    {
        return children.contains(child);
    }

    /**
     * Gets the TreeNode's parent
     *
     * @return the TreeNode's parents
     */
    public ArrayList<TreeNode> getParents()
    {
        return parents;
    }

    /**
     * Sets the TreeNode's parent
     *
     * @param parent the TreeNode's parents
     */
    public void setParents(ArrayList<TreeNode> parent)
    {
        this.parents = parent;
    }

    /**
     * Gets the TreeNode's children
     *
     * @return the TreeNode's children
     */
    public ArrayList<TreeNode> getChildren()
    {
        return children;
    }

    /**
     * Sets the TreeNode's children
     *
     * @param children the TreeNode's children
     */
    public void setChildren(ArrayList<TreeNode> children)
    {
        this.children = children;
    }

    /**
     * Gets the TreeNode's rule
     *
     * @return the TreeNode's rule
     */
    public Rule getRule()
    {
        return rule;
    }

    /**
     * Sets the TreeNode's rule
     *
     * @param rule the TreeNode's rule
     */
    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    /**
     * Is the rule correctly applied
     *
     * @return true if the rule was correctly applied, false otherwise
     */
    public boolean isCorrect()
    {
        return isCorrect;
    }

    /**
     * Sets if the rule was correctly applied
     *
     * @param isCorrect true if the rule was correctly applied, false otherwise
     */
    public void setCorrect(boolean isCorrect)
    {
        this.isCorrect = isCorrect;
    }

    /**
     * Determines if this TreeNode is justified by testing to see if there
     * has been a Rule applied to this TreeNode
     *
     * @return
     */
    public boolean isJustified()
    {
        return rule != null;
    }

    /**
     * Is the board state selected
     *
     * @return true if the board state is selected, false otherwise
     */
    public boolean isStateSelected()
    {
        return isStateSelected;
    }

    /**
     * Sets the board state selected field
     *
     * @param isStateSelected true if the board state is selected, false otherwise
     */
    public void setStateSelected(boolean isStateSelected)
    {
        this.isStateSelected = isStateSelected;
    }
}

