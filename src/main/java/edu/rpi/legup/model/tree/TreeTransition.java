package edu.rpi.legup.model.tree;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.rules.RuleType;

import java.util.ArrayList;
import java.util.List;

public class TreeTransition extends TreeElement {
    private ArrayList<TreeNode> parents;
    private TreeNode childNode;
    private Rule rule;
    private boolean isCorrect;
    private boolean isVerified;

    /**
     * TreeTransition Constructor create a transition from one node to another
     *
     * @param board board state of the transition
     */
    public TreeTransition(Board board) {
        super(TreeElementType.TRANSITION);
        this.parents = new ArrayList<>();
        this.childNode = null;
        this.board = board;
        this.rule = null;
        this.isCorrect = false;
        this.isVerified = false;
    }

    /**
     * TreeTransition Constructor - create a transition from one node to another
     *
     * @param parent parent tree node associated with the transition
     * @param board  board state of the transition
     */
    public TreeTransition(TreeNode parent, Board board) {
        this(board);
        this.parents.add(parent);
    }

    /**
     * Recursively propagates the change of puzzleElement down the tree
     *
     * @param element puzzleElement of the change made
     */
    @SuppressWarnings("unchecked")
    public void propagateChange(PuzzleElement element) {
        if(isJustified() && rule.getRuleType() == RuleType.MERGE) {
            TreeNode lca = Tree.getLowestCommonAncestor(parents);
            Board lcaBoard = lca.getBoard();
            List<Board> boards = new ArrayList<>();
            parents.forEach(p -> boards.add(p.getBoard()));
            PuzzleElement lcaElement = lcaBoard.getPuzzleElement(element);
            boolean isSame = true;
            for (Board board : boards) {
                isSame &= element.equalsData(board.getPuzzleElement(lcaElement));
            }

            if (isSame) {
                boolean changed = false;
                PuzzleElement mergedData = board.getPuzzleElement(element);
                if(lcaElement.equalsData(element) && !mergedData.equalsData(element)) {
                    mergedData.setData(element.getData());
                    board.removeModifiedData(element);
                    board.notifyChange(element);
                    changed = true;
                } else if (!lcaElement.equalsData(element)){
                    mergedData.setData(element.getData());
                    board.addModifiedData(mergedData);
                    board.notifyChange(element);
                    changed = true;
                }
                if (changed && childNode != null) {
                    childNode.getBoard().notifyChange(element.copy());
                    for (TreeTransition child : childNode.getChildren()) {
                        PuzzleElement copy = element.copy();
                        copy.setModifiable(false);
                        child.propagateChange(copy);
                    }
                }
            }
        } else if (childNode != null) {
            board.notifyChange(element);
            childNode.getBoard().notifyChange(element.copy());
            for (TreeTransition child : childNode.getChildren()) {
                PuzzleElement copy = element.copy();
                copy.setModifiable(false);
                child.propagateChange(copy);
            }
        }
        reverify();
    }

    /**
     * Recursively propagates the addition of puzzleElement down the tree
     *
     * @param element puzzleElement of the addition made
     */
    @SuppressWarnings("unchecked")
    public void propagateAddition(PuzzleElement element) {
        if(isJustified() && rule.getRuleType() == RuleType.MERGE) {
            TreeNode lca = Tree.getLowestCommonAncestor(parents);
            Board lcaBoard = lca.getBoard();
            List<Board> boards = new ArrayList<>();
            parents.forEach(p -> boards.add(p.getBoard()));
            PuzzleElement lcaElement = lcaBoard.getPuzzleElement(element);
            boolean isSame = true;
            for (Board board : boards) {
                isSame &= element.equalsData(board.getPuzzleElement(lcaElement));
            }

            if (isSame) {
                boolean changed = false;
                PuzzleElement mergedData = board.getPuzzleElement(element);
                if(lcaElement.equalsData(element) && !mergedData.equalsData(element)) {
                    mergedData.setData(element.getData());
                    board.removeModifiedData(element);
                    board.notifyDeletion(element);
                    changed = true;
                } else if (!lcaElement.equalsData(element)){
                    mergedData.setData(element.getData());
                    board.addModifiedData(mergedData);
                    board.notifyAddition(element);
                    changed = true;
                }
                if (changed && childNode != null) {
                    childNode.getBoard().notifyAddition(element.copy());
                    for (TreeTransition child : childNode.getChildren()) {
                        child.propagateAddition(element.copy());
                    }
                }
            }
        } else if (childNode != null) {
            board.notifyAddition(element);
            childNode.getBoard().notifyAddition(element.copy());
            for (TreeTransition child : childNode.getChildren()) {
                child.propagateAddition(element.copy());
            }
        }
        reverify();
    }

