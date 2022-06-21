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
        int treesLeft;
        Point loc = ((TreeTentCell) puzzleElement).getLocation();
        TreeTentBoard tBoard = (TreeTentBoard)board;
        if(((TreeTentCell) puzzleElement).getType() == TreeTentType.CLUE_SOUTH) {
            group = tBoard.getRowCol(loc.y, TreeTentType.UNKNOWN, true);
            treesLeft = tBoard.getRowClues().get(loc.y).getData() - tBoard.getRowCol(loc.y, TreeTentType.TREE, true).size();
            cases = genCombinations(tBoard, group, treesLeft, loc, true);
        } else {
            group = tBoard.getRowCol(loc.x, TreeTentType.UNKNOWN, false);
            treesLeft = tBoard.getRowClues().get(loc.x).getData() - tBoard.getRowCol(loc.x, TreeTentType.TREE, false).size();
            cases = genCombinations(tBoard, group, treesLeft, loc, true);
        }

        //generate every combination (nCr)
        //call goodBoard for each generated combination
        //alternitive would be to implement collision avoidance while generating instead of after
        if(cases.size() > 0) {return cases;}
        return null;
    }

    private ArrayList<Board> genCombinations(TreeTentBoard iBoard, List<TreeTentCell> tiles, int target, Point loc, boolean isRow)
    {
        return genCombRecursive(iBoard, tiles, target, 0, new ArrayList<TreeTentCell>(), loc, isRow);
    }

    private ArrayList<Board> genCombRecursive(TreeTentBoard iBoard, List<TreeTentCell> tiles, int target, int current, List<TreeTentCell> selected, Point loc, boolean isRow)
    {
        ArrayList<Board> b = new ArrayList<>();
        if(target == current)
        {
            TreeTentBoard temp = iBoard.copy();
            for(TreeTentCell c : selected)
            {
                PuzzleElement change = temp.getPuzzleElement(c);
                change.setData(TreeTentType.TENT.value);
                temp.addModifiedData(change);
            }
            if(goodBoard(temp, loc, isRow)) {b.add(temp);}
            return b;
        }
        for(int i = 0; i < tiles.size(); ++i)
        {
            List<TreeTentCell> sub = tiles.subList(i, tiles.size());
            List<TreeTentCell> next = new ArrayList<TreeTentCell>(selected);
            next.add(tiles.get(i));
            b.addAll(genCombRecursive(iBoard, sub, target, current+1, next, loc, isRow));
        }
        return b;
    }

    //Effectively runs TouchingTents check on all the added trees to make sure that the proposed board is valid.
    //Could check more or less in the future depending on how "smart" this case rule should be.
    private boolean goodBoard(TreeTentBoard board, Point loc, boolean isRow)
    {
        List<TreeTentCell> trees;
        if(isRow)
        {
            trees = board.getRowCol(loc.x, TreeTentType.TREE, true);
        }
        else
        {
            trees = board.getRowCol(loc.y, TreeTentType.TREE, false);
        }

        for(TreeTentCell t : trees)
        {
            List<TreeTentCell> adj = board.getAdjacent(t, TreeTentType.TREE);
            List<TreeTentCell> diag = board.getDiagonals(t, TreeTentType.TREE);
            if(adj.size() > 0 || diag.size() > 0) {return false;}
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
