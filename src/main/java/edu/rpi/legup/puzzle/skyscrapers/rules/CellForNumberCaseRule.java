package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CellForNumberCaseRule extends CaseRule {
    //select a certain row/col? select a certain number?
    public CellForNumberCaseRule() {
        super("SKYS-CASE-0002", "Cell For Number",
                "A number (1-n) must appear in any given row/column",
                "edu/rpi/legup/images/skyscrapers/cases/CellForNumber.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        SkyscrapersBoard currentBoard = (SkyscrapersBoard) board.copy();
        currentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(currentBoard, this);
        for (SkyscrapersClue data : currentBoard.getRow()) {
            System.out.println(data.getType());
            caseBoard.addPickableElement(data);
        }
        for (SkyscrapersClue data : currentBoard.getCol()) {
            System.out.println(data.getType());
            caseBoard.addPickableElement(data);
        }
        return caseBoard;
    }

    public List<Board> getCasesFor(Board board, PuzzleElement puzzleElement, Integer number){
        //return null;
        ArrayList<Board> cases = new ArrayList<>();

        SkyscrapersClue clue = (SkyscrapersClue) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;


        List<SkyscrapersCell> openCells = skyscrapersboard.getRowCol(clue.getClueIndex(),SkyscrapersType.UNKNOWN,clue.getType()==SkyscrapersType.CLUE_WEST);
        for(SkyscrapersCell cell : openCells){
            Board newCase = board.copy();
            PuzzleElement newCell = newCase.getPuzzleElement(cell);
            newCell.setData(number);
            newCase.addModifiedData(newCell);
            //if flags
            boolean passed = true;
            if(skyscrapersboard.getDupeFlag()){
                DuplicateNumberContradictionRule DupeRule = new DuplicateNumberContradictionRule();
                passed = passed && DupeRule.checkContradictionAt(newCase,newCell)!=null;
            }
            if(skyscrapersboard.getViewFlag()){
                InsufficientVisibilityContradictionRule fewRule = new InsufficientVisibilityContradictionRule();
                ExceedingVisibilityContradictionRule moreRule = new ExceedingVisibilityContradictionRule();
                passed = passed && fewRule.checkContradictionAt(newCase,newCell)!=null && moreRule.checkContradictionAt(newCase,newCell)!=null;
            }
            //how should unresolved be handled? should it be?
            if(passed){cases.add(newCase);}


        }
        return cases;
    }

    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
        int number = 1; //hard-coded for now
        return getCasesFor(board,puzzleElement,number);
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
