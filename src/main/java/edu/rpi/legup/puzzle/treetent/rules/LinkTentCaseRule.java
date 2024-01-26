package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LinkTentCaseRule extends CaseRule {

    public LinkTentCaseRule() {
        super("TREE-CASE-0002", "Links from tent",
                "A tent must link to exactly one adjacent tree.",
                "edu/rpi/legup/images/treetent/caseLinkTent.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board.copy();
        treeTentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(treeTentBoard, this);
        for (PuzzleElement element : treeTentBoard.getPuzzleElements()) {
            if (((TreeTentCell) element).getType() == TreeTentType.TENT && !getCases(board, element).isEmpty()) {
                Boolean canAdd = true;
                List<TreeTentLine> lines = treeTentBoard.getLines();
                for (TreeTentLine l : lines) {
                    if (l.getC1().getLocation().equals(((TreeTentCell) element).getLocation()) || l.getC2().getLocation().equals(((TreeTentCell) element).getLocation())) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    caseBoard.addPickableElement(element);
                }
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
        ArrayList<Board> cases = new ArrayList<Board>();
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        List<TreeTentCell> adj = ((TreeTentBoard) board).getAdjacent(cell, TreeTentType.TREE);
        List<TreeTentLine> lines = ((TreeTentBoard) board).getLines();

        for (TreeTentCell tree : adj) {
            Boolean makeline = true;
            for (TreeTentLine l : ((TreeTentBoard) board).getLines()) {
                if (l.getC1().getLocation().equals(tree.getLocation()) || l.getC2().getLocation().equals(tree.getLocation())) {
                    makeline = false;
                }
            }
            if (makeline) {
                TreeTentBoard temp = ((TreeTentBoard) board).copy();
                TreeTentLine l = new TreeTentLine((TreeTentCell) temp.getPuzzleElement(cell), (TreeTentCell) temp.getPuzzleElement(tree));
                temp.getLines().add(l);
                temp.addModifiedData(l);
                cases.add(temp);
            }
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
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case";
        }
        PuzzleElement mod = modCells.iterator().next();
        TreeTentLine line = mod instanceof TreeTentLine ? (TreeTentLine) mod : null;
        if (line == null) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule only involves tree and tent connection lines";
        }
        TreeTentCell tent = null;
        if (line.getC1().getType() == TreeTentType.TENT) {
            tent = line.getC1();
        }
        if (line.getC2().getType() == TreeTentType.TENT) {
            tent = line.getC2();
        }
        if (tent == null) {
            return super.getInvalidUseOfRuleMessage() + "This case rule must have a tent cell";
        }

        TreeTentBoard parentBoard = (TreeTentBoard) transition.getParents().get(0).getBoard();
        ArrayList<Board> cases = getCases(parentBoard, tent);
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != cases.size()) {
            return super.getInvalidUseOfRuleMessage();
        }
        for (Board caseBoard : cases) {
            TreeTentBoard cBoard = (TreeTentBoard) caseBoard;
            TreeTentLine cLine = (TreeTentLine) cBoard.getModifiedData().iterator().next();
            boolean hasLine = false;
            for (TreeTransition tran : childTransitions) {
                TreeTentBoard tBoard = (TreeTentBoard) tran.getBoard();
                if (tBoard.getModifiedData().size() != 1) {
                    return super.getInvalidUseOfRuleMessage();
                }
                PuzzleElement tElement = tBoard.getModifiedData().iterator().next();
                if (!(tElement instanceof TreeTentLine)) {
                    return super.getInvalidUseOfRuleMessage() + ": This case rule only involves tree and tent connection lines";
                }
                if (cLine.compare((TreeTentLine) tElement)) {
                    hasLine = true;
                    break;
                }
            }
            if (!hasLine) {
                return super.getInvalidUseOfRuleMessage();
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
