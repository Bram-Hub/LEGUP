package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.TreeTentClue;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class FillinRowCaseRule extends CaseRule {

    public FillinRowCaseRule() {
        super("TREE-CASE-0001", "Fill In row",
                "A row must have the number of tents of its clue.",
                "edu/rpi/legup/images/treetent/case_rowcount.png");
    }

    /**
     * Gets the case board that indicates where this case rule can be applied on the given Board.
     *
     * @param board the given board
     * @return the case board object
     */
    @Override
    public CaseBoard getCaseBoard(Board board) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board.copy();
        treeTentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(treeTentBoard, this);
        ArrayList<TreeTentClue> clues = treeTentBoard.getRowClues();
        clues.addAll(treeTentBoard.getColClues());
        for (PuzzleElement element : clues) {
            // if ((((TreeTentCell) element).getType() == TreeTentType.CLUE_SOUTH && treeTentBoard.getRowCol(((TreeTentCell)element).getLocation().y, TreeTentType.UNKNOWN, true).size() != 0) ||
            //     (((TreeTentCell) element).getType() == TreeTentType.CLUE_EAST && treeTentBoard.getRowCol(((TreeTentCell)element).getLocation().x, TreeTentType.UNKNOWN, false).size() != 0)) {
            //     caseBoard.addPickableElement(element);
            // }
            caseBoard.addPickableElement(element);
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
        List<TreeTentCell> group;
        int tentsLeft;
        TreeTentClue clue = ((TreeTentClue) puzzleElement);
        int clueIndex = clue.getClueIndex() - 1;
        TreeTentBoard tBoard = (TreeTentBoard) board;
        if (clue.getType() == TreeTentType.CLUE_SOUTH) {
            group = tBoard.getRowCol(clueIndex, TreeTentType.UNKNOWN, false);
            tentsLeft = tBoard.getRowClues().get(clueIndex).getData() - tBoard.getRowCol(clueIndex, TreeTentType.TENT, false).size();
            cases = genCombinations(tBoard, group, tentsLeft, clueIndex, false);
        }
        else {
            group = tBoard.getRowCol(clueIndex, TreeTentType.UNKNOWN, true);
            tentsLeft = tBoard.getRowClues().get(clueIndex).getData() - tBoard.getRowCol(clueIndex, TreeTentType.TENT, true).size();
            cases = genCombinations(tBoard, group, tentsLeft, clueIndex, true);
        }

        //generate every combination (nCr)
        //call goodBoard for each generated combination
        //alternitive would be to implement collision avoidance while generating instead of after
        if (cases.size() > 0) {
            return cases;
        }
        return null;
    }

    private ArrayList<Board> genCombinations(TreeTentBoard iBoard, List<TreeTentCell> tiles, int target, Integer index, boolean isRow) {
        return genCombRecursive(iBoard, tiles, tiles, target, 0, new ArrayList<TreeTentCell>(), index, isRow);
    }

    private ArrayList<Board> genCombRecursive(TreeTentBoard iBoard, List<TreeTentCell> original, List<TreeTentCell> tiles, int target, int current, List<TreeTentCell> selected, Integer index, boolean isRow) {
        ArrayList<Board> b = new ArrayList<>();
        if (target == current) {
            TreeTentBoard temp = iBoard.copy();
            for (TreeTentCell c : original) {
                if (selected.contains(c)) {
                    PuzzleElement change = temp.getPuzzleElement(c);
                    change.setData(TreeTentType.TENT);
                    temp.addModifiedData(change);
                }
                else {
                    PuzzleElement change = temp.getPuzzleElement(c);
                    change.setData(TreeTentType.GRASS);
                    temp.addModifiedData(change);
                }

            }
            if (goodBoard(temp, index, isRow)) {
                b.add(temp);
            }
            return b;
        }
        for (int i = 0; i < tiles.size(); ++i) {
            List<TreeTentCell> sub = tiles.subList(i + 1, tiles.size());
            List<TreeTentCell> next = new ArrayList<TreeTentCell>(selected);
            next.add(tiles.get(i));
            b.addAll(genCombRecursive(iBoard, original, sub, target, current + 1, next, index, isRow));
        }
        return b;
    }

    //Effectively runs TouchingTents check on all the added tents to make sure that the proposed board is valid.
    //Could check more or less in the future depending on how "smart" this case rule should be.
    private boolean goodBoard(TreeTentBoard board, Integer index, boolean isRow) {
        List<TreeTentCell> tents;
        if (isRow) {
            tents = board.getRowCol(index, TreeTentType.TENT, true);
        }
        else {
            tents = board.getRowCol(index, TreeTentType.TENT, false);
        }

        for (TreeTentCell t : tents) {
            List<TreeTentCell> adj = board.getAdjacent(t, TreeTentType.TENT);
            List<TreeTentCell> diag = board.getDiagonals(t, TreeTentType.TENT);
            if (adj.size() > 0 || diag.size() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
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
        return null;
    }
}
