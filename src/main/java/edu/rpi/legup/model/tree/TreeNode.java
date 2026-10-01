package edu.rpi.legup.model.tree;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.utility.DisjointSets;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents a node in a tree structure. Extends {@link TreeElement}. A {@code TreeNode} contains a
 * board, references to its parent and children transitions, and indicates if it is the root node of
 * the tree.
 */
public class TreeNode extends TreeElement {

    private TreeTransition parent;
    private List<TreeTransition> children;
    private boolean isRoot;

    /**
     * {@code TreeNode} constructor creates a tree node whenever a rule has been made.
     *
     * @param board board associated with this tree node
     */
    public TreeNode(@NotNull Board board) {
        super(TreeElementType.NODE);
        this.board = board;
        this.parent = null;
        this.children = new ArrayList<>();
        this.isRoot = false;
    }

    /**
     * Determines if this tree node leads to a contradiction. Every path from this tree node must
     * lead to a contradiction including all of its children.
     *
     * @return {@code true} if this tree node leads to a contradiction; {@code false} otherwise
     */
    @Override
    public boolean isContradictoryBranch() {
        if (!this.isRoot()
                && parent.isJustified()
                && parent.isCorrect()
                && parent.getRule().getRuleType() == RuleType.CONTRADICTION) {
            return true;
        }
        boolean leadsToContra = true;
        for (TreeTransition child : children) {
            leadsToContra &= child.isContradictoryBranch();
        }
        return leadsToContra && !children.isEmpty();
    }

    /**
     * Recursively determines if the subtree rooted at this tree element is valid by checking
     * whether this tree element and all descendants of this tree element is justified
     * and justified correctly.
     *
     * @return {@code true} if this tree element and all descendants of this tree element is
     *     valid; {@code false} otherwise
     */
    @Override
    public boolean isValidBranch() {
        for (TreeTransition transition : children) {
            if (!transition.isValidBranch()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a list of the ancestors of this node.
     *
     * @return list of all the ancestors for this node
     */
    public List<TreeNode> getAncestors() {
        List<TreeNode> ancestors = new ArrayList<>();
        Queue<TreeNode> it = new LinkedList<>();
        it.add(this);

        while (!it.isEmpty()) {
            TreeNode next = it.poll();
            if (next.getParent() != null) {
                for (TreeNode treeNode : next.getParent().getParents()) {
                    if (!it.contains(treeNode)) {
                        it.add(treeNode);
                    }
                }
            }

            if (!ancestors.contains(next)) {
                ancestors.add(next);
            }
        }
        return ancestors;
    }

    /**
     * Gets a list of the descendants of this node.
     *
     * @return list of all the descendants for this node
     */
    public List<TreeElement> getDescendants() {
        List<TreeElement> descendants = new ArrayList<>();
        Queue<TreeElement> it = new LinkedList<>();
        it.add(this);

        while (!it.isEmpty()) {
            TreeElement next = it.poll();

            if (next.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode) next;
                for (TreeTransition transition : node.getChildren()) {
                    if (!descendants.contains(transition)) {
                        descendants.add(transition);
                        it.add(transition);
                    }
                }
            } else {
                TreeTransition trans = (TreeTransition) next;
                TreeNode childNode = trans.getChildNode();
                if (childNode != null && !descendants.contains(childNode)) {
                    descendants.add(childNode);
                    it.add(childNode);
                }
            }
        }
        return descendants;
    }

    /**
     * Gets a {@code DisjointSets} containing the children of this node such that the sets contained within
     * the {@code DisjointSets} are such that elements in the same set are branches of this tree node that
     * will eventually merge. This could mean that multiple merges take place before this happens.
     *
     * @return {@code DisjointSets} of tree transitions containing unique non-merging branches
     */
    public DisjointSets<TreeTransition> findMergingBranches() {
        DisjointSets<TreeElement> branches = new DisjointSets<>();
        children.forEach(branches::createSet);

        for (TreeTransition tran : children) {
            branches.createSet(tran);

            TreeNode child = tran.getChildNode();
            if (child != null) {
                List<TreeElement> nodes = new ArrayList<>();
                nodes.add(child);
                while (!nodes.isEmpty()) {
                    TreeElement element = nodes.get(0);
                    branches.createSet(element);
                    branches.union(tran, element);

                    if (element.getType() == TreeElementType.NODE) {
                        TreeNode node = (TreeNode) element;
                        nodes.addAll(node.getChildren());
                    } else {
                        TreeTransition childTran = (TreeTransition) element;
                        if (childTran.getChildNode() != null) {
                            nodes.add(childTran.getChildNode());
                        }
                    }
                    nodes.remove(element);
                }
            }
        }

        DisjointSets<TreeTransition> mergingBranches = new DisjointSets<>();
        children.forEach(mergingBranches::createSet);

        for (TreeTransition tran : children) {
            for (TreeTransition tran1 : children) {
                if (branches.find(tran) == branches.find(tran1)) {
                    mergingBranches.union(tran, tran1);
                }
            }
        }
        return mergingBranches;
    }

    /**
     * Finds the point at which the set of tree elements passed in will merge. This must be a set
     * gotten from {@link #findMergingBranches()} method {@code DisjointSets}.
     *
     * @param branches tree elements to find the merging point of
     * @return tree transition of the merging point or {@code null} if no such point exists
     */
    @SuppressWarnings("unchecked")
    public static TreeTransition findMergingPoint(@NotNull Set<? extends TreeElement> branches) {
        DisjointSets<TreeElement> mergeSet = new DisjointSets<>();
        Set<TreeElement> branchesCopy = new HashSet<>(branches);
        TreeElement headBranch = branchesCopy.iterator().next();
        branchesCopy.remove(headBranch);

        for (TreeElement element : branchesCopy) {
            mergeSet.createSet(element);
            if (element.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode) element;
                node.getDescendants()
                        .forEach(
                                (TreeElement e) -> {
                                    if (!mergeSet.contains(e)) {
                                        mergeSet.createSet(e);
                                    }
                                    mergeSet.union(element, e);
                                });
            } else {
                TreeTransition transition = (TreeTransition) element;
                TreeNode childNode = transition.getChildNode();
                if (childNode != null) {
                    List<TreeElement> des = childNode.getDescendants();
                    for (TreeElement e : des) {
                        if (!mergeSet.contains(e)) {
                            mergeSet.createSet(e);
                        }
                        mergeSet.union(element, e);
                    }
                }
            }
        }

        Queue<TreeElement> next = new LinkedList<>();
        next.add(headBranch);

        while (!next.isEmpty()) {
            TreeElement element = next.poll();
            if (!mergeSet.contains(element)) {
                mergeSet.createSet(element);
            }
            mergeSet.union(headBranch, element);

            if (mergeSet.setCount() == 1) {
                if (element.getType() == TreeElementType.TRANSITION) {
                    return (TreeTransition) element;
                }
                return null;
            }

            if (element.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode) element;
                next.addAll(node.getChildren());
            } else {
                TreeTransition tran = (TreeTransition) element;
                next.add(tran.getChildNode());
            }
        }
        return null;
    }

