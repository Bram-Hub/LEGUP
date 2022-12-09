package edu.rpi.legup.puzzle.skyscrapers.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UnresolvedNumberContradictionRule extends ContradictionRule {

    public UnresolvedNumberContradictionRule() {
        super("SKYS-CONT-0005", "Unresolved Number",
                "No possible cell for a number without a duplicate contradiction.",
                //specify a number? defaulting to every number for now. expand to more than duplicate?
                "edu/rpi/legup/images/skyscrapers/contradictions/UnresolvedNumber.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        Point loc = cell.getLocation();

        CellForNumberCaseRule caseRule = new CellForNumberCaseRule();
        for(int i=0;i<skyscrapersBoard.getWidth();i++){
            int num = i+1;
            //check row
            //isn't already present
            boolean exists = false;
            for(SkyscrapersCell presentCell : skyscrapersBoard.getRowCol(loc.y,SkyscrapersType.Number,true)) {
                if (presentCell.getData() == num) {
                    exists = true;
                    break;
                }
            }
            if(!exists){
                //and no possible cases
                if(caseRule.getCasesFor(board,skyscrapersBoard.getWestClues().get(loc.y),num).size()==0) {
                    return null;
                }
            }

            //check col
            //same process as for row
            exists = false;
            for(SkyscrapersCell presentCell : skyscrapersBoard.getRowCol(loc.x,SkyscrapersType.Number,false)) {
                if(presentCell.getData() == num) {
                    exists = true;
                    break;
                }
            }
            if(!exists){
                if(caseRule.getCasesFor(board,skyscrapersBoard.getNorthClues().get(loc.x),num).size()==0) {
                    return null;
                }
            }
        }

        //System.out.print("Does not contain a contradiction at this index");
        return super.getNoContradictionMessage();
    }

    /**
     * Checks whether the tree node has a contradiction using this rule
     *
     * @param board board to check contradiction
     * @return null if the tree node contains a contradiction, otherwise error message
     */
    @Override
    public String checkContradiction(Board board) {
        SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
        for (int i = 0; i < skyscrapersBoard.getWidth(); i++) {
            //checks the middle diagonal (checkContradictionAt checks row/col off each)
            String checkStr = checkContradictionAt(board, skyscrapersBoard.getCell(i,i));
            if (checkStr == null) {
                return null;
            }
        }
        return "No instance of the contradiction " + this.ruleName + " here";
    }
}

