package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersClue;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PreemptiveTooFewContradictionRule extends ContradictionRule{

    public PreemptiveTooFewContradictionRule() {
        super("SKYS-CONT-0006", "Preemptive Too Few",
                "Less skyscrapers are visible than there should be in an incomplete row/col",
                "edu/rpi/legup/images/skyscrapers/contradictions/TooFew.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();

        //get borders
        int west  = skyscrapersboard.getWestClues().get(loc.y).getData();
        int east  = skyscrapersboard.getEastClues().get(loc.y).getData();
        int north  = skyscrapersboard.getNorthClues().get(loc.x).getData();
        int south  = skyscrapersboard.getSouthClues().get(loc.x).getData();



        InsufficientVisibilityContradictionRule tooFew = new InsufficientVisibilityContradictionRule();
        CellForNumberCaseRule caseRule = new CellForNumberCaseRule();
        System.out.println("1");


        //Check Row


        //check west


        //create board with every possible row permutation given the current cell

        System.out.println("2");
        List<Board> list = caseRule.getCasesFor(board, skyscrapersBoard.getWestClues().get(loc.y), 1);



//        for(int i = 0; i < list.size(); i++)
//            System.out.println(list.get(i));

        System.out.println("3");




        //for each row call getCasesFor for each number progressively
        //getCasesFor for each clue

        //for each clue iterate 1-n for number

        //
        return null;


    }



}
