package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.*;
import org.apache.commons.lang3.ObjectUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CellForNumberCaseRule extends CaseRule {
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

    public ArrayList<Board> getCasesFor(Board board, PuzzleElement puzzleElement, Integer number){
        //return null;
        ArrayList<Board> cases = new ArrayList<>();

        SkyscrapersClue clue = (SkyscrapersClue) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;


        List<SkyscrapersCell> openCells = skyscrapersboard.getRowCol(clue.getClueIndex(),SkyscrapersType.UNKNOWN,clue.getType()==SkyscrapersType.CLUE_WEST);
        for(SkyscrapersCell cell : openCells){
            Board newCase = board.copy();
            PuzzleElement newCell = newCase.getPuzzleElement(cell);
            newCell.setData(number);

            newCase.getPuzzleElement(clue).setModified(true);
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
            if(passed){cases.add(newCase);}


        }
        return cases;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        int number = 1; //hard-coded for now
        return getCasesFor(board,puzzleElement,number);
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        SkyscrapersBoard oldBoard = (SkyscrapersBoard) transition.getParents().get(0).getBoard();
        if (childTransitions.size() == 0) {
            return "This case rule must have at least one child.";
        }

        //find changed row/col
        SkyscrapersClue modClue = null;
        for(SkyscrapersClue clue : ((SkyscrapersBoard)childTransitions.get(0).getBoard()).getRow()){
            if(clue.isModified()){
                modClue = clue;
                break;
            }
        }
        if(modClue!=null){
            for(SkyscrapersClue clue : ((SkyscrapersBoard)childTransitions.get(0).getBoard()).getCol()){
                if(clue.isModified()){
                    modClue = clue;
                    break;
                }
            }
        }

        if(childTransitions.size() != getCasesFor(oldBoard,modClue,(Integer) childTransitions.get(0).getBoard().getModifiedData().iterator().next().getData()).size()){
            System.out.println("Wrong number of cases.");
            return "Wrong number of cases.";
        }

        for(TreeTransition newTree : childTransitions){
            SkyscrapersBoard newBoard = (SkyscrapersBoard) newTree.getBoard();
            if(newBoard.getModifiedData().size()!=1){
                System.out.println("Only one cell should be modified.");
                return "Only one cell should be modified.";
            }
            SkyscrapersCell newCell = (SkyscrapersCell) newBoard.getModifiedData().iterator().next();
            if(newCell.getType() != SkyscrapersType.Number){
                System.out.println("Changed value should be a number.");
                return "Changed value should be a number.";
            }
        }
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRaw(transition);
    }
}
