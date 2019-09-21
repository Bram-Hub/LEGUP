package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LinkTreeCaseRule extends CaseRule {

    public LinkTreeCaseRule() {
        super("Links from tree",
                "A tree must link to exactly one adjacent tent.",
                "edu/rpi/legup/images/treetent/caseLinkTree.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board.copy();
        treeTentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(treeTentBoard, this);
        for (PuzzleElement element : treeTentBoard.getPuzzleElements()) {
            if (((TreeTentCell) element).getType() == TreeTentType.TREE &&
                    !getCases(treeTentBoard, element).isEmpty()) {
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board         the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        List<TreeTentCell> adjCells = treeTentBoard.getAdjacent(cell, TreeTentType.TENT);
        for (TreeTentCell c : adjCells) {
            TreeTentBoard caseBoard = (TreeTentBoard) board.copy();
            TreeTentLine line = new TreeTentLine(cell, c);
            caseBoard.getLines().add(line);
            caseBoard.addModifiedData(line);
            cases.add(caseBoard);
        }
        return cases;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        Set<PuzzleElement> modCells = transition.getBoard().getModifiedData();
        if (modCells.size() != 1) {
            return "This case rule must have 1 modified cell for each case.";
        }
        PuzzleElement mod = modCells.iterator().next();
        TreeTentLine line = mod instanceof TreeTentLine ? (TreeTentLine) mod : null;
        if (line == null) {
            return "This case rule only involves tree and tent connection lines.";
        }
        TreeTentCell tree = null;
        if (line.getC1().getType() == TreeTentType.TREE) {
            tree = line.getC1();
        }
        if (line.getC2().getType() == TreeTentType.TREE) {
            tree = line.getC2();
        }
        if (tree == null) {
            return "This case rule must have a tent cell.";
        }

        TreeTentBoard parentBoard = (TreeTentBoard) transition.getParents().get(0).getBoard();
        ArrayList<Board> cases = getCases(parentBoard, tree);
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != cases.size()) {
            return "This case rule is incorrectly created.";
        }
        for (Board caseBoard : cases) {
            TreeTentBoard cBoard = (TreeTentBoard) caseBoard;
            TreeTentLine cLine = (TreeTentLine) cBoard.getModifiedData().iterator().next();
            boolean hasLine = false;
            for (TreeTransition tran : childTransitions) {
                TreeTentBoard tBoard = (TreeTentBoard) tran.getBoard();
                if (tBoard.getModifiedData().size() != 1) {
                    return "This case rule is incorrectly created.";
                }
                PuzzleElement tElement = tBoard.getModifiedData().iterator().next();
                if (tElement instanceof TreeTentLine) {
                    return "This case rule only involves tree and tent connection lines.";
                }
                if (cLine.compare((TreeTentLine) tElement)) {
                    hasLine = true;
                    break;
                }
            }
            if (!hasLine) {
                return "Could not find case";
            }
        }

        return null;
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
        return checkRuleRaw(transition);
    }
}
