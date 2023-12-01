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
//            for (TreeTransition t2 : childTransitions) {
//                if (t1 != t2 && t1.getLocation().equals(t2.getLocation())) {
//
//                }
//            }
            for (Point loc : locations) {
                for (Point loc2 : locations) {
                    if ((loc != loc2) && (loc.x == loc2.x) && (loc.y == loc2.y)) {
                        return super.getInvalidUseOfRuleMessage() + ": This case rule must alter a different cell for each case.";
                    }
                }
            }
        }

        return null;
//
////        TreeTransition case1 = childTransitions.get(0);
////        TreeTransition case2 = childTransitions.get(1);
////        if (case1.getBoard().getModifiedData().size() != 1 ||
////                case2.getBoard().getModifiedData().size() != 1) {
////            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
////        }
//
////        NurikabeCell mod1 = (NurikabeCell) case1.getBoard().getModifiedData().iterator().next();
////        NurikabeCell mod2 = (NurikabeCell) case2.getBoard().getModifiedData().iterator().next();
////        if (!(mod1.getType() == NurikabeType.WHITE && mod2.getType() == NurikabeType.WHITE)) {
////            return super.getInvalidUseOfRuleMessage() + ": This case rule must place 2 empty white cells.";
////        }
//        if (mod1.getLocation().equals(mod2.getLocation())) {
//            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify a different cell for each case.";
//        }
//
//        Point loc1 = mod1.getLocation(); //position of placed cell
//        Point loc2 = mod2.getLocation(); //position of other placed cell
//        Set<NurikabeCell> adj = new HashSet<>(); //set to hold adjacent cells
//        Set<NurikabeCell> adj2 = new HashSet<>(); //set to hold adjacent cells
//        Point left = new Point(-1,0);
//        Point right = new Point(1, 0);
//        Point bot = new Point(0, -1);
//        Point top = new Point(0, 1);
//        Set<Point> directions = new HashSet<>();
//        directions.add(left);
//        directions.add(right);
//        directions.add(top);
//        directions.add(bot);
//        for(Point direction : directions) {
//            NurikabeCell curr = destBoardState.getCell(loc1.x + direction.x, loc1.y + direction.y);
//            if(curr != null) {
//                if(curr.getType() == NurikabeType.WHITE || curr.getType() == NurikabeType.NUMBER) {
//                    adj.add(curr); //adds cells to adj only if they are white or number blocks
//                }
//            }
//            NurikabeCell curr2 = destBoardState.getCell(loc2.x + direction.x, loc2.y + direction.y);
//            if(curr2 != null) {
//                if(curr2.getType() == NurikabeType.WHITE || curr2.getType() == NurikabeType.NUMBER) {
//                    adj2.add(curr2);
//                }
//            }
//        }
//        boolean match = false;
//        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(destBoardState);
//        Set<NurikabeCell> realadj = new HashSet<>(); //set to hold adjacent cells
//        Set<NurikabeCell> realadj2 = new HashSet<>(); //set to hold adjacent cells
//        boolean breakit = false;
//        for (NurikabeCell c : adj) { //loops through adjacent cells
//            breakit = false;
////            Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
////            for (NurikabeCell d : disRow) { //loops through white spaces
//                for (NurikabeCell c2 : adj2) {
//                    Set<NurikabeCell> disRow2 = regions.getSet(c2);
//                    Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
//                    breakit = false;
//                    for (NurikabeCell d : disRow) { //loops through white spaces
//                    for (NurikabeCell d2 : disRow2) {
//                        if(d.getLocation() == d2.getLocation()) {
//                            match = true;
//                            realadj.add(c);
//                            realadj2.add(d);
//                            breakit = true;
//                            break;
//                        }
//                    }
//                    if(breakit) {
//                        break;
//                    }
//                }
//            }
//        }
//        //the above should find and add to realadj and realadj2 all of the adjacent cells to the original
//        //that match with the same white space region that has the room you're trying to finish
//
//        if(!match) {
//            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify cells adjacent to the same white region.";
//        }
//
//        match = false;
//
//        //issue is ensuring that the 2 modified locations are related to the same white region. can't just make sure
//        //white number size is same because there can be multiple that are the same
//        //maybe point of white number being the same??
//        int intArray[] = new int[realadj.size()];
//        for(int k = 0; k < realadj.size(); k++) {
//            intArray[k] = -1;
//        }
//        int count = 0;
//        for (NurikabeCell c : realadj) { //loops through adjacent cells //changed to realadj
//            Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
//            //if the location of any of the blocks is the same. User should realize that solution can't be made
//            //if they correctly fill one room but it adds too many squares to another already completed room
//            for (NurikabeCell d : disRow) { //loops through white spaces
//                if (d.getType() == NurikabeType.NUMBER) { //if the white space is a number
//                    if(regions.getSet(d).size()-1 == d.getData()) { //if that cells white area is 1 less than
//                        //return null; //the size of the number of one of the number cells within that set
//                        match = true;
//                        intArray[count] = count;
//                    }
//                }
//            }
//            count++;
//        }
//        if(!match) {
//            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify cells adjacent to the same white region that " +
//                    "needs one more white cell to complete the room.";
//        }
//
//        count = 0;
//        for (NurikabeCell c : realadj2) { //loops through adjacent cells //changed to realadj2
//            Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
//            //if the location of any of the blocks is the same. User should realize that solution can't be made
//            //if they correctly fill one room but it adds too many squares to another already completed room
//            for (NurikabeCell d : disRow) { //loops through white spaces
//                if (d.getType() == NurikabeType.NUMBER) { //if the white space is a number
//                    if(regions.getSet(d).size()-1 == d.getData() && intArray[count] == count) { //if that cells white area is 1 less than
//                        //return null; //the size of the number of one of the number cells within that set
//                        return null;
//                    }
//                }
//            }
//            count++;
//        }
//        //if the cell is white and size of its column is 1 less than the room size
//        //may not work for example with room of size 4 that is finished. But also a near adjacent room of size
//        //3 that is also finished. Would validate the 4 - 1 = 3 even tho the 3 room is finished.
//        return super.getInvalidUseOfRuleMessage() + ": This case rule must modify cells adjacent to the same white region that " +
//                "needs one more white cell to complete the room.";
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(nurikabeBoard, this);
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nurikabeBoard);
        nurikabeBoard.setModifiable(false);
