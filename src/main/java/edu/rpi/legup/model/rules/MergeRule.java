package edu.rpi.legup.model.rules;

import static edu.rpi.legup.model.rules.RuleType.MERGE;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import java.util.ArrayList;
import java.util.List;

/**
 * MergeRule is an implementation of a rule that merges multiple nodes into one. It validates if the
 * merging of nodes is done correctly.
 */
public class MergeRule extends Rule {
    /** MergeRule Constructor merges to board states together */
    public MergeRule() {
        super(
                "MERGE",
                "Merge Rule",
                "Merge any number of nodes into one",
                "edu/rpi/legup/images/Legup/MergeRule.png");
        this.ruleType = MERGE;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule. This
     * method is the one that should have overridden in child classes
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @SuppressWarnings("unchecked")
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        Board board = transition.getBoard();
        List<TreeNode> mergingNodes = new ArrayList<>();
        List<Board> mergingBoards = new ArrayList<>();
        for (TreeNode treeNode : transition.getParents()) {
            mergingNodes.add(treeNode);
            mergingBoards.add(treeNode.getBoard());
        }

        TreeNode lca = Tree.getLowestCommonAncestor(mergingNodes);
        if (lca == null) {
            return "Merge was not correctly created.";
        }
        Board lcaBoard = lca.getBoard();

        Board mergedBoard = lcaBoard.mergedBoard(lcaBoard, mergingBoards);

        for (PuzzleElement m : mergedBoard.getPuzzleElements()) {
            if (!m.equalsData(board.getPuzzleElement(m))) {
                return "Merge was not correctly created.";
            }
        }

        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule This method is the one that should be overridden in child
     * classes
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRule(transition);
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition) {
        return checkRuleRaw(transition);
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRawAt(transition, puzzleElement);
    }
}
