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
        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        if (childTransitions.size() != 2) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 2 children.";
        }

        TreeTransition case1 = childTransitions.get(0);
        TreeTransition case2 = childTransitions.get(1);
        if (case1.getBoard().getModifiedData().size() != 1 ||
                case2.getBoard().getModifiedData().size() != 1) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must have 1 modified cell for each case.";
        }

        NurikabeCell mod1 = (NurikabeCell) case1.getBoard().getModifiedData().iterator().next();
        NurikabeCell mod2 = (NurikabeCell) case2.getBoard().getModifiedData().iterator().next();
        if (!(mod1.getType() == NurikabeType.WHITE && mod2.getType() == NurikabeType.WHITE)) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must place 2 empty white cells.";
        }
        if (mod1.getLocation().equals(mod2.getLocation())) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify a different cell for each case.";
        }
        Point loc1 = mod1.getLocation(); //position of placed cell
        Point loc2 = mod2.getLocation(); //position of other placed cell
        Set<NurikabeCell> adj = new HashSet<>(); //set to hold adjacent cells
        Set<NurikabeCell> adj2 = new HashSet<>(); //set to hold adjacent cells
        Point left = new Point(-1,0);
        Point right = new Point(1, 0);
        Point bot = new Point(0, -1);
        Point top = new Point(0, 1);
        Set<Point> directions = new HashSet<>();
        directions.add(left);
        directions.add(right);
        directions.add(top);
        directions.add(bot);
        for(Point direction : directions) {
            NurikabeCell curr = destBoardState.getCell(loc1.x + direction.x, loc1.y + direction.y);
            if(curr != null) {
                if(curr.getType() == NurikabeType.WHITE || curr.getType() == NurikabeType.NUMBER) {
                    adj.add(curr); //adds cells to adj only if they are white or number blocks
                }
            }
            NurikabeCell curr2 = destBoardState.getCell(loc2.x + direction.x, loc2.y + direction.y);
            if(curr2 != null) {
                if(curr2.getType() == NurikabeType.WHITE || curr2.getType() == NurikabeType.NUMBER) {
                    adj2.add(curr2);
                }
            }
        }
        boolean match = false;
//        for(NurikabeCell c1 : adj) {
//            for(NurikabeCell c2 : adj2) {
//                if(c1.getLocation() == c2.getLocation()) {
//                    match = true;
//                    break;
//                }
//            }
//            if(match) {
//                break;
//            }
//        }
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(destBoardState);

        for (NurikabeCell c : adj) { //loops through adjacent cells
            Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
            for (NurikabeCell d : disRow) { //loops through white spaces
                for (NurikabeCell c2 : adj2) {
                    Set<NurikabeCell> disRow2 = regions.getSet(c2);
                    for (NurikabeCell d2 : disRow2) {
                        if(d.getLocation() == d2.getLocation()) {
                            match = true;
                        }
                    }
                }
            }
        }

        if(!match) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify cells adjacent to the same white region.";
        }
        //issue is ensuring that the 2 modified locations are related to the same white region. can't just make sure
        //white number size is same because there can be multiple that are the same
        //maybe point of white number being the same??
        for (NurikabeCell c : adj) { //loops through adjacent cells
            Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
            //if the location of any of the blocks is the same. User should realize that solution can't be made
            //if they correctly fill one room but it adds too many squares to another already completed room
            for (NurikabeCell d : disRow) { //loops through white spaces
                if (d.getType() == NurikabeType.NUMBER) { //if the white space is a number
                    if(regions.getSet(d).size()-1 == d.getData()) { //if that cells white area is 1 less than
                        //return null; //the size of the number of one of the number cells within that set
                        match = true;
                    }
                }
            }
        }
        if(!match) {
            return super.getInvalidUseOfRuleMessage() + ": This case rule must modify cells adjacent to the same white region that " +
                    "needs one more white cell to complete the room.";
        }
        for (NurikabeCell c : adj2) { //loops through adjacent cells
            Set<NurikabeCell> disRow = regions.getSet(c); //set of white spaces
            //if the location of any of the blocks is the same. User should realize that solution can't be made
            //if they correctly fill one room but it adds too many squares to another already completed room
            for (NurikabeCell d : disRow) { //loops through white spaces
                if (d.getType() == NurikabeType.NUMBER) { //if the white space is a number
                    if(regions.getSet(d).size()-1 == d.getData()) { //if that cells white area is 1 less than
                        //return null; //the size of the number of one of the number cells within that set
                        return null;
                    }
                }
            }
        }
        //if the cell is white and size of its column is 1 less than the room size
        //may not work for example with room of size 4 that is finished. But also a near adjacent room of size
        //3 that is also finished. Would validate the 4 - 1 = 3 even tho the 3 room is finished.
        return super.getInvalidUseOfRuleMessage() + ": This case rule must modify cells adjacent to the same white region that " +
                "needs one more white cell to complete the room.";
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(nurikabeBoard, this);
        nurikabeBoard.setModifiable(false);
        for (PuzzleElement element : nurikabeBoard.getPuzzleElements()) {
            if (((NurikabeCell) element).getType() == NurikabeType.UNKNOWN) {
                caseBoard.addPickableElement(element);
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
        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(NurikabeType.WHITE.toValue());
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(NurikabeType.WHITE.toValue());
        case2.addModifiedData(data2);
        cases.add(case2);

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
