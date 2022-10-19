package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;

import java.awt.*;
import java.util.List;

public class PreemptiveVisibilityContradictionRule extends ContradictionRule{

    public PreemptiveVisibilityContradictionRule() {
        super("SKYS-CONT-0006", "Preemptive Visibility",
                "Visibility constraints are not met given an incomplete row/col",
                "edu/rpi/legup/images/skyscrapers/contradictions/PreemptiveVisibility.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        Point loc = cell.getLocation();

        //get borders
        int west  = skyscrapersBoard.getWestClues().get(loc.y).getData();
        int east  = skyscrapersBoard.getEastClues().get(loc.y).getData();
        int north  = skyscrapersBoard.getNorthClues().get(loc.x).getData();
        int south  = skyscrapersBoard.getSouthClues().get(loc.x).getData();

        InsufficientVisibilityContradictionRule tooFew = new InsufficientVisibilityContradictionRule();
        ExceedingVisibilityContradictionRule tooMany = new ExceedingVisibilityContradictionRule();
        CellForNumberCaseRule caseRule = new CellForNumberCaseRule();




        // find all cases for the corresponding row and column for each possible skyscraper height
        for(int i = 0; i < skyscrapersBoard.getWidth(); i++) {
            int num = i + 1;
            //find every case board for a height

            //check row west clue
            List<Board> westBoards = caseRule.getCasesFor(board, skyscrapersBoard.getWestClues().get(loc.y), num);
            String tooFewWest;
            String tooManyWest;
            boolean contradictionWest = true;
            //check if each case board has a contradiction
            for(int j = 0; j < westBoards.size(); j++) {
                //checks if there is a contradiction given the row based on the west clue
                tooFewWest = tooFew.checkContradictionAt(westBoards.get(j), cell); // is cell the correct puzzle element to check?
                tooManyWest = tooMany.checkContradictionAt(westBoards.get(j), cell);
                if(tooFewWest != null || tooManyWest != null) {// !null means there isn't a contradiction, so there must be a valid permutation of the array
                    contradictionWest = false;
                }
            }

            //check row east clue
            List<Board> eastBoards = caseRule.getCasesFor(board, skyscrapersBoard.getEastClues().get(loc.y), num);
            String tooFewEast;
            String tooManyEast;
            boolean contradictionEast = true;
            for(int j = 0; j < eastBoards.size(); j++) {
                tooFewEast = tooFew.checkContradictionAt(eastBoards.get(j), cell);
                tooManyEast = tooMany.checkContradictionAt(eastBoards.get(j), cell);
                if(tooFewEast != null || tooManyEast != null) {
                    contradictionEast = false;
                }
            }

            //check col north clue
            List<Board> northBoards = caseRule.getCasesFor(board, skyscrapersBoard.getNorthClues().get(loc.x), num);
            String tooFewNorth;
            String tooManyNorth;
            boolean contradictionNorth = true;
            for(int j = 0; j < northBoards.size(); j++) {
                tooFewNorth = tooFew.checkContradictionAt(northBoards.get(j), cell);
                tooManyNorth = tooMany.checkContradictionAt(northBoards.get(j), cell);
                if(tooFewNorth != null || tooManyNorth != null) {
                    contradictionNorth = false;
                }
            }

            //check col south clue
            List<Board> southBoards = caseRule.getCasesFor(board, skyscrapersBoard.getSouthClues().get(loc.x), num);
            String tooFewSouth;
            String tooManySouth;
            boolean contradictionSouth = true;
            for(int j = 0; j < southBoards.size(); j++) {
                tooFewSouth = tooFew.checkContradictionAt(southBoards.get(j), cell);
                tooManySouth = tooMany.checkContradictionAt(southBoards.get(j), cell);
                if(tooFewSouth != null || tooManySouth != null) {
                    contradictionSouth = false;
                }
            }

            //if every possible permutation results in contradictions
            if(contradictionWest && contradictionEast && contradictionNorth && contradictionSouth)
                return null;
            return super.getNoContradictionMessage();
        }

        return null;

    }



}
