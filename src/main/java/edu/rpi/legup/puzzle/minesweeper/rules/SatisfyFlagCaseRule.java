package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SatisfyFlagCaseRule extends CaseRule{
    public SatisfyFlagCaseRule() {
        super("MINE-CASE-0002",
                "Satisfy Flag",
                "Create a different path for each valid way to mark bombs and filled cells around a flag",
                "edu/rpi/legup/images/minesweeper/cases/Satisfy_Flag.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(minesweeperBoard, this);
        minesweeperBoard.setModifiable(false);
        for (PuzzleElement data : minesweeperBoard.getPuzzleElements()) {
            MinesweeperCell cell = (MinesweeperCell) data;
            if (cell.getTileNumber() >= 0 && cell.getTileNumber() <= 8 && MinesweeperUtilities.hasEmptyAdjacent(minesweeperBoard, cell)) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<Board>();

        // get value of cell
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board.copy();
        MinesweeperCell cell = (MinesweeperCell) minesweeperBoard.getPuzzleElement(puzzleElement);
        int cellMaxBlack = cell.getTileNumber();
        if (cellMaxBlack <= 0 || cellMaxBlack > 8) { // cell is not valid cell
            return null;
        }

        // find number of black & unset squares
        int cellNumBomb = 0;
        int cellNumUnset = 0;
        ArrayList<MinesweeperCell> unsetCells = new ArrayList<MinesweeperCell>();
        ArrayList<MinesweeperCell> adjCells = MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if (adjCell.getTileType() == MinesweeperTileType.BOMB) {
                cellNumBomb++;
            }
            if (adjCell.getTileType() == MinesweeperTileType.UNSET) {
                cellNumUnset++;
                unsetCells.add(adjCell);
            }
        }
        // no cases if no empty or if too many black already
        if (cellNumBomb >= cellMaxBlack || cellNumUnset == 0) {
            return cases;
        }

        // generate all cases as boolean expressions
        ArrayList<boolean[]> combinations;
        combinations = MinesweeperUtilities.getCombinations(cellMaxBlack - cellNumBomb, cellNumUnset);

        for (int i=0; i < combinations.size(); i++) {
            Board case_ = board.copy();
            for (int j=0; j < combinations.get(i).length; j++) {
                cell = (MinesweeperCell) case_.getPuzzleElement(unsetCells.get(j));
                if (combinations.get(i)[j]) {
                    cell.setCellType(MinesweeperTileData.bomb());
                }
                else {
                    cell.setCellType(MinesweeperTileData.empty());
                }
                case_.addModifiedData(cell);
            }
            cases.add(case_);
        }

        return cases;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        TreeNode parent = transition.getParents().get(0);
        List<TreeTransition> childTransitions = parent.getChildren();

        /*
         * In order for the transition to be valid, it can only be applied to
         * one cell, thus:
         *          * there must be modified cells
         *          * all modified cells must share at least one common adjacent
         *              cell
         *          * all modified cells must fit within a 3X3 square
         *          * the center of one of the possible squares must be a cell
         *              with a number
         *          * that cells possible combinations must match the transitions
         * If all the above is verified, then the transition is valid
         */


        /* ensure there are modified cells */
        Set<PuzzleElement> modCells = transition.getBoard().getModifiedData();
        if (modCells.size() <= 0) {
            return super.getInvalidUseOfRuleMessage();
        }


        /* ensure modified cells occur within a 3X3 square */
        int minVertLoc = Integer.MAX_VALUE, maxVertLoc = Integer.MIN_VALUE;
        int minHorzLoc = Integer.MAX_VALUE, maxHorzLoc = Integer.MIN_VALUE;
        for (PuzzleElement modCell : modCells) {
            Point loc = ((MinesweeperCell) modCell).getLocation();
            if (loc.x < minHorzLoc) {
                minHorzLoc = loc.x;
            }
            if (loc.x > maxHorzLoc) {
                maxHorzLoc = loc.x;
            }
            if (loc.y < minVertLoc) {
                minVertLoc = loc.y;
            }
            if (loc.y > maxVertLoc) {
                maxVertLoc = loc.y;
            }
        }
        if (maxVertLoc - minVertLoc > 3 || maxHorzLoc - minHorzLoc > 3) {
            return super.getInvalidUseOfRuleMessage();
        }


        /* get the center of all possible 3X3 squares,
         * and collect all that have numbers */
        MinesweeperBoard board = (MinesweeperBoard) transition.getParents().get(0).getBoard();
        ArrayList<MinesweeperCell> possibleCenters = new ArrayList<MinesweeperCell>();
        for (PuzzleElement modCell : modCells) {
            ArrayList<MinesweeperCell> adjacentCells = MinesweeperUtilities.getAdjacentCells(board, (MinesweeperCell) modCell);
            for (MinesweeperCell cell : adjacentCells) {
                possibleCenters.add(cell);
            }
        }

        // removing all elements without a valid number
        possibleCenters.removeIf(x -> x.getTileNumber() <= 0 || x.getTileNumber() >= 9);
        if (possibleCenters.isEmpty()) {
            return super.getInvalidUseOfRuleMessage();
        }


        /* Now go through the remaining centers, and check if their combinations
         * match the transitions */
        for (MinesweeperCell possibleCenter : possibleCenters) {
            int numBlack = 0;
            int numEmpty = 0;
            int maxBlack = possibleCenter.getTileNumber();
            for (MinesweeperCell adjCell : MinesweeperUtilities.getAdjacentCells(board, possibleCenter)) {
                if (adjCell.getTileType() == MinesweeperTileType.BOMB) {
                    numBlack++;
                }
                if (adjCell.getTileType() == MinesweeperTileType.UNSET) {
                    numEmpty++;
                }
            }
            if (numEmpty <= 0 || numBlack > maxBlack) {
                // this cell has no cases (no empty) or is already broken (too many black)
                continue;
            }

            ArrayList<boolean[]> combinations = MinesweeperUtilities.getCombinations(maxBlack - numBlack, numEmpty);
            if (combinations.size() != childTransitions.size()) {
                // not this center because combinations do not match transitions
                continue;
            }
            boolean quitEarly = false;
            for (TreeTransition trans : childTransitions) {
                /* convert the transition board into boolean format, so that it
                 * can be compared to the combinations */
                MinesweeperBoard transBoard = (MinesweeperBoard) trans.getBoard();
                ArrayList<MinesweeperCell> transModCells = new ArrayList<MinesweeperCell>();
                for (PuzzleElement modCell : modCells) {
                    transModCells.add((MinesweeperCell) transBoard.getPuzzleElement(modCell));
                }

                boolean[] translatedModCells = new boolean[transModCells.size()];
                for (int i=0; i < transModCells.size(); i++) {
                    if (transModCells.get(i).getTileType() == MinesweeperTileType.BOMB) {
                        translatedModCells[i] = true;
                    }
                    else {
                        translatedModCells[i] = false;
                    }
                }

                // try to find the above state in the combinations, remove if found
                boolean removed = false;
                for (boolean[] combination : combinations) {
                    if (Arrays.equals(combination, translatedModCells)) {
                        combinations.remove(combination);
                        removed = true;
                        break;
                    }
                }
                // if combination not found, no need to check further, just quit
                if (!removed) {
                    quitEarly = true;
                    break;
                }
            }

            /* we found a center that is valid */
            if (combinations.isEmpty() && !quitEarly) {
                return null;
            }
        }

        return super.getInvalidUseOfRuleMessage();
    }


    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