    /**
     * Recursively propagates the change of puzzleElement down the tree
     *
     * @param element puzzleElement of the change made
     */
    @SuppressWarnings("unchecked")
    public void propagateDeletion(PuzzleElement element) {
        if(isJustified() && rule.getRuleType() == RuleType.MERGE) {
            TreeNode lca = Tree.getLowestCommonAncestor(parents);
            Board lcaBoard = lca.getBoard();
            List<Board> boards = new ArrayList<>();
            parents.forEach(p -> boards.add(p.getBoard()));
            PuzzleElement lcaElement = lcaBoard.getPuzzleElement(element);
            boolean isSame = true;
            for (Board board : boards) {
                isSame &= element.equalsData(board.getPuzzleElement(lcaElement));
            }

            if (isSame) {
                boolean changed = false;
                PuzzleElement mergedData = board.getPuzzleElement(element);
                if(lcaElement.equalsData(element) && !mergedData.equalsData(element)) {
                    mergedData.setData(element.getData());
                    board.removeModifiedData(element);
                    board.notifyDeletion(element);
                    changed = true;
                } else if (!lcaElement.equalsData(element)){
                    mergedData.setData(element.getData());
                    board.addModifiedData(mergedData);
                    board.notifyAddition(element);
                    changed = true;
                }
                if (changed && childNode != null) {
                    childNode.getBoard().notifyDeletion(element.copy());
                    for (TreeTransition child : childNode.getChildren()) {
                        child.propagateDeletion(element.copy());
                    }
                }
            }
        } else if (childNode != null) {
            board.notifyDeletion(element);
            childNode.getBoard().notifyDeletion(element.copy());
            for (TreeTransition child : childNode.getChildren()) {
                child.propagateDeletion(element.copy());
            }
        }
        reverify();
    }

    /**
     * Determines if this tree node leads to a contradiction. Every path from this tree node
     * must lead to a contradiction including all of its children
     *
     * @return true if this tree node leads to a contradiction, false otherwise
     */
    @Override
    public boolean isContradictoryBranch() {
        if (isJustified() && isCorrect() && rule.getRuleType() == RuleType.CONTRADICTION) {
            return true;
        } else if (childNode == null) {
            return false;
        } else {
            return childNode.isContradictoryBranch() && isJustified() && isCorrect();
        }
    }

    /**
     * Recursively determines if the sub tree rooted at this tree puzzleElement is valid by checking
     * whether this tree puzzleElement and all descendants of this tree puzzleElement is justified
     * and justified correctly
     *
     * @return true if this tree puzzleElement and all descendants of this tree puzzleElement is valid,
     * false otherwise
     */
    @Override
    public boolean isValidBranch() {
        return isJustified() && isCorrect() && childNode != null && childNode.isValidBranch();
    }

    /**
     * Gets the parent tree nodes of this transition
     *
     * @return parent tree nodes of this tree transition
     */
    public ArrayList<TreeNode> getParents() {
        return parents;
    }

    /**
     * Sets the parent tree nodes of this transition
     *
     * @param parents parents tree nodes of this tree transition
     */
    public void setParents(ArrayList<TreeNode> parents) {
        this.parents = parents;
    }

    /**
     * Adds a parent tree node to this tree transition
     *
     * @param parent parent tree node to add
     */
    public void addParent(TreeNode parent) {
        parents.add(parent);
    }

    /**
     * Removes a parent tree node to this tree transition
     *
     * @param parent parent tree node to remove
     */
    public void removeParent(TreeNode parent) {
        parents.remove(parent);
    }

    /**
     * Determines if the specified tree node is a parent of this transition
     *
     * @param parent tree node that could be a parent
     * @return true if the specified tree node is a parent of this transition, false otherwise
     */
    public boolean isParent(TreeNode parent) {
        return parents.contains(parent);
    }

    /**
     * Gets the childNode tree node of this transition
     *
     * @return childNode tree node
     */
    public TreeNode getChildNode() {
        return childNode;
    }

    /**
     * Sets the childNode tree node of this transition
     *
     * @param childNode childNode tree node
     */
    public void setChildNode(TreeNode childNode) {
        this.childNode = childNode;
    }

    /**
     * Gets the rule associated with this transition
     *
     * @return rule of this transition
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Sets the rule associated with this transition
     *
     * @param rule rule of this transition
     */
    public void setRule(Rule rule) {
        this.rule = rule;
        isVerified = false;
    }

    /**
     * Gets whether this transition is correctly justified
     *
     * @return true if this transition is correctly justified, false otherwise
     */
    public boolean isCorrect() {
        if (isJustified() && !isVerified) {
            isCorrect = rule.checkRule(this) == null;
            isVerified = true;
        }
        return isJustified() && isCorrect;
    }

    /**
     * Sets whether this transition is correctly justified
     *
     * @param isCorrect true if this transition is correctly justified, false otherwise
     */
    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
        this.isVerified = true;
    }

    /**
     * Forces check of rule on this transition regardless if it has been cached already
     *
     * @return true if this transition is correctly justified, false otherwise
     */
    public boolean reverify() {
        isVerified = false;
        return isCorrect();
    }

    /**
     * Gets whether this transition is justified
     *
     * @return true if this transition is justified, false otherwise
     */
    public boolean isJustified() {
        return rule != null;
    }
}
