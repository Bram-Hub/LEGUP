package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SatisfyNumberCaseRule extends CaseRule {
    private static final Logger LOGGER =
            LogManager.getLogger(SatisfyNumberCaseRule.class.getName());

    public SatisfyNumberCaseRule() {
        super(
                "LTUP-CASE-0002",
                "Satisfy Number",
                "The different ways a blocks number can be satisfied.",
                "edu/rpi/legup/images/lightup/cases/SatisfyNumber.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        LightUpBoard lightUpBoard = (LightUpBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(lightUpBoard, this);
        lightUpBoard.setModifiable(false);
        for (PuzzleElement data : lightUpBoard.getPuzzleElements()) {
            if (((LightUpCell) data).getType() == LightUpCellType.NUMBER) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement puzzleElement to determine the possible cases for
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        LightUpBoard lightUpBoard = (LightUpBoard) board;
        LightUpCell cell = (LightUpCell) puzzleElement;
        Point loc = cell.getLocation();

        List<LightUpCell> openSpots = new ArrayList<>();

        int numNeeded = cell.getData();

        LightUpCell checkCell = lightUpBoard.getCell(loc.x + 1, loc.y);
        if (checkCell != null) {
            if (checkCell.getType() == LightUpCellType.UNKNOWN && !checkCell.isLite()) {
                openSpots.add(checkCell);
            } else {
                if (checkCell.getType() == LightUpCellType.BULB) {
                    numNeeded--;
                }
            }
        }
        checkCell = lightUpBoard.getCell(loc.x, loc.y + 1);
        if (checkCell != null) {
            if (checkCell.getType() == LightUpCellType.UNKNOWN && !checkCell.isLite()) {
                openSpots.add(checkCell);
            } else {
                if (checkCell.getType() == LightUpCellType.BULB) {
                    numNeeded--;
                }
            }
        }
        checkCell = lightUpBoard.getCell(loc.x - 1, loc.y);
        if (checkCell != null) {
            if (checkCell.getType() == LightUpCellType.UNKNOWN && !checkCell.isLite()) {
                openSpots.add(checkCell);
            } else {
                if (checkCell.getType() == LightUpCellType.BULB) {
                    numNeeded--;
                }
            }
        }
        checkCell = lightUpBoard.getCell(loc.x, loc.y - 1);
        if (checkCell != null) {
            if (checkCell.getType() == LightUpCellType.UNKNOWN && !checkCell.isLite()) {
                openSpots.add(checkCell);
            } else {
                if (checkCell.getType() == LightUpCellType.BULB) {
                    numNeeded--;
                }
            }
        }

        if (numNeeded == 0) {
            return cases;
        }

        generateCases(lightUpBoard, numNeeded, openSpots, cases);

        return cases;
    }

    private void generateCases(
            final LightUpBoard board,
            final int num,
            List<LightUpCell> openSpots,
            List<Board> cases) {
        if (num > openSpots.size()) {
            return;
        }

        for (int i = 0; i < openSpots.size(); i++) {
            LightUpCell c = openSpots.get(i);
            LightUpBoard newCase = board.copy();
            LightUpCell newCell = c.copy();
            Point loc = c.getLocation();

            newCell.setData(-4);
            newCase.setCell(loc.x, loc.y, newCell);
            newCase.addModifiedData(newCell);

            generateCases(board, num, openSpots, cases, newCase, i);
        }
    }

    private void generateCases(
            final LightUpBoard board,
            final int num,
            List<LightUpCell> openSpots,
            List<Board> cases,
            LightUpBoard curBoard,
            int index) {
        if (num <= curBoard.getModifiedData().size()) {
            // Mark remaining open spots (that didn't get bulbs) as empty
            for (LightUpCell openSpot : openSpots) {
                Point loc = openSpot.getLocation();
                LightUpCell cell = curBoard.getCell(loc.x, loc.y);

                // If this cell wasn't already marked with a bulb, mark it as empty
                if (!curBoard.getModifiedData().contains(cell)) {
                    LightUpCell emptyCell = cell.copy();
                    emptyCell.setData(LightUpCellType.EMPTY.value);
                    curBoard.setCell(loc.x, loc.y, emptyCell);
                    curBoard.addModifiedData(emptyCell);
                }
            }

            cases.add(curBoard);
            return;
        }

        for (int i = index + 1; i < openSpots.size(); i++) {
            LightUpCell c = openSpots.get(i);
            Point loc = c.getLocation();
            LightUpCell cc = curBoard.getCell(loc.x, loc.y);
            if (!curBoard.getModifiedData().contains(cc)) {
                LightUpBoard newCase = board.copy();
                LightUpCell newCell = c.copy();

                for (PuzzleElement mod : curBoard.getModifiedData()) {
                    LightUpCell modCell = (LightUpCell) mod.copy();
                    Point modLoc = modCell.getLocation();

                    modCell.setData(LightUpCellType.BULB.value);

                    newCase.setCell(modLoc.x, modLoc.y, modCell);
                    newCase.addModifiedData(modCell);
                }

                newCell.setData(LightUpCellType.BULB.value);

                newCase.setCell(loc.x, loc.y, newCell);
                newCase.addModifiedData(newCell);

                generateCases(board, num, openSpots, cases, newCase, i);
            }
        }
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        TreeNode parent = transition.getParents().get(0);
        List<TreeTransition> childTransitions = parent.getChildren();

        List<LightUpCell> spots = getPossibleSpots(transition);
        if (spots == null) {
            return super.getInvalidUseOfRuleMessage();
        }

        for (LightUpCell c : spots) {
            ArrayList<Board> cases = getCases(parent.getBoard(), c);

            // Note: we will allow case rules to have only one option

            // Some error checking to make sure that weird stuff doesn't happen
            // if this case rule is incorrectly used to justify changes on the
            // puzzle board
            if (cases.size() == childTransitions.size() && cases.size() == 1) {
                TreeTransition childTransition = childTransitions.get(0);

                // If there is only 1 case, then this case rule should function no
                // differently than the Finish With Bulbs Direct Rule
                FinishWithBulbsDirectRule finishWithBulbs = new FinishWithBulbsDirectRule();
                childTransition.setRule(finishWithBulbs);
                boolean isCorrect = childTransition.isCorrect();

                // Changes the transition back to this case rule
                childTransition.setRule(this);

                if (isCorrect) {
                    return null;
                }
                return super.getInvalidUseOfRuleMessage();
            } else if (cases.size() == childTransitions.size() && cases.size() > 1) {
                boolean foundSpot = true;
                for (TreeTransition childTrans : childTransitions) {
                    LightUpBoard actCase = (LightUpBoard) childTrans.getBoard();
                    boolean foundBoard = false;
                    for (Board b : cases) {
                        LightUpBoard posCase = (LightUpBoard) b;
                        boolean foundAllCells = false;
                        if (posCase.getModifiedData().size() == actCase.getModifiedData().size()) {
                            foundAllCells = true;
                            for (PuzzleElement actEle : actCase.getModifiedData()) {
                                LightUpCell actCell = (LightUpCell) actEle;
                                boolean foundCell = false;
                                for (PuzzleElement posEle : posCase.getModifiedData()) {
                                    LightUpCell posCell = (LightUpCell) posEle;
                                    if (actCell.getType() == posCell.getType()
                                            && actCell.getLocation()
                                                    .equals(posCell.getLocation())) {
                                        foundCell = true;
                                        break;
                                    }
                                }
                                if (!foundCell) {
                                    foundAllCells = false;
                                    break;
                                }
                            }
                        }
                        if (foundAllCells) {
                            foundBoard = true;
                            break;
                        }
                    }
                    if (!foundBoard) {
                        foundSpot = false;
                        break;
                    }
                }
                if (foundSpot) {
                    return null;
                }
            }
        }
        return super.getInvalidUseOfRuleMessage();
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    /**
     * Gets all cells in the TreeTransition board that are adjacent to all modified cells
     *
     * @param transition TreeTransition object
     * @return list of cells that are adjacent to all modified cells, returns null if the number of
     *     modified cells is =0 || >4
     */
    private List<LightUpCell> getPossibleSpots(TreeTransition transition) {
        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Set<PuzzleElement> modCells = transition.getBoard().getModifiedData();

        int size = modCells.size();
        if (size == 0 || size > 4) {
            return null;
        } else {
            Iterator<PuzzleElement> it = modCells.iterator();
            List<LightUpCell> spots = getAdjacentCells(board, (LightUpCell) it.next());

            while (it.hasNext()) {
                spots.retainAll(getAdjacentCells(board, (LightUpCell) it.next()));
            }
            return spots;
        }
    }

    private List<LightUpCell> getAdjacentCells(LightUpBoard board, LightUpCell cell) {
        List<LightUpCell> cells = new ArrayList<>();
        Point point = cell.getLocation();
        LightUpCell right = board.getCell(point.x + 1, point.y);
        if (right != null) {
            cells.add(right);
        }
        LightUpCell down = board.getCell(point.x, point.y + 1);
        if (down != null) {
            cells.add(down);
        }
        LightUpCell left = board.getCell(point.x - 1, point.y);
        if (left != null) {
            cells.add(left);
        }
        LightUpCell up = board.getCell(point.x, point.y - 1);
        if (up != null) {
            cells.add(up);
        }
        return cells;
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

        LightUpBoard puzzleBoard = (LightUpBoard) board;
        LightUpCell point = (LightUpCell) puzzleBoard.getPuzzleElement(puzzleElement);

        List<LightUpCell> cells = getAdjacentCells(puzzleBoard, point);

        for (LightUpCell cell : cells) {
            // add cells that can light adjacents from any direction
            Point location = cell.getLocation();
            for (int i = location.x; i < puzzleBoard.getWidth(); i++) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(i);
                }
                LightUpCell c = puzzleBoard.getCell(i, location.y);
                if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                    break;
                } else if (!elements.contains(board.getPuzzleElement(c))) {
                    elements.add(board.getPuzzleElement(c));
                }
            }
            for (int i = location.x; i >= 0; i--) {
                LightUpCell c = puzzleBoard.getCell(i, location.y);
                if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                    break;
                } else if (!elements.contains(board.getPuzzleElement(c))) {
                    elements.add(board.getPuzzleElement(c));
                }
            }
            for (int i = location.y; i < puzzleBoard.getHeight(); i++) {
                LightUpCell c = puzzleBoard.getCell(location.x, i);
                if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                    break;
                } else if (!elements.contains(board.getPuzzleElement(c))) {
                    elements.add(board.getPuzzleElement(c));
                }
            }
            for (int i = location.y; i >= 0; i--) {
                LightUpCell c = puzzleBoard.getCell(location.x, i);
                if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                    break;
                } else if (!elements.contains(board.getPuzzleElement(c))) {
                    elements.add(board.getPuzzleElement(c));
                }
            }
        }

        return elements;
    }
}
