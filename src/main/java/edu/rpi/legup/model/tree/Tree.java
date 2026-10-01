package edu.rpi.legup.model.tree;

import edu.rpi.legup.model.gameboard.Board;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * {@code Tree} constructor creates the tree structure from the initial {@code Board}.
     *
     * @param initBoard initial board
     */
    public Tree(@NotNull Board initBoard) {
        this.rootNode = new TreeNode(initBoard);
        this.rootNode.setRoot(true);
    }

    /** {@code Tree} constructor creates the tree structure with {@code null} root node. */
    public Tree() { this.rootNode = null; }

    /**
     * Adds a new transition to the specified node.
     *
     * @param treeNode the node to add a transition to
     * @return the created transition
     */
    public TreeTransition addNewTransition(@NotNull TreeNode treeNode) {
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
    public TreeElement addTreeElement(@NotNull TreeElement element) {
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
    public TreeElement addTreeElement(@NotNull TreeNode treeNode, @NotNull TreeTransition transition) {
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
    public TreeElement addTreeElement(@NotNull TreeTransition transition, @NotNull TreeNode treeNode) {
        transition.setChildNode(treeNode);
        treeNode.setParent(transition);
        return treeNode;
    }

    /**
     * Removes a tree element (node or transition) from the tree.
     *
     * @param element the tree element to remove
     */
    public void removeTreeElement(@NotNull TreeElement element) {
        if (element.getType() == TreeElementType.NODE) { removeTreeNode((TreeNode) element); }
        else { removeTreeTransition((TreeTransition) element); }
    }

    /**
     * Recursively removes a {@code TreeNode} from the tree.
     *
     * @param node {@code TreeNode} to remove subtree of
     */
    private void removeTreeNode(@NotNull TreeNode node) {
        while (!node.getChildren().isEmpty()) { removeTreeTransition(node.getChildren().getFirst()); }
        if (node.getParent() != null) {
            node.getParent().setChildNode(null);
            node.setParent(null);
        }
    }

    /**
     * Recursively removes a {@code TreeTransition} from the tree.
     *
     * @param transition {@code TreeTransition} to remove subtree of
     */
    private void removeTreeTransition(@NotNull TreeTransition transition) {
        if (transition.getChildNode() != null) { removeTreeNode(transition.getChildNode()); }
        for (TreeNode parent : transition.getParents()) {
            parent.removeChild(transition);
            parent.getChildren().forEach(TreeTransition::reverify);
        }
        transition.setParents(new ArrayList<>());
    }

    /**
     * Determines if the tree is valid by checking whether this tree element and all
     * descendants of this tree element is justified and justified correctly
     *
     * @return {@code true} if tree is valid; {@code false} otherwise
     */
    public boolean isValid() { return rootNode.isValidBranch(); }

    /**
     * Gets a {@code Set} of {@code TreeNodes} that are leaf nodes of the tree.
     *
     * @return {@code Set} of {@code TreeNodes} that are leaf nodes
     */
    public Set<TreeElement> getLeafTreeElements() {
        Set<TreeElement> leafs = new HashSet<>();
        getLeafTreeElements(leafs, rootNode);
        return leafs;
    }

    /**
     * Gets a {@code Set} of {@code TreeNodes} that are leaf nodes of the subtree rooted at the specified node.
     *
     * @param node root of the subtree
     * @return {@code Set} of {@code TreeNodes} that are leaf nodes of the subtree
     */
    public Set<TreeElement> getLeafTreeElements(@NotNull TreeNode node) {
        Set<TreeElement> leafs = new HashSet<>();
        getLeafTreeElements(leafs, node);
        return leafs;
    }

    /**
     * Recursively gets a {@code Set} of {@code TreeNodes} that are leaf nodes.
     *
     * @param leafs {@code Set} of {@code TreeNodes} that are leaf nodes
     * @param element current {@code TreeNode} being evaluated
     */
    private void getLeafTreeElements(@NotNull Set<TreeElement> leafs, @NotNull TreeElement element) {
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
     * Gets the lowest common ancestor (LCA) among the list of {@link TreeNode}s passed into the
     * function. This lowest common ancestor is the most immediate ancestor node such that the list
     * of tree nodes specified are descendants of the node.
     *
     * @param nodes list of tree nodes to find the LCA of
     * @return the first ancestor node that all tree nodes have in common
     * @throws IllegalArgumentException if {@code nodes} is empty or tree nodes do not all belong to the same tree
     */
    public static TreeNode getLowestCommonAncestor(@NotNull List<TreeNode> nodes) {
        if (!nodes.isEmpty()) {

            if (nodes.size() == 1) { return nodes.getFirst(); }
            else {
                List<List<TreeNode>> ancestors = new ArrayList<>();
                for (TreeNode node : nodes) { ancestors.add(node.getAncestors()); }

                List<TreeNode> first = ancestors.getFirst();

                for (TreeNode node : first) {

                    boolean isCommon = true;
                    for (List<TreeNode> nList : ancestors) {
                        if (!nList.contains(node)) {
                            isCommon = false;
                            break;
                        }
                    }

                    if (isCommon) { return node; }
                }
            }
        }

        throw new IllegalArgumentException("List of nodes is empty or the nodes do not all belong to the same tree.");
    }

    /**
     * Determines if the tree contains all contradictory branches (puzzle has no solution).
     *
     * @return {@code true} if the whole tree is contradictory; {@code false} otherwise
     */
    public boolean isContradictory() {
        for (TreeElement leaf : getLeafTreeElements()) {
            if (leaf.getType() != TreeElementType.NODE) {
                return false;
            }
            TreeNode node = (TreeNode) leaf;
            if (node.isRoot() || !node.getParent().isContradictoryBranch()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the root node of this tree.
     *
     * @return the root node of the tree
     */
    public TreeNode getRootNode() { return rootNode; }

    /**
     * Sets the root node of this tree.
     *
     * @param rootNode the root node of the tree
     */
    public void setRootNode(@Nullable TreeNode rootNode) { this.rootNode = rootNode; }

    /**
     * Checks if every leaf of the tree is a {@code TreeNode}.
     *
     * @return {@code true} if every leaf of the tree is a {@code TreeNode}; {@code false} otherwise
     */
    public boolean isClosed() {
        for (TreeElement leaf : getLeafTreeElements()) {
            if (leaf.getType() != TreeElementType.NODE) {
                return false;
            }
        }
        return true;
    }
}
