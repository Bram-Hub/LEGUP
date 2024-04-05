package edu.rpi.legup.puzzle.binary.rules;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class ThreeAdjacentContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a zero or one";

    public ThreeAdjacentContradictionRule() {
        super("BINA-CONT-0001",
                "Three Adjacent",
                "There must not be three adjacent zeros or three adjacent ones in a row or column",
                "edu/rpi/legup/images/binary/rules/ThreeAdjacentZerosContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        int height = binaryBoard.getHeight();
        int width = binaryBoard.getWidth();

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        
        int cellX = cell.getLocation().x;
        int cellY = cell.getLocation().y;
        BinaryCell oneUp = null;
        BinaryCell oneDown = null;
        BinaryCell oneForward = null;
        BinaryCell oneBackward = null;
        if(binaryBoard.getCell(cellX, cellY + 1) != null){
            oneUp = binaryBoard.getCell(cellX, cellY + 1);
        }
        if(binaryBoard.getCell(cellX, cellY - 1) != null){
            oneDown = binaryBoard.getCell(cellX, cellY - 1);
        }
        if(binaryBoard.getCell(cellX + 1, cellY) != null){
            oneForward = binaryBoard.getCell(cellX + 1, cellY);
        }
        if(binaryBoard.getCell(cellX - 1, cellY) != null){
            oneBackward = binaryBoard.getCell(cellX - 1, cellY);
        }        
        //System.out.println("UP: " + oneUp.getLocation() + " " + "DOWN: " + oneDown.getLocation() + 
        //    " " + "BACKWARD: " + oneBackward.getLocation() + " " + "FORWARD: " + oneForward.getLocation() + " " + "CELL: " + cell.getLocation());        


        if(cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            if(oneBackward != null && oneForward != null){
                if(oneBackward.getType() == cell.getType() && oneForward.getType() == cell.getType())
                    return null;
            }
            if(oneUp != null && oneDown != null){  
                if(oneUp.getType() == cell.getType() && oneDown.getType() == cell.getType())
                    return null;
            }
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}