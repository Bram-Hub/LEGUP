package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.*;
import org.apache.commons.lang3.ObjectUtils;

import javax.swing.*;
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

    private Integer selectedNumber;

    @Override
    public CaseBoard getCaseBoard(Board board) {
        SkyscrapersBoard currentBoard = (SkyscrapersBoard) board.copy();
        currentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(currentBoard, this);
        for (SkyscrapersClue data : currentBoard.getWestClues()) {
            System.out.println(data.getType());
            caseBoard.addPickableElement(data);
        }
        for (SkyscrapersClue data : currentBoard.getNorthClues()) {
            System.out.println(data.getType());
            caseBoard.addPickableElement(data);
        }

        //selects integer before checking Command.canExecute for use in Command.getErrorString
        int size = ((SkyscrapersBoard)board).getWidth();
        Object[] possibleValues = new Object[size];
        for(int i=0; i<size; i++){
            possibleValues[i] = i+1;
        }
        Object selectedValue;
        do{
            selectedValue = JOptionPane.showInputDialog(null,
                    "Pick the number to be added", "Cell For Number",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    possibleValues, possibleValues[0]);
        }while(selectedValue==null);
        selectedNumber = (Integer)selectedValue;

        return caseBoard;
    }

    public ArrayList<Board> getCasesFor(Board board, PuzzleElement puzzleElement, Integer number){
        ArrayList<Board> cases = new ArrayList<>();

        SkyscrapersClue clue = (SkyscrapersClue) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;

        List<SkyscrapersCell> openCells = skyscrapersboard.getRowCol(clue.getClueIndex(),SkyscrapersType.UNKNOWN,clue.getType()==SkyscrapersType.CLUE_WEST);
        for(SkyscrapersCell cell : openCells){
            SkyscrapersBoard newCase = skyscrapersboard.copy();
            PuzzleElement newCell = newCase.getPuzzleElement(cell);
            newCell.setData(number);
            newCase.addModifiedData(newCell);
            newCase.setModClue((SkyscrapersClue)newCase.getPuzzleElement(clue));

            //if flags
            boolean passed = true;
            if(skyscrapersboard.getDupeFlag()){
                DuplicateNumberContradictionRule DupeRule = new DuplicateNumberContradictionRule();
                passed = passed && DupeRule.checkContradictionAt(newCase,newCell)!=null;
            }
            if(skyscrapersboard.getViewFlag()){
                PreemptiveTooFewContradictionRule ViewRule = new PreemptiveTooFewContradictionRule();
                passed = passed && ViewRule.checkContradictionAt(newCase,newCell)!=null;
            }
            if(passed){cases.add(newCase);}


        }
        return cases;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        return getCasesFor(board,puzzleElement,selectedNumber);
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        List<TreeTransition> childTransitions = transition.getParents().get(0).getChildren();
        SkyscrapersBoard oldBoard = (SkyscrapersBoard) transition.getParents().get(0).getBoard();
        if (childTransitions.size() == 0) {
            return "This case rule must have at least one child.";
        }

        //find changed row/col
        SkyscrapersClue modClue = ((SkyscrapersBoard)childTransitions.get(0).getBoard()).getmodClue();

        System.out.println(modClue.getType());
        System.out.println(modClue.getClueIndex());
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
