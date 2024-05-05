package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentClue;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FillinRowCaseRule extends CaseRule {

    public FillinRowCaseRule() {
        super(
                "TREE-CASE-0001",
                "Fill In row",
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
            // if ((((TreeTentCell) element).getType() == TreeTentType.CLUE_SOUTH &&
            // treeTentBoard.getRowCol(((TreeTentCell)element).getLocation().y,
            // TreeTentType.UNKNOWN,
            // true).size() != 0) ||
            //     (((TreeTentCell) element).getType() == TreeTentType.CLUE_EAST &&
            // treeTentBoard.getRowCol(((TreeTentCell)element).getLocation().x,
            // TreeTentType.UNKNOWN,
            // false).size() != 0)) {
            //     caseBoard.addPickableElement(element);
            // }
            caseBoard.addPickableElement(element);
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases;
        List<TreeTentCell> group;
        int tentsLeft;
        TreeTentClue clue = ((TreeTentClue) puzzleElement);
        int clueIndex = clue.getClueIndex() - 1;
        TreeTentBoard tBoard = (TreeTentBoard) board;
        if (clue.getType() == TreeTentType.CLUE_SOUTH) {
            group = tBoard.getRowCol(clueIndex, TreeTentType.UNKNOWN, false);
            tentsLeft =
                    tBoard.getColClues().get(clueIndex).getData()
                            - tBoard.getRowCol(clueIndex, TreeTentType.TENT, false).size();
            cases = genCombinations(tBoard, group, tentsLeft, clueIndex, false);
        } else {
            group = tBoard.getRowCol(clueIndex, TreeTentType.UNKNOWN, true);
            tentsLeft =
                    tBoard.getRowClues().get(clueIndex).getData()
                            - tBoard.getRowCol(clueIndex, TreeTentType.TENT, true).size();
            cases = genCombinations(tBoard, group, tentsLeft, clueIndex, true);
        }

        // generate every combination (nCr)
        // call goodBoard for each generated combination
        // alternative would be to implement collision avoidance while generating instead of after
        if (cases.size() > 0) {
            return cases;
        }
        return null;
    }

    /**
     *
     * @param iBoard the board to place tents onto
     * @param tiles the locations where tents can be placed
     * @param target the target number of tents to place
     * @param index the index of tiles which is trying to be placed
     * @param isRow Used to check validity of board
     * @return the list of boards created
     */
    private ArrayList<Board> genCombinations(
            TreeTentBoard iBoard,
            List<TreeTentCell> tiles,
            int target,
            Integer index,
            boolean isRow) {
        ArrayList<Board> generatedBoards = new ArrayList<>();
        genCombRecursive(
                iBoard, tiles, target, 0, new ArrayList<TreeTentCell>(), 0, index, generatedBoards, isRow);
        return generatedBoards;
    }

    /**
     *
     * Recursive function to generate all ways of placing the target number of tents
     * from the list of tiles to fill.
     *
     * @param iBoard The board
     * @param tiles Unknown Tiles to fill
     * @param target number of tents to place
     * @param current number of tents already placed
     * @param currentTile index of the next tile to add
     * @param selected the cells which have tents
     * @param index The index of the clue
     * @param isRow Used for checking if the board is good
     *
     * The generated boards are placed into generatedBoards (passed by reference)
     */

    private void genCombRecursive(
            TreeTentBoard iBoard,
            List<TreeTentCell> tiles,
            int target,
            int current,
            List<TreeTentCell> selected,
            int currentTile,
            Integer index,
            ArrayList<Board> generatedBoards,
            boolean isRow) {
        // Base Case: Enough tents have been placed
        if (target == current) {
            TreeTentBoard boardCopy = iBoard.copy();
            // Selected Tiles should already be filled
            // Fill in other tiles with Grass
            for (TreeTentCell tile : tiles) {
                if (!selected.contains(tile)) {
                    PuzzleElement element = boardCopy.getPuzzleElement(tile);
                    element.setData(TreeTentType.GRASS);
                    boardCopy.addModifiedData(element);
                }
            }
            // board validity is checked after placing every tent
            // because the base case doesn't place any tents, the board
            // should still be valid
            generatedBoards.add(boardCopy);
            return;
        }

        // Recursive Case:
        // Looking at the group of possible tiles, save one of the tiles into selected,
        // Place it on the board,
        // Check if the board is good and recurse
        //
        // Backtracking:
        // Remove the placed tent from the board and selected
        for (int i = currentTile; i < tiles.size(); ++i){
            TreeTentCell tile = tiles.get(i);
            selected.add(tile);
            PuzzleElement element = iBoard.getPuzzleElement(tile);
            element.setData(TreeTentType.TENT);
            iBoard.addModifiedData(element);
            if (goodBoard(iBoard, index, isRow)) {
                genCombRecursive(iBoard, tiles, target, current + 1, selected, i + 1, index, generatedBoards, isRow);
            }
            element.setData(TreeTentType.UNKNOWN);
            iBoard.addModifiedData(element);
            selected.remove(tile);
        }
    }

    // Effectively runs TouchingTents check on all the added tents to make sure that the proposed
    // board is valid.
    // Could check more or less in the future depending on how "smart" this case rule should be.
    private boolean goodBoard(TreeTentBoard board, Integer index, boolean isRow) {
        List<TreeTentCell> tents;
        if (isRow) {
            tents = board.getRowCol(index, TreeTentType.TENT, true);
        } else {
            tents = board.getRowCol(index, TreeTentType.TENT, false);
        }

        for (TreeTentCell t : tents) {
            List<TreeTentCell> adj = board.getAdjacent(t, TreeTentType.TENT);
            List<TreeTentCell> diag = board.getDiagonals(t, TreeTentType.TENT);
            if (!adj.isEmpty() || !diag.isEmpty()) {
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
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    /**
     * Returns the elements necessary for the cases returned by getCases(board,puzzleElement) to be
     * valid Overridden by case rules dependent on more than just the modified data
     *
     * @param board board state at application
     * @param puzzleElement selected puzzleElement
     * @return List of puzzle elements (typically cells) this application of the case rule depends
     *     upon. Defaults to any element modified by any case
     */
    @Override
    public List<PuzzleElement> dependentElements(Board board, PuzzleElement puzzleElement) {
        List<PuzzleElement> elements = new ArrayList<>();

        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        TreeTentClue clue = (TreeTentClue) puzzleElement;

        // add all elements of filled row
        for (int i = 0; i < treeTentBoard.getWidth(); i++) {
            TreeTentCell cell = treeTentBoard.getCell(i, clue.getClueIndex() - 1);
            elements.add(board.getPuzzleElement((cell)));
        }

        return elements;
    }
}
