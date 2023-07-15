package edu.rpi.legup.puzzle.fillapix.rules;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;

public class CompleteClueCaseRule extends CaseRule {
    public CompleteClueCaseRule() {
        super("FPIX-CASE-0002",
        "Complete Clue",
        "Each clue must touch that number of squares.",
        "edu/rpi/legup/images/fillapix/cases/CompleteClue.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(fillapixBoard, this);
        fillapixBoard.setModifiable(false);
        for (PuzzleElement data : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) data;
            if (cell.getNumber() >= 0 && cell.getNumber() <= 9 && FillapixUtilities.hasEmptyAdjacent(fillapixBoard, cell)) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<Board>();

        // get value of cell
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        FillapixCell cell = (FillapixCell) fillapixBoard.getPuzzleElement(puzzleElement);
        int cellMaxBlack = cell.getNumber();
        if (cellMaxBlack < 0 || cellMaxBlack > 9) { // cell is not valid cell
            return null;
        }

        // find number of black & empty squares
        int cellNumBlack = 0;
        int cellNumEmpty = 0;
        ArrayList<FillapixCell> emptyCells = new ArrayList<FillapixCell>();
        ArrayList<FillapixCell> adjCells = FillapixUtilities.getAdjacentCells(fillapixBoard, cell);
        for (FillapixCell adjCell : adjCells) {
            if (adjCell.getType() == FillapixCellType.BLACK) {
                cellNumBlack++;
            }
            if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                cellNumEmpty++;
                emptyCells.add(adjCell);
            }
        }
        // no cases if no empty or if too many black already
        if (cellNumBlack > cellMaxBlack || cellNumEmpty == 0) {
            return cases;
        }
        
        // generate all cases as boolean expressions
        ArrayList<boolean[]> combinations;
        combinations = FillapixUtilities.getCombinations(cellMaxBlack - cellNumBlack, cellNumEmpty);

        for (int i=0; i < combinations.size(); i++) {
            Board case_ = board.copy();
            for (int j=0; j < combinations.get(i).length; j++) {
                cell = (FillapixCell) case_.getPuzzleElement(emptyCells.get(j));
                if (combinations.get(i)[j]) {
                    cell.setType(FillapixCellType.BLACK);
                } else {
                    cell.setType(FillapixCellType.WHITE);
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
         *          * the center of one of the possible squaress must be a cell 
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
            Point loc = ((FillapixCell) modCell).getLocation();
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
        FillapixBoard board = (FillapixBoard) transition.getParents().get(0).getBoard();
        Set<FillapixCell> possibleCenters = new TreeSet<FillapixCell>();
        possibleCenters.addAll(FillapixUtilities.getAdjacentCells(board, (FillapixCell) modCells.iterator().next()));
        for (PuzzleElement modCell : modCells) {
            possibleCenters.retainAll((FillapixUtilities.getAdjacentCells(board, (FillapixCell) modCell))); 
        }
        // removing all elements without a valid number
        possibleCenters.removeIf(x -> x.getNumber() < 0 || x.getNumber() >= 10);
        if (possibleCenters.isEmpty()) {
            return super.getInvalidUseOfRuleMessage();
        }


        /* Now go through the remaining centers, and check if their combinations
         * match the transitions */
        for (FillapixCell possibleCenter : possibleCenters) {
            int numBlack = 0;
            int numEmpty = 0;
            int maxBlack = possibleCenter.getNumber();
            for (FillapixCell adjCell : FillapixUtilities.getAdjacentCells(board, possibleCenter)) {
                if (adjCell.getType() == FillapixCellType.BLACK) {
                    numBlack++;
                }
                if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                    numEmpty++;
                }
            }
            if (numEmpty <= 0 || numBlack > maxBlack) {
                // this cell has no cases (no empty) or is already broken (too many black)
                continue;
            }

            ArrayList<boolean[]> combinations = FillapixUtilities.getCombinations(maxBlack - numBlack, numEmpty);
            if (combinations.size() != childTransitions.size()) {
                // not this center because combinations do not match transitions
                continue;
            }
            boolean quitEarly = false;
            for (TreeTransition trans : childTransitions) {
                /* convert the transition board into boolean format, so that it
                 * can be compared to the combinations */
                FillapixBoard transBoard = (FillapixBoard) trans.getBoard();
                ArrayList<FillapixCell> transModCells = new ArrayList<FillapixCell>();
                for (PuzzleElement modCell : modCells) {
                    transModCells.add((FillapixCell) transBoard.getPuzzleElement(modCell));
                }

                boolean[] translatedModCells = new boolean[transModCells.size()];
                for (int i=0; i < transModCells.size(); i++) {
                    if (transModCells.get(i).getType() == FillapixCellType.BLACK) {
                        translatedModCells[i] = true;
                    } else {
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