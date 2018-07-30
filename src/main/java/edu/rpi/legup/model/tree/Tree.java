package edu.rpi.legup.model.tree;

import edu.rpi.legup.model.gameboard.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * Tree Constructor - creates the tree structure with null root node
     */
    public Tree()
    {
        this.rootNode = null;
    }

    public TreeTransition addNewTransition(TreeNode treeNode)
    {
        TreeTransition transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
        treeNode.addChild(transition);
        return transition;
    }

    public TreeNode addNode(TreeTransition transition)
    {
        TreeNode treeNode = new TreeNode(transition.getBoard().copy());
        transition.setChildNode(treeNode);
        treeNode.setParent(transition);
        return treeNode;
    }

    public void removeTreeElement(TreeElement element)
    {
        if(element.getType() == TreeElementType.NODE)
        {
            TreeNode node = (TreeNode)element;
            node.getParent().setChildNode(null);
        }
        else
        {
            TreeTransition transition = (TreeTransition)element;
            transition.getParents().forEach(n -> n.removeChild(transition));
        }
    }

    /**
     * Determines if the tree is valid by checking whether this tree element and
     * all descendants of this tree element is justified and justified correctly
     *
     * @return true if tree is valid, false otherwise
     */
    public boolean isValid()
    {
        return rootNode.isValid();
    }

    /**
     * Gets a Set of TreeNodes that are leaf nodes
     *
     * @return Set of TreeNodes that are leaf nodes
     */
    public Set<TreeNode> getLeafNodes()
    {
        Set<TreeNode> leafs = new HashSet<>();
        getLeafNodes(leafs, rootNode);
        return leafs;
    }

    /**
     * Recursively gets a Set of TreeNodes that are leaf nodes
     *
     * @param leafs Set of TreeNodes that are leaf nodes
     * @param node current TreeNode being evaluated
     */
    private void getLeafNodes(Set<TreeNode> leafs, TreeNode node)
    {
        if(node == null)
        {
            return;
        }
        if(node.getChildren().isEmpty())
        {
            leafs.add(node);
        }
        else
        {
            for(TreeTransition transition : node.getChildren())
            {
                getLeafNodes(leafs, transition.getChildNode());
            }
        }
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
     * node such that the list of tree nodes specified are descendants of the node.
     * This will return null if no such ancestor exists
     *
     * @param nodes list of tree nodes to find the LCA
     * @return the first ancestor node that all tree nodes have in common, otherwise null if none exists
     */
    public TreeNode getLowestCommonAncestor(List<TreeNode> nodes)
    {
        if(nodes.isEmpty())
        {
            return null;
        }
        else if(nodes.size() == 1)
        {
            return nodes.get(0);
        }
        else
        {
            List<List<TreeNode>> ancestors = new ArrayList<>();
            for(TreeNode node : nodes)
            {
                ancestors.add(node.getAncestors());
            }

            List<TreeNode> first = ancestors.get(0);

            for(TreeNode node : first)
            {
                boolean isCommon = true;
                for(List<TreeNode> nList: ancestors)
                {
                    isCommon &= nList.contains(node);
                }

                if(isCommon)
                {
                    return node;
                }
            }
        }
        return null;
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
