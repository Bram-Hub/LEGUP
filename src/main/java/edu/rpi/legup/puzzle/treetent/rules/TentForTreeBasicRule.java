package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.util.ArrayList;

import java.util.List;

public class TentForTreeBasicRule extends BasicRule {

    public TentForTreeBasicRule() {
        super("TREE-BASC-0006", "Tent for Tree",
                "If only one unlinked tent and no blank cells are adjacent to an unlinked tree, the unlinked tree must link to the unlinked tent.",
                "edu/rpi/legup/images/treetent/NewTreeLink.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        if (!(puzzleElement instanceof TreeTentLine)) {
            return super.getInvalidUseOfRuleMessage() + ": Lines must be created for this rule.";
        }
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentLine line = (TreeTentLine) board.getPuzzleElement(puzzleElement);
        TreeTentCell tree, tent;
        if (line.getC1().getType() == TreeTentType.TREE && line.getC2().getType() == TreeTentType.TENT) {
            tree = line.getC1();
            tent = line.getC2();
        }
        else {
            if (line.getC2().getType() == TreeTentType.TREE && line.getC1().getType() == TreeTentType.TENT) {
                tree = line.getC2();
                tent = line.getC1();
            }
            else {
                return super.getInvalidUseOfRuleMessage() + ": This line must connect a tree to a tent.";
            }
        }
        int forced = isForced(board, tree, tent, line);
        if (forced == 1) {
            return null;
        }
        else {
            if (forced == -1) {
                return super.getInvalidUseOfRuleMessage() + ": This tree already has a link";
            }
            else {
                if (forced == -2) {
                    return super.getInvalidUseOfRuleMessage() + ": This tent already has a link";
                }
                else {
                    return super.getInvalidUseOfRuleMessage() + ": This tree and tent don't need to be linked.";
                }
            }
        }
    }

    private Integer isForced(TreeTentBoard board, TreeTentCell tree, TreeTentCell tent, TreeTentLine line) {
        List<TreeTentCell> adjTents = board.getAdjacent(tree, TreeTentType.TENT);
        adjTents.remove(tent);
        List<TreeTentLine> lines = board.getLines();
        lines.remove(line);
        for (TreeTentLine l : lines) {
            ArrayList<TreeTentCell> toRemove = new ArrayList<>();
            if (l.getC1().getLocation().equals(tree.getLocation()) || l.getC2().getLocation().equals(tree.getLocation())) {
                return -2;
            }
            for (TreeTentCell c : adjTents) {
                if (l.getC1().getLocation().equals(c.getLocation())) {
                    if (l.getC2().getLocation().equals(tree.getLocation())) {
                        return -1;
                    }
                    toRemove.add(c);

                }
                else {
                    if (l.getC2().getLocation().equals(c.getLocation())) {
                        if (l.getC1().getLocation().equals(tree.getLocation())) {
                            return -1;
                        }
                        toRemove.add(c);
                    }
                }
            }
            for (TreeTentCell c : toRemove) {
                adjTents.remove(c);
            }
            toRemove.clear();
        }
        if (adjTents.size() == 0) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
