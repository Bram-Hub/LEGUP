package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;
import edu.rpi.legup.utility.DisjointSets;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FinishRoomCaseRule extends CaseRule {

    private Set<Integer> uniqueCases; // stores the unique case hashes

    public FinishRoomCaseRule() {
        super(
                "NURI-CASE-0002",
                "Finish Room",
                "Room can be finished in up to nine ways",
                "edu/rpi/legup/images/nurikabe/cases/FinishRoom.png");
        this.MAX_CASES = 9;
        this.MIN_CASES = 1;
        this.uniqueCases = new HashSet<>();
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule. This method is the one that should have overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        NurikabeBoard prevBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();
        /*
           Cannot have more than 9 cases
        */
        if (childTransitions.size() > MAX_CASES) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 9 or less children.";
        }
        /*
           Cannot have less than 1 case
        */
        if (childTransitions.size() < MIN_CASES) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 1 or more children.";
        }

        ArrayList<NurikabeCell> possibleOrigins = new ArrayList<>();
        Set<NurikabeCell> numberCells = NurikabeUtilities.getNurikabeNumberedCells(prevBoardState);
        for (PuzzleElement element : destBoardState.getModifiedData()) {
            NurikabeCell cell = (NurikabeCell) element;
            Point cellLoc = cell.getLocation();

            // Check all four adjacent directions
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int adjX = cellLoc.x + dir[0];
                int adjY = cellLoc.y + dir[1];

                // Check bounds
                if (adjX >= 0
                        && adjX < prevBoardState.getWidth()
                        && adjY >= 0
                        && adjY < prevBoardState.getHeight()) {
                    NurikabeCell adjacentCell = prevBoardState.getCell(adjX, adjY);

                    // If adjacent cell is a number cell and not already in possibleOrigins
                    if (numberCells.contains(adjacentCell)
                            && !possibleOrigins.contains(adjacentCell)) {
                        possibleOrigins.add(adjacentCell);
                    }
                }
            }
        }
        /*
           There must be at least one possible origin cell
        */
        if (possibleOrigins.size() == 0) {
            return super.getInvalidUseOfRuleMessage()
                    + ": There must be at least one possible origin cell.";
        }
        /*
           There must be only one possible origin cell
        */
        if (possibleOrigins.size() > 1) {
            return super.getInvalidUseOfRuleMessage()
                    + ": There must be only one possible origin cell.";
        }
        NurikabeCell selectedElement = possibleOrigins.get(0);
        // Check if the number of child transitions matches expected cases for the selected element
        ArrayList<Board> expectedCases = getCases(prevBoardState, selectedElement);
        if (childTransitions.size() != expectedCases.size()) {
            return super.getInvalidUseOfRuleMessage()
                    + ": Invalid number of child transitions. Expected "
                    + expectedCases.size()
                    + " but found "
                    + childTransitions.size()
                    + ".";
        }

        return null;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link Board}'s
     * current state to determine where this rule can be applied.
     *
     * @param board board to find locations where this case rule can be applied
     * @return a case board
     */
    @Override
    public CaseBoard getCaseBoard(Board board) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(nurikabeBoard, this);
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nurikabeBoard);
        nurikabeBoard.setModifiable(false);

        for (PuzzleElement element :
                nurikabeBoard.getPuzzleElements()) { // loops all puzzle elements
            if (((NurikabeCell) element).getType()
                    == NurikabeType.NUMBER) { // if the tile is a white number block
                Set<NurikabeCell> disRow =
                        regions.getSet(
                                ((NurikabeCell) element)); // store the row of the white region
                boolean only =
                        true; // placeholder boolean of if the element being tested is the only
                // number block in the room or not

                for (NurikabeCell d : disRow) { // loops through tiles in the room
                    // if found another number tile and it's data is different from the element
                    // we're working with
                    if ((d.getType() == NurikabeType.NUMBER)
                            && !(d.getData().equals(((NurikabeCell) element).getData()))) {
                        only = false;
                    }
                }
                // if size of region is 1 less than the number block and the number block is only
                // number block in the region
                if (disRow.size() < ((NurikabeCell) element).getData() && only) {
                    caseBoard.addPickableElement(element); // add that room as a pickable element
                }
            }
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
        ArrayList<Board> cases = new ArrayList<>(); // makes array list of cases
        if (puzzleElement == null) {
            return cases;
        }

        NurikabeBoard nuriBoard = (NurikabeBoard) board.copy(); // nurikabe board to edit
        NurikabeCell numberCell =
                nuriBoard.getCell(
                        ((NurikabeCell) puzzleElement).getLocation().x,
                        ((NurikabeCell) puzzleElement)
                                .getLocation()
                                .y); // number cell whose room we want to fill

        Point origPoint = new Point(numberCell.getLocation().x, numberCell.getLocation().y);
        int filledRoomSize = numberCell.getData(); // size of room we want afterward

        Point left = new Point(-1, 0);
        Point right = new Point(1, 0);
        Point bot = new Point(0, -1);
        Point top = new Point(0, 1);
        Set<Point> directions = new HashSet<>();
        directions.add(left);
        directions.add(right);
        directions.add(top);
        directions.add(bot);

        Set<Point> checkedPoints =
                new HashSet<>(); // add all into checked points and continue at start of loop if
        // inside
        DisjointSets<NurikabeCell> regions =
                NurikabeUtilities.getNurikabeRegions(nuriBoard); // gathers regions
        Set<NurikabeCell> numberCellRegion = regions.getSet(numberCell); // set of white spaces

        for (NurikabeCell d : numberCellRegion) {
            generateCases(
                    nuriBoard,
                    d,
                    filledRoomSize,
                    directions,
                    checkedPoints,
                    cases,
                    origPoint,
                    new ArrayList<>());
        }

        return cases;
    }

    /**
     * Recursively generates possible cases for filling a room with white cells based on the current
     * board state and specified parameters.
     *
     * @param nuriBoard the current Nurikabe board state
     * @param currentCell the current cell being evaluated
     * @param filledRoomSize the target size for the room being filled
     * @param directions the set of possible directions to expand the room
     * @param checkedPoints the set of points already evaluated to avoid redundancy
     * @param cases the list of valid board cases generated
     * @param origPoint the original point of the number cell initiating the room filling
     * @param modifiedPoints the list of points modified in the current recursive path
     */
    private void generateCases(
            NurikabeBoard nuriBoard,
            NurikabeCell currentCell,
            int filledRoomSize,
            Set<Point> directions,
            Set<Point> checkedPoints,
            ArrayList<Board> cases,
            Point origPoint,
            List<Point> modifiedPoints) {

        for (Point direction : directions) {
            Point newPoint =
                    new Point(
                            currentCell.getLocation().x + direction.x,
                            currentCell.getLocation().y + direction.y);

            if (newPoint.x < 0
                    || newPoint.y < 0
                    || newPoint.x >= nuriBoard.getWidth()
                    || newPoint.y >= nuriBoard.getHeight()) {
                continue;
            }

            if (checkedPoints.contains(newPoint)) {
                continue;
            }

            NurikabeCell newCell = nuriBoard.getCell(newPoint.x, newPoint.y);
            if (newCell.getType() == NurikabeType.UNKNOWN) {
                // Mark current cell as white
                newCell.setData(NurikabeType.WHITE.toValue());
                newCell.setModifiable(false);
                checkedPoints.add(newPoint);

                // Track the modification
                List<Point> newModList = new ArrayList<>(modifiedPoints);
                newModList.add(newPoint);

                // Recompute regions
                DisjointSets<NurikabeCell> regions =
                        NurikabeUtilities.getNurikabeRegions(nuriBoard);
                Set<NurikabeCell> newRoomSet = regions.getSet(newCell);

                if (!touchesDifferentRoom(
                        nuriBoard, newCell, filledRoomSize, directions, origPoint)) {
                    if (newRoomSet.size() == filledRoomSize) {
                        // Create a new board with all modified cells marked
                        NurikabeBoard caseBoard = (NurikabeBoard) nuriBoard.copy();

                        for (Point p : newModList) {
                            NurikabeCell c = caseBoard.getCell(p.x, p.y);
                            c.setData(NurikabeType.WHITE.toValue());
                            c.setModifiable(false);
                            caseBoard.addModifiedData(c);
                        }

                        boolean unique = true;
                        for (Board board : cases) {
                            if (caseBoard.equalsBoard(board)) {
                                unique = false;
                                break;
                            }
                        }
                        if (unique) {
                            cases.add(caseBoard);
                        }

                    } else if (newRoomSet.size() < filledRoomSize) {
                        for (NurikabeCell cellInRoom : newRoomSet) {
                            generateCases(
                                    nuriBoard,
                                    cellInRoom,
                                    filledRoomSize,
                                    directions,
                                    checkedPoints,
                                    cases,
                                    origPoint,
                                    newModList);
                        }
                    }
                }

                // Reset the board
                newCell.setData(NurikabeType.UNKNOWN.toValue());
                newCell.setModifiable(true);
                checkedPoints.remove(newPoint);
            }
        }
    }

    /**
     * Determines if a given cell touches a different room by checking adjacent cells in specified
     * directions.
     *
     * @param board the current Nurikabe board state
     * @param cell the cell being evaluated
     * @param origRoomSize the size of the original room being filled
     * @param directions the set of possible directions to check around the cell
     * @param origPoint the original point of the number cell initiating the room filling
     * @return true if the cell touches a different room, false otherwise
     */
    private boolean touchesDifferentRoom(
            NurikabeBoard board,
            NurikabeCell cell,
            int origRoomSize,
            Set<Point> directions,
            Point origPoint) {
        for (Point direction : directions) {
            Point adjacentPoint =
                    new Point(
                            cell.getLocation().x + direction.x, cell.getLocation().y + direction.y);

            if (adjacentPoint.x >= 0
                    && adjacentPoint.y >= 0
                    && adjacentPoint.x < board.getWidth()
                    && adjacentPoint.y < board.getHeight()) { // check if out of bounds
                NurikabeCell adjacentCell = board.getCell(adjacentPoint.x, adjacentPoint.y);
                // check if the adjacent cell is a number cell
                if (adjacentCell.getType() == NurikabeType.NUMBER) {
                    // check if it's different from the original number cell
                    if (origRoomSize != adjacentCell.getData()
                            || (adjacentPoint.x != origPoint.x || adjacentPoint.y != origPoint.y)) {
                        return true;
                    }
                }
            }
        }
        return false;
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
}
