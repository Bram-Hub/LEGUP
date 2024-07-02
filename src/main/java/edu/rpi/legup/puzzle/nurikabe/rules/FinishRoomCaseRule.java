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
    private int legitCases =
            0; // placeholder for amount of cases originally generated in case user tries to delete

    // cases

    public FinishRoomCaseRule() {
        super(
                "NURI-CASE-0002",
                "Finish Room",
                "Room can be finished in up to five ways",
                "edu/rpi/legup/images/nurikabe/cases/FinishRoom.png");
        this.MAX_CASES = 5;
        this.MIN_CASES = 2;
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule. This method is the one that should overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() > 5) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 5 or less children.";
        }
        if (childTransitions.size() < 2) {
            return super.getInvalidUseOfRuleMessage()
                    + ": This case rule must have 2 or more children.";
        }
        if (childTransitions.size() != legitCases) {
            return super.getInvalidUseOfRuleMessage()
                    + ": Cases can not be removed from the branch.";
        } // stops user from deleting 1 or more generated cases and still having path show as green
        Set<Point> locations = new HashSet<>();
        for (TreeTransition t1 : childTransitions) {
            locations.add(
                    ((NurikabeCell) t1.getBoard().getModifiedData().iterator().next())
                            .getLocation()); // loop see if matches
            if (t1.getBoard().getModifiedData().size() != 1) {
                return super.getInvalidUseOfRuleMessage()
                        + ": This case rule must have 1 modified cell for each case.";
            }
            for (Point loc : locations) {
                for (Point loc2 : locations) {
                    if (!(loc.equals(loc2)) && (loc.x == loc2.x) && (loc.y == loc2.y)) {
                        return super.getInvalidUseOfRuleMessage()
                                + ": This case rule must alter a different cell for each case.";
                    }
                }
            }
        }

        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(nurikabeBoard, this);
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nurikabeBoard);
        nurikabeBoard.setModifiable(false);

        for (PuzzleElement element : nurikabeBoard.getPuzzleElements()) { // loops all puzzle elements
            if (((NurikabeCell) element).getType() == NurikabeType.NUMBER) { // if the tile is a white number block
                Set<NurikabeCell> disRow = regions.getSet(((NurikabeCell) element)); // store the row of the white region
                boolean only = true; // placeholder boolean of if the element being tested is the only number block in the room or not

                for (NurikabeCell d : disRow) { // loops through tiles in the room
                    // if found another number tile and it's data is different from the element we're working with
                    if ((d.getType() == NurikabeType.NUMBER)
                            && !(d.getData().equals(((NurikabeCell) element).getData()))) {
                        only = false;
                    }
                }
                // if size of region is 1 less than the number block and the number block is only number block in the region
                if (disRow.size() + 1 == ((NurikabeCell) element).getData() && only) {
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
        NurikabeCell numberCell = nuriBoard.getCell(((NurikabeCell) puzzleElement).getLocation().x,
                ((NurikabeCell) puzzleElement).getLocation().y); // number cell whose room we want to fill

        int filledRoomSize = numberCell.getData(); // size of room we want afterward
        Set<Point> locations = new HashSet<>(); // locations where white space is added to finish room

        Point left = new Point(-1, 0);
        Point right = new Point(1, 0);
        Point bot = new Point(0, -1);
        Point top = new Point(0, 1);
        Set<Point> directions = new HashSet<>();
        directions.add(left);
        directions.add(right);
        directions.add(top);
        directions.add(bot);

        Set<Point> checkedPoints = new HashSet<>(); // add all into checked points and continue at start of loop if inside
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nuriBoard); // gathers regions
        Set<NurikabeCell> disRow = regions.getSet(numberCell); // set of white spaces
        for (NurikabeCell d : disRow) { // loops through white spaces
            if (cases.size() >= 6) { // no need to check this many cases
                // throw new IllegalStateException("Too many cases");
                continue; // crash/runtime protection
            }
            for (Point direction : directions) {
                if (cases.size() >= 6) { // no need to check this many cases
                    // throw new IllegalStateException("Too many cases");
                    continue; // crash/runtime protection
                }
                if (!((nuriBoard.getWidth() > (d.getLocation().x + direction.x)
                        && (nuriBoard.getHeight() > d.getLocation().y + direction.y)
                        && (d.getLocation().x + direction.x >= 0)
                        && (d.getLocation().y + direction.y >= 0)))) {
                    continue; // if next location check would be outside of grid then continue
                }
                NurikabeCell curr = nuriBoard.getCell(d.getLocation().x + direction.x, d.getLocation().y + direction.y);
                if (checkedPoints.contains(curr.getLocation())) {
                    continue; // if we already checked whether making this tile white would complete the room then continue
                }
                checkedPoints.add(curr.getLocation()); // adds location to checkedPoints so we don't check it again and accidentally add

                if (curr.getType() == NurikabeType.UNKNOWN) { // found adjacent space to region that is currently unknown
                    curr.setData(NurikabeType.WHITE.toValue()); // changes adjacent cell color to white
                    //nuriBoard.addModifiedData(curr); // adds modified before check
                    regions = NurikabeUtilities.getNurikabeRegions(nuriBoard); // update regions
                    Set<NurikabeCell> disCreatedRow = regions.getSet(curr); // gets set of created row with new white cell added

                    if (disCreatedRow.size() == filledRoomSize) { // If adding white fills the room to exact size of
                                                                    // number block and doesn't connect with another room
                        Point here = curr.getLocation(); // gets current location of new white tile that fills room
                        boolean alreadyIn = false; // sets whether the tile has already been added to false
                        for (Point p : locations) { // loops through locations of previously added tiles
                            if (p == here) { // if point is already in
                                alreadyIn = true; // change already in to true
                                break;
                            }
                        }

                        if (!alreadyIn) { // if point wasn't already in
                            Board casey = nuriBoard.copy(); // copy the current board with white tile changed
                            PuzzleElement datacasey = curr; // gets changed white tile as a puzzle element
                            datacasey.setData(NurikabeType.WHITE.toValue()); // ensure set to white, probably redundant
                            casey.addModifiedData(datacasey); // ensure confirmed white change
                            regions = NurikabeUtilities.getNurikabeRegions(nuriBoard); // update regions
                            cases.add(casey); // add this case to list of cases
                            locations.add(here); // add location of new white tile to list of locations so
                                                // that we don't accidentally add it again later
                        }
                    }
                    curr.setData(NurikabeType.UNKNOWN.toValue()); // set cell type back to unknown
                    regions = NurikabeUtilities.getNurikabeRegions(nuriBoard); // updates regions
                }
            }
            legitCases = cases.size();
        }
        return cases;
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
