package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Point;

public class NonTouchingSharedEmptyDirectRule extends DirectRule {
    public NonTouchingSharedEmptyDirectRule() {
        super(
                "MINE-BASC-0003",
                "Non Shared Empty",
                "Adjacent cells with numbers have the same difference in mine in their unshared\n"
                        + " regions as the difference in their numbers",
                "edu/rpi/legup/images/minesweeper/direct/NonSharedEmpty.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();
        MinesweeperBoard parentBoard =
                (MinesweeperBoard) transition.getParents().get(0).getBoard().copy();
        MinesweeperBoard parentBoard2 = parentBoard.copy();
        MinesweeperCell cell = (MinesweeperCell) board.getPuzzleElement(puzzleElement);
        MinesweeperCell parentCell = (MinesweeperCell) parentBoard.getPuzzleElement(puzzleElement);
        if (!(parentCell.getTileType() == MinesweeperTileType.UNSET
                && cell.getTileType() == MinesweeperTileType.EMPTY)) {

            return super.getInvalidUseOfRuleMessage()
                    + ": This cell must be empty to be applicable with this rule.";
        }
        if (MinesweeperUtilities.checkBoardForContradiction(board)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be empty";
        }

        //get location of cell/parentCell to compare with AdjCells later
        Point parentCellLoc = parentCell.getLocation();

        // get all adjCells that have a number
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(parentBoard, parentCell);
        adjCells.removeIf(x -> x.getTileNumber() < 1 || x.getTileNumber() >= 9);
        /* remove any number cell that does not have another number cell
         * adjacent to it on the opposite side of the modified cell */
        Iterator<MinesweeperCell> itr = adjCells.iterator();
        while (itr.hasNext()) {
            MinesweeperCell adjCell = itr.next();

            //get location of this AdjCell to compare with cell/parentCell
            Point adjCellLoc = adjCell.getLocation();

            //check if this AdjCell has another 'number cell' on the opposite side of the parentCell
            if (adjCellLoc.x == parentCellLoc.x){//in the same column
                if(parentCellLoc.y >= 1 && adjCellLoc.y > parentCellLoc.y){//adjCell is below parentCell
                    //get cell above parentCell
                    MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x, parentCellLoc.y-1).copy();

                    //check oppositeSideCell, rook
                    if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                        continue;// do not remove from adj cell list
                    }

                    //knight check right
                    if(parentCellLoc.x < parentBoard.getWidth()){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x+1, parentCellLoc.y-1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }

                    //knight check left
                    if(parentCellLoc.x >= 1){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x-1, parentCellLoc.y-1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }


                } else if(parentCellLoc.y < parentBoard.getHeight()-1 && adjCellLoc.y < parentCellLoc.y){//adjCell is above parentCell
                    //get cell below parentCell
                    MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x, parentCellLoc.y+1).copy();

                    //check oppositeSideCell, rook
                    if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                        continue;// do not remove from adj cell list
                    }

                    //knight check right
                    if(parentCellLoc.x < parentBoard.getWidth()){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x+1, parentCellLoc.y+1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }

                    //knight check left
                    if(parentCellLoc.x >=1){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x-1, parentCellLoc.y+1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }
                }
            }
            else if (adjCellLoc.y == parentCellLoc.y){//in the same row
                if(parentCellLoc.x >= 1 && adjCellLoc.x > parentCellLoc.x){//adjCell is right of parentCell
                    //get cell left of parentCell
                    MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x-1, parentCellLoc.y).copy();

                    //check oppositeSideCell, rook
                    if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                        continue;// do not remove from adj cell list
                    }

                    //knight check up
                    if(parentCellLoc.y >= 1){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x-1, parentCellLoc.y-1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }

                    //knight check down
                    if(parentCellLoc.y < parentBoard.getHeight()){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x-1, parentCellLoc.y+1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }

                } else if(parentCellLoc.x < parentBoard.getWidth()-1 && adjCellLoc.x < parentCellLoc.x){//adjCell is left of parentCell
                    //get cell right of parentCell
                    MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x+1, parentCellLoc.y).copy();

                    //check oppositeSideCell, rook
                    if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                        continue;// do not remove from adj cell list
                    }

                    //knight check up
                    if(parentCellLoc.y >= 1){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x+1, parentCellLoc.y-1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }

                    //knight check down
                    if(parentCellLoc.y < parentBoard.getHeight()){
                        oppositeSideCell.setLocation(new Point(parentCellLoc.x+1, parentCellLoc.y+1));
                        if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                            continue;// do not remove from adj cell list
                        }
                    }

                }
            }
            else {//opposite side check, bishop
               if(adjCellLoc.x > parentCellLoc.x){
                   if(adjCellLoc.y > parentCellLoc.y){// adjCell is SE of parentCell
                       if(parentCellLoc.x >= 1 && parentCellLoc.y >= 1){
                           MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x-1, parentCellLoc.y-1);
                           if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                               continue;// do not remove from adj cell list
                           }
                       }
                   } else {// adjCell is NE of parentCell
                       if(parentCellLoc.x>= 1 && parentCellLoc.y < parentBoard.getHeight() - 1){
                           MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x-1, parentCellLoc.y+1);
                           if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                               continue;// do not remove from adj cell list
                           }
                       }
                   }
               } else {
                   if(adjCellLoc.y > parentCellLoc.y){// adjCell is SW of parentCell
                       if(parentCellLoc.x < parentBoard.getWidth() -1 && parentCellLoc.y >= 1){
                           MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x+1, parentCellLoc.y-1);
                           if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                               continue;// do not remove from adj cell list
                           }
                       }
                   } else {// adjCell is NW of parentCell
                       if(parentCellLoc.x < parentBoard.getWidth() -1 && parentCellLoc.y < parentBoard.getHeight() -1){
                           MinesweeperCell oppositeSideCell = parentBoard.getCell(parentCellLoc.x+1, parentCellLoc.y+1);
                           if(oppositeSideCell.getTileNumber()>=1 && oppositeSideCell.getTileNumber()<9){
                               continue;// do not remove from adj cell list
                           }
                       }
                   }
               }

            }



            boolean found = false;
            ArrayList<MinesweeperCell> adjAdjCells =
                    MinesweeperUtilities.getAdjacentCells(parentBoard, adjCell);
            for (MinesweeperCell adjAdjCell : adjAdjCells) {
                if (adjAdjCell.getTileNumber() >= 1
                        && adjAdjCell.getTileNumber() < 9
                        && adjAdjCell.getIndex() != parentCell.getIndex()) {
                    // adjAdjCell is adjacent to adjCell && it has a
                    // number && it is not parentCell
                    found = true;
                }
            }

            // does not qualify for this rule
            if (!found) {
                itr.remove();
            }
        }
        // change the cell to be a mine instead of empty
        parentCell.setCellType(MinesweeperTileData.mine());
        // check for some contradiction in all cases
        parentBoard.addModifiedData(parentCell);
        CaseRule completeClue = new SatisfyNumberCaseRule();
        List<Board> caseBoards;
        for (MinesweeperCell adjCell : adjCells) {
            caseBoards = completeClue.getCases(parentBoard, adjCell);
            // System.out.println(adjCell.getLocation().x + " " + adjCell.getLocation().y);
            boolean found = true;
            for (Board b : caseBoards) {
                if (!MinesweeperUtilities.checkBoardForContradiction((MinesweeperBoard) b)) {
                    found = false;
                }
            }

            if (found) {
                return null;
            }
        }

        return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be empty";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) node.getBoard().copy();
        for (PuzzleElement element : minesweeperBoard.getPuzzleElements()) {
            MinesweeperCell cell = (MinesweeperCell) element;
            if (cell.getTileType() == MinesweeperTileType.UNSET
                    && MinesweeperUtilities.isForcedEmpty(
                            (MinesweeperBoard) node.getBoard(), cell)) {
                cell.setCellType(MinesweeperTileData.empty());
                minesweeperBoard.addModifiedData(cell);
            }
        }
        if (minesweeperBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return minesweeperBoard;
        }
    }
}
