package model.tree;

import model.gameboard.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class TreeNode extends TreeElement
{
    private TreeTransition parent;
    private ArrayList<TreeTransition> children;
    private boolean isRoot;

    /**
     * TreeNode Constructor - Creates a tree node whenever a rule has been made
     *
     * @param board board associated with this tree node
     */
    public TreeNode(Board board)
    {
        super(TreeElementType.NODE);
        this.board = board;
        this.parent = null;
        this.children = new ArrayList<>();
        this.isRoot = false;
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
        boolean leadsToContra = true;
        for(TreeTransition child: children)
        {
            leadsToContra &= child.isContradictoryBranch();
        }
        return leadsToContra && !children.isEmpty();
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
        for(TreeTransition transition : children)
        {
            if(!transition.isValid())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets all of the ancestors of the this node
     *
     * @return list of all of the ancestors for this node
     */
    public ArrayList<TreeNode> getAncestors()
    {
        ArrayList<TreeNode> ancestors = new ArrayList<>();
        HashSet<TreeNode> it = new HashSet<>();
        it.add(this);

        while(!it.isEmpty())
        {
            Iterator<TreeNode> i = it.iterator();
            while(i.hasNext())
            {
                TreeNode next = i.next();
                if(next.getParent() != null)
                {
                    it.addAll(next.getParent().getParents());
                }
                ancestors.add(next);
                it.remove(next);
            }
        }
        return ancestors;
    }

    /**
     * Determines if the specified tree transition is a parent of this node
     *
     * @param parent tree transition that could be a parent
     * @return true if the specified tree transition is a parent of this node, false otherwise
     */
    public boolean isParent(TreeTransition parent)
    {
        return this.parent == parent;
    }

    /**
     * Adds a child to this tree node
     *
     * @param child child to add
     */
    public void addChild(TreeTransition child)
    {
        children.add(child);
    }

    /**
     * Removes a child to this tree node
     *
     * @param child child to remove
     */
    public void removeChild(TreeTransition child)
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
     * @return the TreeNode's parent
     */
    public TreeTransition getParent()
    {
        return parent;
    }

    /**
     * Sets the TreeNode's parent
     *
     * @param parent the TreeNode's parent
     */
    public void setParent(TreeTransition parent)
    {
        this.parent = parent;
    }

    /**
     * Gets the TreeNode's children
     *
     * @return the TreeNode's children
     */
    public ArrayList<TreeTransition> getChildren()
    {
        return children;
    }

    /**
     * Sets the TreeNode's children
     *
     * @param children the TreeNode's children
     */
    public void setChildren(ArrayList<TreeTransition> children)
    {
        this.children = children;
    }

    /**
     * Is this node the root of the tree
     *
     * @return true if this node is the root of the tree, false otherwise
     */
    public boolean isRoot()
    {
        return isRoot;
    }

    /**
     * Sets the root of the tree
     *
     * @param isRoot true if this node is the root of the tree, false otherwise
     */
    public void setRoot(boolean isRoot)
    {
        this.isRoot = isRoot;
    }
}