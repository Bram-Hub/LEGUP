package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;
import edu.rpi.legup.utility.DisjointSets;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FinishRoomDirectRule extends DirectRule{

    public FinishRoomDirectRule() {
        super(
                "NURI-BASC-0007",
                "Finish Room",
                "Finishes Room",
                "edu/rpi/legup/images/nurikabe/cases/FinishRoom.png");
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
        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        NurikabeCell cell = (NurikabeCell) destBoardState.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.WHITE) {
            return "Only Number cells are allowed for this rule!";
        }
        NurikabeBoard modified = origBoardState.copy();
        NurikabeCell modCell = (NurikabeCell) modified.getPuzzleElement(puzzleElement);
        modCell.setData(NurikabeType.WHITE.toValue());

        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(modified);
        Set<NurikabeCell> disRow = regions.getSet(modCell);
        Set<NurikabeCell> disCreatedRow = regions.getSet(modCell);
        int filledRoomSize = -1;
        boolean only = false;
        Point numLoc = new Point(-1, -1);
        for(NurikabeCell num : disCreatedRow) {
            if(num.getType() == NurikabeType.NUMBER) {
                if(!only) {
                   filledRoomSize = num.getData();
                    only = true;
                    numLoc = num.getLocation();
                }
                else {
                    if(num.getData() != filledRoomSize) {
                        return "Room can't be generated with different numbered number tiles";
                    }
                }
            }
        }
        if(filledRoomSize == -1) {
            return "Room must have a number tile within it";
        }
        if (disCreatedRow.size() != filledRoomSize) {
            return "Room must be of size of the number tile within it";
        }
        NurikabeCell prev = (NurikabeCell) origBoardState.getCell((int)numLoc.getX(), (int)numLoc.getY());
        DisjointSets<NurikabeCell> oldRegions = NurikabeUtilities.getNurikabeRegions(origBoardState);
        Set<NurikabeCell> disOldRow = oldRegions.getSet(prev);
        if(filledRoomSize - 1 != disOldRow.size()) {
            return "Only rooms with a previous size of 1 less than completion can be completed using this rule";
        }
        //loop all around the modified region. If any location around is not unknown or out of bounds, unless it
        //around the newest block, return error message
        //modcell is the exception cell location
        Set<Point> locations = new HashSet<>();
        for(NurikabeCell nu : disOldRow) {
            locations.add(nu.getLocation());
        }
        Point left = new Point(-1, 0);
        Point right = new Point(1, 0);
        Point bot = new Point(0, -1);
        Point top = new Point(0, 1);
        Set<Point> directions = new HashSet<>();
        directions.add(left);
        directions.add(right);
        directions.add(top);
        directions.add(bot);
        int height = destBoardState.getHeight();
        int width = destBoardState.getWidth();
        for(NurikabeCell c : disCreatedRow) {
            if(c.getLocation().equals(modCell.getLocation())) {
                if(c.getType() == NurikabeType.WHITE || c.getType() == NurikabeType.NUMBER) {
                    boolean beenThere = false;
                    for(Point l : locations) {
                        if(c.getLocation().equals(l)) {
                            beenThere = true;
                        }
                    }
                    if(!beenThere) {
                        //if c is number tile and same as number that been in it, allow
                        //else return badness
                    }
                }
                //exception way
                //around the white block that was just added
                //if any of the blocks around are white, then must be in the room previously.
            }
            else {
                //regular way
                for(Point direction : directions) {
                    if(!((c.getLocation().x + direction.x >= 0 && c.getLocation().x + direction.x < width) &&
                            (c.getLocation().y + direction.y >= 0 && c.getLocation().y + direction.y < height))) {
                        continue;
                    }
                    if(c.getType() != NurikabeType.BLACK) {
                        boolean beenInRoom = false;
                        for(Point l : locations) {
                            if(c.getLocation().equals(l)) {
                                beenInRoom = true;
                            }
                        }
                        if(!beenInRoom) {
                            //could still be fine if connected 2 disjoint rooms into 1 with same number block
                        }
                    }
                        //(!((nuriBoard.getWidth() > (d.getLocation().x + direction.x) && (nuriBoard.getHeight() > d.getLocation().y + direction.y) &&
                               // (d.getLocation().x + direction.x >= 0) && (d.getLocation().y + direction.y >= 0))))
                }
            }
        }


        return null;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }


}
