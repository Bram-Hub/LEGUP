package edu.rpi.legup.model.tree;

import edu.rpi.legup.model.gameboard.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * Tree Constructor creates the tree structure with null root node
     */
    public Tree() {
        this.rootNode = null;
    }

    public TreeTransition addNewTransition(TreeNode treeNode) {
        TreeTransition transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
        treeNode.addChild(transition);
        treeNode.getChildren().forEach(TreeTransition::reverify);
        return transition;
    }

    public TreeNode addNode(TreeTransition transition) {
        TreeNode treeNode = new TreeNode(transition.getBoard().copy());
        transition.setChildNode(treeNode);
        treeNode.setParent(transition);
        return treeNode;
    }

    public TreeElement addTreeElement(TreeElement element) {
        if (element.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) element;
            return addTreeElement(treeNode, new TreeTransition(treeNode, treeNode.getBoard().copy()));
        }
        else {
            TreeTransition transition = (TreeTransition) element;
            Board copyBoard = transition.board.copy();
            copyBoard.setModifiable(false);
            return addTreeElement(transition, new TreeNode(copyBoard));
        }
    }

    public TreeElement addTreeElement(TreeNode treeNode, TreeTransition transition) {
        treeNode.addChild(transition);
        treeNode.getChildren().forEach(TreeTransition::reverify);
        return transition;
    }

    public TreeElement addTreeElement(TreeTransition transition, TreeNode treeNode) {
        transition.setChildNode(treeNode);
        treeNode.setParent(transition);
        return treeNode;
    }

    public void removeTreeElement(TreeElement element) {
        if (element.getType() == TreeElementType.NODE) {
            TreeNode node = (TreeNode) element;
            node.getParent().setChildNode(null);
        }
        else {
            TreeTransition transition = (TreeTransition) element;
            transition.getParents().forEach(n -> n.removeChild(transition));
            transition.getParents().get(0).getChildren().forEach(TreeTransition::reverify);
        }
    }

    /**
     * Determines if the tree is valid by checking whether this tree puzzleElement and
     * all descendants of this tree puzzleElement is justified and justified correctly
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
     * Gets a Set of TreeNodes that are leaf nodes from the sub tree rooted at the specified node
     * @param node node that is input
     * @return Set of TreeNodes that are leaf nodes from the sub tree
     */
    public Set<TreeElement> getLeafTreeElements(TreeNode node) {
        Set<TreeElement> leafs = new HashSet<>();
        getLeafTreeElements(leafs, node);
        return leafs;
    }

    /**
     * Recursively gets a Set of TreeNodes that are leaf nodes
     *
     * @param leafs   Set of TreeNodes that are leaf nodes
     * @param element current TreeNode being evaluated
     */
    private void getLeafTreeElements(Set<TreeElement> leafs, TreeElement element) {
        if (element.getType() == TreeElementType.NODE) {
            TreeNode node = (TreeNode) element;
            List<TreeTransition> childTrans = node.getChildren();
            if (childTrans.isEmpty()) {
                leafs.add(node);
            }
            else {
                childTrans.forEach(t -> getLeafTreeElements(leafs, t));
            }
        }
        else {
            TreeTransition transition = (TreeTransition) element;
            TreeNode childNode = transition.getChildNode();
            if (childNode == null) {
                leafs.add(transition);
            }
            else {
                getLeafTreeElements(leafs, childNode);
            }
        }
    }

    /**
     * Gets the lowest common ancestor (LCA) among the list of {@link TreeNode} passed into
     * the function. This lowest common ancestor is the most immediate ancestor
     * node such that the list of tree nodes specified are descendants of the node.
     * This will return null if no such ancestor exists
     *
     * @param nodes list of tree nodes to find the LCA
     * @return the first ancestor node that all tree nodes have in common, otherwise null if none exists
     */
    public static TreeNode getLowestCommonAncestor(List<TreeNode> nodes) {
        if (nodes.isEmpty()) {
            return null;
        }
        else {
            if (nodes.size() == 1) {
                return nodes.get(0);
            }
            else {
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
