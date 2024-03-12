package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
import java.util.HashSet;


public class CompleteRowColumnDirectRule extends DirectRule {

    public CompleteRowColumnDirectRule() {
        super("BINA-BASC-0003",
                "Complete Row Column",
                "If a row/column of length n contains n/2 of a single value, the remaining cells must contain the other value",
                "edu/rpi/legup/images/binary/rules/CompleteRowColumnDirectRule.png");
    }
    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard initialBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryBoard finalBoard = (BinaryBoard) transition.getBoard();
        int elementIndex = puzzleElement.getIndex();
        int boardDim = initialBoard.getWidth();
        int elementRow = elementIndex / boardDim;
        int elementCol = elementIndex % boardDim;
        Set<BinaryCell> curRow = initialBoard.getRow(elementRow);
        Set<BinaryCell> curCol = initialBoard.getCol(elementCol); 
     
            int numColZeros = 0;
            int numColOnes = 0;

        for (int i = 0; i < boardDim; i++) {
            BinaryCell cell = initialBoard.getCell(i, elementCol);
            if(cell.getData() == 1){
                numColOnes ++;
            }
            else if(cell.getData() == 0){
                numColZeros ++; 
            }
        }
            int numRowZeros = 0;
            int numRowOnes = 0;

        for (int i = 0; i < boardDim; i++) {
            BinaryCell cell = initialBoard.getCell(elementRow, i);
            if(cell.getData() == 1){
                numRowOnes ++;
            }
            else if(cell.getData() == 0){
                numRowZeros ++; 
            }
        }
        if (numColOnes + numColZeros != boardDim) {
            return super.getInvalidUseOfRuleMessage() + ": The column for the specificed element is not complete";
        }
         if (numRowOnes + numRowZeros != boardDim) {
            return super.getInvalidUseOfRuleMessage() + ": The row for the specified element is not complete";
        }
        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}