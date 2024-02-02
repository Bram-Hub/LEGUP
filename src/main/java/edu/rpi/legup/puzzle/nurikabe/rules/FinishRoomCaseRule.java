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

    public FinishRoomCaseRule() {
        super("NURI-CASE-0001",
                "Finish Room",
                "Room can be finished in one of two ways",
                "edu/rpi/legup/images/nurikabe/cases/BlackOrWhite.png"); //new image
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this rule. This method is
     * the one that should overridden in child classes.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition) {
        //make sure white region size of room that you're trying to finish is less than a set variable. for now 5
        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() > 5) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 5 or less children.";
        }

        Set<Point> locations = new HashSet<>();
        for (TreeTransition t1 : childTransitions) {
            locations.add(((NurikabeCell) t1.getBoard().getModifiedData().iterator().next()).getLocation()); //loop see if matches
            if (t1.getBoard().getModifiedData().size() != 1) {
                return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
            }
            if (((NurikabeCell) t1.getBoard().getModifiedData().iterator().next()).getType() != NurikabeType.WHITE) {
                return super.getInvalidUseOfRuleMessage() + ": This case rule must place an empty white cell for each case.";
            }
            for (Point loc : locations) {
                for (Point loc2 : locations) {
                    if ((loc != loc2) && (loc.x == loc2.x) && (loc.y == loc2.y)) {
                        return super.getInvalidUseOfRuleMessage() + ": This case rule must alter a different cell for each case.";
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


        for (PuzzleElement element : nurikabeBoard.getPuzzleElements()) { //loops all puzzle elements
            if (((NurikabeCell) element).getType() == NurikabeType.NUMBER) { //if the tile is a white number block
                Set<NurikabeCell> disRow = regions.getSet(((NurikabeCell) element)); //store the row of the white region
                boolean only = true; //placeholder boolean of if the element being tested is the only number block in the room
                for(NurikabeCell d : disRow) { //loops through tiles in the room
                    if((d.getType() == NurikabeType.NUMBER) && !(d.getData().equals(((NurikabeCell) element).getData()))) {
                    //if((d.getType() == NurikabeType.NUMBER) && (d.getData() != ((NurikabeCell) element).getData())) { //if found another number tile and it's data is different than the elemnt we're working with
                        only = false; //set only to false
                    }
                }
                if (disRow.size() + 1 == ((NurikabeCell) element).getData() && only) { //if size of region is 1 less than the number block and the number block is only number block in the region
                    caseBoard.addPickableElement(element); //add that room as a pickable element
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
        ArrayList<Board> cases = new ArrayList<>(); //makes array list of cases
        NurikabeBoard nuriBoard = (NurikabeBoard) board.copy(); //nurikabe board to edit
        NurikabeCell numbaCell = nuriBoard.getCell(((NurikabeCell) puzzleElement).getLocation().x,
                ((NurikabeCell) puzzleElement).getLocation().y); //number cell whose room we want to fill
        //System.out.println("x and y is " + numbaCell.getLocation().x + " " + numbaCell.getLocation().y);
        //NurikabeCell fakenow = (NurikabeCell)(nuriBoard.getPuzzleElement(puzzleElement));
        //System.out.println("x and y of nuri cell is " + fakenow.getLocation().x + " " + fakenow.getLocation().y);
        int filledRoomSize = numbaCell.getData(); //size of room we want afterward
        System.out.println("Filled room size is " + filledRoomSize);
        Set<Point> locations = new HashSet<>(); //locations where white space is added to finish room
        Point left = new Point(-1,0);
        Point right = new Point(1, 0);
        Point bot = new Point(0, -1);
        Point top = new Point(0, 1);
        Set<Point> directions = new HashSet<>();
        directions.add(left);
        directions.add(right);
        directions.add(top);
        directions.add(bot);
        boolean moreCases = true;
        while(moreCases) {
            moreCases = false;
            //NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy(); //new board each time
            //DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nurikabeBoard); //new board each time
            DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nuriBoard);
            //Set<NurikabeCell> disRow = regions.getSet((NurikabeCell) puzzleElement); //set of white spaces
            Set<NurikabeCell> disRow = regions.getSet(numbaCell); //set of white spaces
            for (NurikabeCell d : disRow) { //loops through white spaces
                System.out.println("Almost");
                for(Point direction : directions) {
                    System.out.println("made it this far");
                    //NurikabeCell curr = nurikabeBoard.getCell(d.getLocation().x + direction.x, d.getLocation().y + direction.y); //new board each time
                    //ensure next cell location will be within bounds
                    if(!((nuriBoard.getWidth() >= (d.getLocation().x + direction.x) && nuriBoard.getHeight() >= d.getLocation().y + direction.y))) {
                        continue; //if next location check would be outside of grid then continue
                    }
                    NurikabeCell curr = nuriBoard.getCell(d.getLocation().x + direction.x, d.getLocation().y + direction.y);
                    System.out.println("checking cell at " + curr.getLocation().x + " " + curr.getLocation().y);
                    if(curr != null) {
                        if (curr.getType() == NurikabeType.UNKNOWN) { //found adjacent space to region that is currently unknown
                            System.out.println("found an unknown");
                            curr.setData(NurikabeType.WHITE.toValue()); //
                            //ISSUE IS DISCREATEDROW SIZE IS 39 which is amount of gray tiles still left, even after change to white above
                            //add modified data?
                            //nuriBoard.addModifiedData(curr); // adds modified before check


                            //Newly added ^ need to set type to white so that created row is correct. delete after
                            Set<NurikabeCell> disCreatedRow = regions.getSet(curr); //should I set curr to white first? then gray after if it doesn't work
                            System.out.println("Literally can smell it discreatedrow size is " + disCreatedRow.size());
                            if (disCreatedRow.size() == filledRoomSize) {
                                System.out.println("Found a possible");
                                Point here = curr.getLocation();
                                boolean alreadyIn = false;
                                for (Point p : locations) {
                                    if (p == here) {
                                        alreadyIn = true;
                                        break;
                                    }
                                }
                                if (!alreadyIn) {
                                    System.out.println("Possible isn't in");
                                    Board casey = board.copy();
                                    PuzzleElement datacasey = curr;
                                    datacasey.setData(NurikabeType.WHITE.toValue());
                                    casey.addModifiedData(datacasey);
                                    cases.add(casey);
                                    locations.add(here);
                                    moreCases = true;
                                    System.out.println("Added possible");
                                }
                            }
                            curr.setData(NurikabeType.UNKNOWN.toValue()); //set type back to unknown
                            //nuriBoard.addModifiedData(curr); // confirms change back to unknown
                        }
                    }
                }
            }
        }
        return cases;
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
