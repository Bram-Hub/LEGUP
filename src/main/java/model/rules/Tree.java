package model.rules;

import app.GameBoardFacade;
import model.gameboard.Board;

import java.util.ArrayList;
import java.util.HashSet;

public class Tree
{
    private TreeNode rootNode;
    private HashSet<TreeNode> selected;

    /**
     * Tree Constructor - creates the tree structure from the initial board
     *
     * @param initBoard initial board state
     */
    public Tree(Board initBoard)
    {
        this.rootNode = new TreeNode(initBoard, null);
        this.selected = new HashSet<>();
        selected.add(rootNode);
    }

    /**
     * Gets the first selected TreeNode
     *
     * @return first selected TreeNode
     */
    public TreeNode getFirstSelected()
    {
        return selected.iterator().next();
    }

    /**
     * Adds a tree node to the currently selected node. There must only be
     * one node currently selected and it must be a leaf node (no children)
     */
    public void addToSelected()
    {
        if(selected.size() == 1)
        {
            TreeNode treeNode = getFirstSelected();
            if(treeNode.getChildren().size() == 0)
            {
                TreeNode newNode = new TreeNode(treeNode.getBoard().copy(), null);
                newNode.addParent(treeNode);
                treeNode.addChild(newNode);
                newTreeNodeSelection(newNode);
            }
        }
    }

    /**
     * Verifies that the selected tree node correctly uses the rule applied to it
     * This is called when a user presses a rule button on a selected node
     *
     * @param rule the rule to verify the selected node
     */
    public void verifySelected(Rule rule)
    {
        if(selected.size() == 1)
        {
            TreeNode treeNode = getFirstSelected();
            if(treeNode.getParents().size() == 1)
            {
                TreeNode parent = treeNode.getParents().get(0);
                treeNode.setRule(rule);
                if(rule.checkRule(parent.getBoard(), treeNode.getBoard()) == null)
                {
                    treeNode.setCorrect(true);
                }
                else
                {
                    treeNode.setCorrect(false);
                }
            }
        }
    }

    /**
     * Deletes the selected tree nodes and its branch from the tree
     * and makes the currently selected node the lowest common ancestor
     * among all the selected nodes
     */
    public void deleteSelected()
    {
        TreeNode newSelected = getLowestCommonAncestor(new ArrayList<>(selected));
        for(TreeNode treeNode: selected)
        {
            for(TreeNode parent : treeNode.getParents())
            {
                parent.getChildren().remove(treeNode);
            }
        }
        newTreeNodeSelection(newSelected);
    }

    /**
     * Determines if the list of selected tree nodes can be merged into one node. All
     * nodes in the list must be at the same depth, all branches must be valid,
     * and all nodes must be apart of the same immediate case rule node
     *
     * @return true if the list of tree nodes can be merged, false otherwise
     */
    public boolean canMerge()
    {
        return true;
    }

    /**
     * Merges the list of selected tree nodes if possible
     */
    public void mergeNodes()
    {
        if(canMerge())
        {
            Board mergedBoard;
        }
    }

    /**
     * Gets the lowest common ancestor (LCA) among the list of nodes passed into
     * the function. This lowest common ancestor is the most immediate ancestor
     * node such that all nodes are descendants. This will return null if
     * no such ancestor exists
     *
     * @param nodes list of tree nodes to find the LCA
     * @return the first ancestor node that all tree nodes have in common, otherwise null if none exists
     */
    public TreeNode getLowestCommonAncestor(ArrayList<TreeNode> nodes)
    {
        return null;
    }

    /**
     * Determines if the descendant is indeed a descendant of the ancestor
     *
     * @param ancestor possible ancestor
     * @param descendant possible descendant
     * @return true if the descendant is a descendant of the ancestor, false otherwise
     */
    public boolean isAncestor(TreeNode ancestor, TreeNode descendant)
    {
        //If either the ancestor or the descendant is null, return false
        if(ancestor == null || descendant == null)
        {
            return false;
        }
        else
        {
            TreeNode node = descendant.getParents().get(0);
            while(node != null)
            {
                if(node == ancestor)
                {
                    return true;
                }
                node = node.getParents().get(0);
            }
            return false;
        }
    }

    /**
     * Determines if the TreeNode specified is currently selected
     *
     * @param treeNode tree node
     * @return true if the TreeNode is currently selected, false otherwise
     */
    public boolean isSelected(TreeNode treeNode)
    {
        return selected.contains(treeNode);
    }

    /**
     * Toggles a tree node selection
     *
     * @param treeNode the tree node selection to toggle
     */
    public void toggleTreeNodeSelection(TreeNode treeNode)
    {
        if(selected.contains(treeNode))
        {
            selected.remove(treeNode);
        }
        else
        {
            selected.add(treeNode);
        }
    }

    /**
     * Adds a new tree node selection and clears the old selection
     *
     * @param treeNode tree node that was selected
     */
    public void newTreeNodeSelection(TreeNode treeNode)
    {
        selected.clear();
        selected.add(treeNode);
    }

    /**
     * Gets the set of selected tree nodes
     *
     * @return set of selected tree nodes
     */
    public HashSet<TreeNode> getSelected()
    {
        return selected;
    }

    /**
     * Gets the root node of this tree
     *
     * @return the root node of the tree
     */
    public TreeNode getRootNode()
    {
        return rootNode;
    }

    /**
     * Sets the root node of this tree
     *
     * @param rootNode the root node of the tree
     */
    public void setRootNode(TreeNode rootNode)
    {
        this.rootNode = rootNode;
    }
}
