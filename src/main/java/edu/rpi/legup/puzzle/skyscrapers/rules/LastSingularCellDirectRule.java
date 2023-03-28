package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.util.ArrayList;

public class LastSingularCellDirectRule extends DirectRule {

    public LastSingularCellDirectRule() {
        super("SKYS-BASC-0002", "Last Non-Duplicate Cell",
                "There is only one cell on this row/col for this number that does not create a duplicate contradiction",
                "edu/rpi/legup/images/skyscrapers/rules/LastCell.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        SkyscrapersBoard initialBoard = (SkyscrapersBoard) transition.getParents().get(0).getBoard();
        SkyscrapersCell initCell = (SkyscrapersCell) initialBoard.getPuzzleElement(puzzleElement);
        SkyscrapersBoard finalBoard = (SkyscrapersBoard) transition.getBoard();
        SkyscrapersCell finalCell = (SkyscrapersCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(initCell.getType() == SkyscrapersType.UNKNOWN && finalCell.getType() == SkyscrapersType.Number)) {
            return super.getInvalidUseOfRuleMessage() + ": Modified cells must be number";
        }

        //set all rules used by case rule to false except for dupe, get all cases
        boolean dupeTemp = initialBoard.getDupeFlag();
        boolean viewTemp = initialBoard.getViewFlag();
        initialBoard.setDupeFlag(true);
        initialBoard.setViewFlag(false);
        CellForNumberCaseRule caseRule = new CellForNumberCaseRule();
        ArrayList<Board> XCandidates = caseRule.getCasesFor(initialBoard,initialBoard.getWestClues().get(finalCell.getLocation().y),(Integer)finalCell.getData().value);
        ArrayList<Board> YCandidates = caseRule.getCasesFor(initialBoard,initialBoard.getNorthClues().get(finalCell.getLocation().x),(Integer)finalCell.getData().value);
        initialBoard.setDupeFlag(dupeTemp);
        initialBoard.setViewFlag(viewTemp);

        System.out.println(XCandidates.size());
        System.out.println(YCandidates.size());

        //return null if either pass, both messages otherwise
        String xCheck = candidateCheck(XCandidates,puzzleElement,finalCell);
        String yCheck = candidateCheck(YCandidates,puzzleElement,finalCell);
        if(xCheck==null || yCheck==null){
            return null;
        }
        return super.getInvalidUseOfRuleMessage() + "\nRow" + xCheck + "\nCol" + yCheck;
    }

    //helper to check if candidate list is valid
    private String candidateCheck(ArrayList<Board> candidates,PuzzleElement puzzleElement, SkyscrapersCell finalCell){
        if(candidates.size() == 1){
            if(((SkyscrapersCell) candidates.get(0).getPuzzleElement(puzzleElement)).getType() == SkyscrapersType.Number) {
                if (candidates.get(0).getPuzzleElement(puzzleElement).getData() == finalCell.getData()) {
                    return null;
                }
                return ": Wrong number in the cell.";
            }
            return ": No case for this cell.";
        }
        return ": This cell is not forced.";
    }

    private boolean isForced(SkyscrapersBoard board, SkyscrapersCell cell) {
        SkyscrapersBoard emptyCase = board.copy();
        emptyCase.getPuzzleElement(cell).setData(0);
        DuplicateNumberContradictionRule duplicate = new DuplicateNumberContradictionRule();
        if (duplicate.checkContradictionAt(emptyCase, cell) == null) {
            System.out.println("no contradiction ln");
            return true;
        }
        return false;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        SkyscrapersBoard initialBoard = (SkyscrapersBoard) node.getBoard();
        SkyscrapersBoard lightUpBoard = (SkyscrapersBoard) node.getBoard().copy();
        System.out.println(lightUpBoard.getPuzzleElements().size());
        for (PuzzleElement element : lightUpBoard.getPuzzleElements()) {
            System.out.println("123");
            SkyscrapersCell cell = (SkyscrapersCell) element;
            if (cell.getType() == SkyscrapersType.UNKNOWN && isForced(initialBoard, cell)) {
                //cell.setData(SkyscrapersType.BULB.value);
                lightUpBoard.addModifiedData(cell);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        }
        else {
            return lightUpBoard;
        }
    }
}