//        Point left = new Point(-1,0);
//        Point right = new Point(1, 0);
//        Point bot = new Point(0, -1);
//        Point top = new Point(0, 1);
//        Set<Point> directions = new HashSet<>();
//        directions.add(left);
//        directions.add(right);
//        directions.add(top);
//        directions.add(bot);
//        for (PuzzleElement element : nurikabeBoard.getPuzzleElements()) {
//            for(Point direction : directions) {
//                NurikabeCell curr = nurikabeBoard.getCell(((NurikabeCell) element).getLocation().x + direction.x,
//                        ((NurikabeCell) element).getLocation().y + direction.y);
//                if (curr.getType() == NurikabeType.WHITE || curr.getType() == NurikabeType.UNKNOWN) {
//                    ArrayList<NurikabeCell> numberedCells = new ArrayList<>();
//                    Set<NurikabeCell> disRow = regions.getSet(curr); //set of white spaces
//                    for (NurikabeCell d : disRow) { //loops through white spaces
//                        if (d.getType() == NurikabeType.NUMBER) { //if the white space is a number
//                            numberedCells.add(d); //add that number to numberedCells
//                        }
//                    }
//                    for (NurikabeCell number : numberedCells) { //loops through numberedCells
//                        if (regions.getSet(number).size()-1 == number.getData()) { //if that cells white area is the exact
//                            caseBoard.addPickableElement(element);
//                            break;
//                        }
//                    }
//
////                    caseBoard.addPickableElement(element);
////                    break;
//                }
//            }
//        }



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
        ArrayList<Board> cases = new ArrayList<>();
        NurikabeBoard nuriBoard = (NurikabeBoard) board.copy();
        NurikabeCell numbaCell = nuriBoard.getCell(((NurikabeCell) puzzleElement).getLocation().x,
                ((NurikabeCell) puzzleElement).getLocation().y);
        int filledRoomSize = numbaCell.getData();
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
            NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy();
            DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nurikabeBoard);
            Set<NurikabeCell> disRow = regions.getSet((NurikabeCell) puzzleElement); //set of white spaces
            for (NurikabeCell d : disRow) { //loops through white spaces
                for(Point direction : directions) {
                    NurikabeCell curr = nurikabeBoard.getCell(d.getLocation().x + direction.x, d.getLocation().y + direction.y);
                    if (curr.getType() == NurikabeType.UNKNOWN) { //found adjacent space to region that is currently unknown
                        Set<NurikabeCell> disCreatedRow = regions.getSet(curr);
                        if (disCreatedRow.size() == filledRoomSize) {
                            Point here = curr.getLocation();
                            boolean alreadyIn = false;
                            for(Point p : locations) {
                                if(p == here) {
                                    alreadyIn = true;
                                }
                            }
                            if(!alreadyIn) {
                                Board casey = board.copy();
                                PuzzleElement datacasey = curr;
                                datacasey.setData(NurikabeType.WHITE.toValue());
                                casey.addModifiedData(datacasey);
                                cases.add(casey);
                                locations.add(here);
                                moreCases = true;
                            }
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