    /**
     * Determines if the specified tree transition is a parent of this node.
     *
     * @param parent tree transition that could be a parent
     * @return {@code true} if the specified tree transition is a parent of this node; {@code false} otherwise
     */
    public boolean isParent(@NotNull TreeTransition parent) {
        return this.parent == parent;
    }

    /**
     * Adds a child to this tree node.
     *
     * @param child child to add
     */
    public void addChild(@NotNull TreeTransition child) {
        children.add(child);
        if (parent != null && ((child.getRule() instanceof CaseRule) || !child.getBoard().isModifiableCaseRule())) {
            parent.propagateModifiableCaseRule(false);
        }
    }

    /**
     * Removes a child to this tree node.
     *
     * @param child child to remove
     */
    public void removeChild(@NotNull TreeTransition child) {
        children.remove(child);
        if (parent != null && ((child.getRule() instanceof CaseRule) || !child.getBoard().isModifiableCaseRule())) {
            parent.propagateModifiableCaseRule(true);
        }
    }

    /**
     * Determines if the specified tree node is a child of this node.
     *
     * @param child tree node that could be a child
     * @return {@code true} if the specified tree node is a child of this node; {@code false} otherwise
     */
    public boolean isChild(@NotNull TreeNode child) { return children.contains(child); }

    /**
     * Gets the {@code TreeNode}'s parent.
     *
     * @return the {@code TreeNode}'s parent
     */
    public TreeTransition getParent() { return parent; }

    /**
     * Sets the {@code TreeNode}'s parent.
     *
     * @param parent the {@code TreeNode}'s parent
     */
    public void setParent(TreeTransition parent) { this.parent = parent; }

    /**
     * Gets the {@code TreeNode}'s children.
     *
     * @return the {@code TreeNode}'s children
     */
    public List<TreeTransition> getChildren() { return children; }

    /**
     * Sets the {@code TreeNode}'s children.
     *
     * @param children the {@code TreeNode}'s children
     */
    public void setChildren(List<TreeTransition> children) {
        boolean hadCaseRule = false;
        boolean hasCaseRule = false;
        if (parent != null) {

            for (TreeTransition child : this.children) {
                if ((child.getRule() instanceof CaseRule) || !child.getBoard().isModifiableCaseRule()) {
                    hadCaseRule = true;
                    break;
                }
            }
            for (TreeTransition child : children) {
                if ((child.getRule() instanceof CaseRule) || !child.getBoard().isModifiableCaseRule()) {
                    hasCaseRule = true;
                    break;
                }
            }
        }

        this.children = children;
        if (hasCaseRule != hadCaseRule) { parent.propagateModifiableCaseRule(!hasCaseRule); }
    }

    /**
     * Determines if this node the root of the tree.
     *
     * @return {@code true} if this node is the root of the tree; {@code false} otherwise
     */
    public boolean isRoot() { return isRoot; }

    /**
     * Sets whether this node is the root of the tree.
     *
     * @param isRoot {@code true} if this node is the root of the tree; {@code false} otherwise
     */
    public void setRoot(boolean isRoot) { this.isRoot = isRoot; }

    /** Clears all children transitions from this tree node. */
    public void clearChildren() { this.children.clear(); }
}
