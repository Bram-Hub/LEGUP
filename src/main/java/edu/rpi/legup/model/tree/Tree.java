package edu.rpi.legup.model.tree;

import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a tree structure in a puzzle. The tree consists of {@link TreeNode}s and {@link
 * TreeTransition}s and allows adding, removing, and validating elements.
 */
public class Tree {
    private TreeNode rootNode;

    /**
     * Tree Constructor creates the tree structure from the initial {@link Board}
     *
     * @param initBoard initial board
     */
    public Tree(Board initBoard) {
        this.rootNode = new TreeNode(initBoard);
        this.rootNode.setRoot(true);
    }

    /** Tree Constructor creates the tree structure with null root node */
    public Tree() {
        this.rootNode = null;
    }

    /**
     * Adds a new transition to the specified node.
     *
     * @param treeNode the node to add a transition to
     * @return the created transition
     */
    public TreeTransition addNewTransition(TreeNode treeNode) {
        TreeTransition transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
        treeNode.addChild(transition);
        treeNode.getChildren().forEach(TreeTransition::reverify);
        return transition;
    }

    /**
     * Adds a tree element (node or transition) to the tree.
     *
     * @param element the tree element to add
     * @return the added tree element
     */
    public TreeElement addTreeElement(TreeElement element) {
        if (element.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) element;
            return addTreeElement(
                    treeNode, new TreeTransition(treeNode, treeNode.getBoard().copy()));
        } else {
            TreeTransition transition = (TreeTransition) element;
            Board copyBoard = transition.board.copy();
            copyBoard.setModifiable(true);
            return addTreeElement(transition, new TreeNode(copyBoard));
        }
    }

    /**
     * Adds a tree node and its associated transition to the tree.
     *
     * @param treeNode the tree node to add
     * @param transition the transition to associate with the node
     * @return the added transition
     */
    public TreeElement addTreeElement(TreeNode treeNode, TreeTransition transition) {
        treeNode.addChild(transition);
        treeNode.getChildren().forEach(TreeTransition::reverify);
        return transition;
    }

    /**
     * Adds a transition and its associated tree node to the tree.
     *
     * @param transition the transition to add
     * @param treeNode the tree node to associate with the transition
     * @return the added tree node
     */
    public TreeElement addTreeElement(TreeTransition transition, TreeNode treeNode) {
        transition.setChildNode(treeNode);
        treeNode.setParent(transition);
        return treeNode;
    }

    /**
     * Removes a tree element (node or transition) from the tree.
     *
     * @param element the tree element to remove
     */
    public void removeTreeElement(TreeElement element) {
        if (element.getType() == TreeElementType.NODE) {
            TreeNode node = (TreeNode) element;

            node.getParent().removeChild(node);
            node.getParent().setChildNode(null);
        } else {
            TreeTransition transition = (TreeTransition) element;

            transition.getParents().forEach(n -> n.removeChild(transition));
            TreeController treeController = new TreeController();
            TreeView treeView = new TreeView(treeController);
            treeView.removeTreeTransition(transition);
            transition.getParents().get(0).getChildren().forEach(TreeTransition::reverify);
        }
    }

    /**
     * Determines if the tree is valid by checking whether this tree puzzleElement and all
     * descendants of this tree puzzleElement is justified and justified correctly
     *
     * @return true if tree is valid, false otherwise
     */
    public boolean isValid() {
        return rootNode.isValidBranch();
    }

    /**
     * Gets a Set of TreeNodes that are leaf nodes
     *
     * @return Set of TreeNodes that are leaf nodes
     */
    public Set<TreeElement> getLeafTreeElements() {
        Set<TreeElement> leafs = new HashSet<>();
        getLeafTreeElements(leafs, rootNode);
        return leafs;
    }

    /**
     * Gets a Set of TreeNodes that are leaf nodes from the subtree rooted at the specified node
     *
     * @param node node that is input
     * @return Set of TreeNodes that are leaf nodes from the subtree
     */
    public Set<TreeElement> getLeafTreeElements(TreeNode node) {
        Set<TreeElement> leafs = new HashSet<>();
        getLeafTreeElements(leafs, node);
        return leafs;
    }

    /**
     * Recursively gets a Set of TreeNodes that are leaf nodes
     *
     * @param leafs Set of TreeNodes that are leaf nodes
     * @param element current TreeNode being evaluated
     */
    private void getLeafTreeElements(Set<TreeElement> leafs, TreeElement element) {
        if (element.getType() == TreeElementType.NODE) {
            TreeNode node = (TreeNode) element;
            List<TreeTransition> childTrans = node.getChildren();
            if (childTrans.isEmpty()) {
                leafs.add(node);
            } else {
                childTrans.forEach(t -> getLeafTreeElements(leafs, t));
            }
        } else {
            TreeTransition transition = (TreeTransition) element;
            TreeNode childNode = transition.getChildNode();
            if (childNode == null) {
                leafs.add(transition);
            } else {
                getLeafTreeElements(leafs, childNode);
            }
        }
    }

    /**
     * Gets the lowest common ancestor (LCA) among the list of {@link TreeNode} passed into the
     * function. This lowest common ancestor is the most immediate ancestor node such that the list
     * of tree nodes specified are descendants of the node. This will return null if no such
     * ancestor exists
     *
     * @param nodes list of tree nodes to find the LCA
     * @return the first ancestor node that all tree nodes have in common, otherwise null if none
     *     exists
     */
    public static TreeNode getLowestCommonAncestor(List<TreeNode> nodes) {
        if (nodes.isEmpty()) {
            return null;
        } else {
            if (nodes.size() == 1) {
                return nodes.get(0);
            } else {
                List<List<TreeNode>> ancestors = new ArrayList<>();
                for (TreeNode node : nodes) {
                    ancestors.add(node.getAncestors());
                }

                List<TreeNode> first = ancestors.get(0);

                for (TreeNode node : first) {
                    boolean isCommon = true;
                    for (List<TreeNode> nList : ancestors) {
                        isCommon &= nList.contains(node);
                    }

                    if (isCommon) {
                        return node;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Determines if the tree contains all contradictory branches (puzzle has no solution)
     *
     * @return true if the whole tree is contradictory, false otherwise
     */
    public boolean isContradictory() {
        for (TreeElement leaf : getLeafTreeElements()) {
            if (leaf.getType() != TreeElementType.NODE) {return false;}
            TreeNode node = (TreeNode) leaf;
            if (node.isRoot() || !node.getParent().isContradictoryBranch()) {return false;}
        }
        return true;
    }


    /**
     * Gets the root node of this tree
     *
     * @return the root node of the tree
     */
    public TreeNode getRootNode() {
        return rootNode;
    }

    /**
     * Sets the root node of this tree
     *
     * @param rootNode the root node of the tree
     */
    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
