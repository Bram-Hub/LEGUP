package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.*;

import java.util.List;

public class CellForNumberCaseRule extends CaseRule {
    //select a certain row/col? select a certain number?
    public CellForNumberCaseRule() {
        super("SKYS-CASE-0002", "Cell For Number",
                "Each blank cell is could have height of 1 to n.",
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

    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
        return null;
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
