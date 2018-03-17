package model.tree;

import model.gameboard.Board;
import model.rules.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
        TreeTransition transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
        treeNode.getChildren().add(transition);
        return transition;
    }

    public TreeNode addNode(TreeTransition transition)
    {
        TreeNode treeNode = new TreeNode(transition.getBoard().copy());
        transition.setChildNode(treeNode);
        treeNode.addParent(transition);
        return treeNode;
    }

    public void removeTreeElement(TreeElement element)
    {
        if(element.getType() == TreeElementType.NODE)
        {
            TreeNode node = (TreeNode)element;
            for(TreeTransition transition: node.getParents())
            {
                transition.setChildNode(null);
            }
        }
        else
        {
            TreeTransition transition = (TreeTransition)element;
            transition.getParentNode().removeChild(transition);
        }
    }

    public boolean isValid()
    {
        return true;
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

    private ArrayList<TreeNode> getPathToRoot(TreeNode node)
    {
        ArrayList<TreeNode> path = new ArrayList<>();
        HashSet<TreeNode> it = new HashSet<>();
        it.add(node);

        path.add(node);
        while(!it.isEmpty())
        {
            Iterator<TreeNode> i = it.iterator();
            while(i.hasNext())
            {
                TreeNode next = i.next();
                for(TreeTransition transition : next.getParents())
                {
                    it.add(transition.getParentNode());
                }
                it.remove(next);

            }
        }
        return path;
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
