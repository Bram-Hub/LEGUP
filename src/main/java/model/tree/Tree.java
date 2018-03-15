package model.tree;

import model.gameboard.Board;
import model.rules.Rule;

import java.util.ArrayList;

public class Tree
{
    private TreeNode rootNode;

    /**
     * Tree Constructor - creates the tree structure from the initial board
     *
     * @param initBoard initial board state
     */
    public Tree(Board initBoard)
    {
        this.rootNode = new TreeNode(initBoard);
        this.rootNode.setRoot(true);
    }

    public TreeTransition addNewTransition(TreeNode treeNode)
    {
//        if(treeNode.getChildren().size() == 0)
//        {
            TreeTransition transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
            treeNode.getChildren().add(transition);
            return transition;
//        }
//        return null;
    }

    public void addNode(TreeTransition transition)
    {
        if(transition.getChildNode() == null)
        {
            TreeNode treeNode = new TreeNode(transition.getBoard().copy());
            transition.setChildNode(treeNode);
        }
    }

    /**
     * Adds a tree node to the currently selected node. There must only be
     * one node currently selected and it must be a leaf node (no children)
     */
    public void addToSelected()
    {
//        if(selected.size() == 1)
//        {
//            TreeNode treeElement = getFirstSelected();
//            if(treeElement.getChildren().size() == 0)
//            {
//                TreeNode newNode = new TreeNode(treeElement.getBoard().copy(), null);
//                newNode.addParent(treeElement);
//                treeElement.addChild(newNode);
//                newTreeNodeSelection(newNode);
//            }
//        }
    }

    /**
     * Verifies that the selected tree node correctly uses the rule applied to it
     * This is called when a user presses a rule button on a selected node
     *
     * @param rule the rule to verify the selected node
     */
    public void verifySelected(Rule rule)
    {
//        if(selected.size() == 1)
//        {
//            TreeNode treeElement = getFirstSelected();
//            if(treeElement.getParents().size() == 1)
//            {
//                TreeNode parent = treeElement.getParents().get(0);
//                treeElement.setRule(rule);
//                if(rule.checkRule(parent.getBoard(), treeElement.getBoard()) == null)
//                {
//                    treeElement.setCorrect(true);
//                }
//                else
//                {
//                    treeElement.setCorrect(false);
//                }
//            }
//        }
    }

    /**
     * Deletes the selected tree nodes and its branch from the tree
     * and makes the currently selected node the lowest common ancestor
     * among all the selected nodes
     */
    public void deleteSelected()
    {
//        TreeNode newSelected = getLowestCommonAncestor(new ArrayList<>(selected));
//        for(TreeNode treeElement: selected)
//        {
//            for(TreeNode parent : treeElement.getParents())
//            {
//                parent.getChildren().remove(treeElement);
//            }
//        }
//        newTreeNodeSelection(newSelected);
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
//        if(ancestor == null || descendant == null)
//        {
//            return false;
//        }
//        else
//        {
//            TreeNode node = descendant.getParents().get(0);
//            while(node != null)
//            {
//                if(node == ancestor)
//                {
//                    return true;
//                }
//                node = node.getParents().get(0);
//            }
//            return false;
//        }
        return false;
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
