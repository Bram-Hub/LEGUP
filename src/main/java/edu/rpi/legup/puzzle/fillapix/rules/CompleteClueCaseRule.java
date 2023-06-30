package edu.rpi.legup.puzzle.fillapix.rules;

import java.util.ArrayList;
import java.awt.*;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;

public class CompleteClueCaseRule extends CaseRule {
    public CompleteClueCaseRule() {
        super("FPIX-CASE-0002",
        "Complete Clue",
        "Each clue must touch that number of squares.",
        "edu/rpi/legup/images/fillapix/cases/CompleteClue.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(fillapixBoard, this);
        fillapixBoard.setModifiable(false);
        for (PuzzleElement data : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) data;
            if (cell.getNumber() >= 0 && cell.getNumber() <= 9 && FillapixUtilities.hasEmptyAdjacent(fillapixBoard, cell)) {
                // TODO: make sure cell is not complete
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<Board>();

        // get value of cell
        FillapixCell cell = (FillapixCell) board.getPuzzleElement(puzzleElement);
        int cellMaxBlack = cell.getNumber();
        if (cellMaxBlack < 0 || cellMaxBlack > 9) { // cell is not valid cell
            return null;
        }

        // find number of black & empty squares
        int cellNumBlack = 0;
        int cellNumEmpty = 0;
        ArrayList<FillapixCell> emptyCells = new ArrayList<FillapixCell>();
        Point cellLoc = cell.getLocation();
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(cellLoc.x + i, cellLoc.y + j);
                if (adjCell == null) {
                    continue;
                }
                if (adjCell.getType() == FillapixCellType.BLACK) {
                    cellNumBlack++;
                }
                if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                    cellNumEmpty++;
                    emptyCells.add(adjCell);
                }
            }
        }
        // no cases if no empty or if too many black already
        if (cellNumBlack > cellMaxBlack || cellNumEmpty == 0) {
            return cases;
        }
        
        // generate all cases as boolean expressions
        ArrayList<boolean[]> combinations;
        combinations = FillapixUtilities.getCombinations(cellMaxBlack - cellNumBlack, cellNumEmpty); 

        for (int i=0; i < combinations.size(); i++) {
            Board case_ = board.copy();
            for (int j=0; j < combinations.get(i).length; j++) {
                cell = (FillapixCell) case_.getPuzzleElement(emptyCells.get(j));
                if (combinations.get(i)[j]) {
                    cell.setType(FillapixCellType.BLACK);
                } else {
                    cell.setType(FillapixCellType.WHITE);
                }
                case_.addModifiedData(cell);
            }
            cases.add(case_);
        }
        
        return cases;
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