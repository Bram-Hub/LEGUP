package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkyscrapersBoard extends GridBoard {

    private ArrayList<SkyscrapersLine> lines;


    private ArrayList<SkyscrapersClue> eastClues;
    //EAST clues
    private ArrayList<SkyscrapersClue> southClues;
    //SOUTH clues
    private ArrayList<SkyscrapersClue> westClues;
    //WEST clues
    private ArrayList<SkyscrapersClue> northClues;
    //NORTH clues

    private boolean viewFlag = false;
    private boolean dupeFlag = false;

    private SkyscrapersClue modClue = null;
    //helper variable for case rule verification, tracks recently modified row/col

    public SkyscrapersBoard(int size) {
        super(size, size);

        this.lines = new ArrayList<>();

        this.eastClues = new ArrayList<>();
        this.southClues = new ArrayList<>();
        this.westClues = new ArrayList<>();
        this.northClues = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            eastClues.add(null);
            southClues.add(null);
            westClues.add(null);
            northClues.add(null);
        }
    }

    public ArrayList<SkyscrapersLine> getLines() {
        return lines;
    }

    /**
    * @return eastClues Returns a list of the eastern clues ordered from loc.y = 0 to max
    */
    public ArrayList<SkyscrapersClue> getEastClues() {
        return eastClues;
    }

    /**
     * @return southClues Returns a list of the southern clues ordered from loc.x = 0 to max
     */
    public ArrayList<SkyscrapersClue> getSouthClues() {
        return southClues;
    }

    /**
     * @return westClues Returns a list of the western clues ordered from loc.y = 0 to max
     */
    public ArrayList<SkyscrapersClue> getWestClues() {
        return westClues;
    }

    /**
     * @return northClues Returns a list of the northern clues ordered from loc.x = 0 to max
     */
    public ArrayList<SkyscrapersClue> getNorthClues() {
        return northClues;
    }

    public boolean getDupeFlag(){
        return dupeFlag;
    }
    public boolean getViewFlag(){
        return viewFlag;
    }
    public void setDupeFlag(boolean newFlag){
        dupeFlag = newFlag;
    }
    public void setViewFlag(boolean newFlag){
        viewFlag = newFlag;
    }

    public SkyscrapersClue getmodClue(){
        return modClue;
    }
    public void setModClue(SkyscrapersClue newClue){
        modClue = newClue;
    }

    @Override
    public SkyscrapersCell getCell(int x, int y) {
        return (SkyscrapersCell) super.getCell(x, y);
    }

    public int getSize() {
        return this.getWidth();
    }

    @Override
    public PuzzleElement getPuzzleElement(PuzzleElement element) {
        switch (element.getIndex()) {
            case -2:
                return element;
            case -1:
                SkyscrapersLine line = (SkyscrapersLine) element;
                SkyscrapersLine thisLine = null;
                for (SkyscrapersLine l : lines) {
                    if (line.compare(l)) {
                        thisLine = l;
                        break;
                    }
                }
                return thisLine;
            default:
                return super.getPuzzleElement(element);
        }
    }

    /**
     * Called when a {@link PuzzleElement} has been added and passes in the equivalent puzzle element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    @Override
    public void notifyAddition(PuzzleElement puzzleElement) {
        if (puzzleElement instanceof SkyscrapersLine) {
            lines.add((SkyscrapersLine) puzzleElement);
        }
    }

    /**
     * Called when a {@link PuzzleElement} has been deleted and passes in the equivalent puzzle element with the data.
     *
     * @param puzzleElement equivalent puzzle element with the data.
     */
    @Override
    public void notifyDeletion(PuzzleElement puzzleElement) {
        if (puzzleElement instanceof SkyscrapersLine) {
            for (SkyscrapersLine line : lines) {
                if (line.compare((SkyscrapersLine) puzzleElement)) {
                    lines.remove(line);
                    break;
                }
            }
        }
    }

    /**
     * Gets the cells of a certain type directly adjacent to a given cell
     *
     * @param cell at the center,
     * @param type of cell to collect
     * @return list of cells of the given type
     */
    public List<SkyscrapersCell> getAdjacent(SkyscrapersCell cell, SkyscrapersType type) {
        List<SkyscrapersCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        SkyscrapersCell up = getCell(loc.x, loc.y - 1);
        SkyscrapersCell right = getCell(loc.x + 1, loc.y);
        SkyscrapersCell down = getCell(loc.x, loc.y + 1);
        SkyscrapersCell left = getCell(loc.x - 1, loc.y);
        if (up != null && (up.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(up);
        }
        if (right != null && (right.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(right);
        }
        if (down != null && (down.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(down);
        }
        if (left != null && (left.getType() == type || type == SkyscrapersType.ANY)) {
            adj.add(left);
        }
        return adj;
    }

    /**
     * Gets the cells of a certain type directly diagonal to a given cell
     *
     * @param cell at the center,
     * @param type of cell to collect
     * @return list of cells of the given type
     */
    public List<SkyscrapersCell> getDiagonals(SkyscrapersCell cell, SkyscrapersType type) {
        List<SkyscrapersCell> dia = new ArrayList<>();
        Point loc = cell.getLocation();
        SkyscrapersCell upRight = getCell(loc.x + 1, loc.y - 1);
        SkyscrapersCell downRight = getCell(loc.x + 1, loc.y + 1);
        SkyscrapersCell downLeft = getCell(loc.x - 1, loc.y + 1);
        SkyscrapersCell upLeft = getCell(loc.x - 1, loc.y - 1);
        if (upRight != null && (upRight.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(upRight);
        }
        if (downLeft != null && (downLeft.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(downLeft);
        }
        if (downRight != null && (downRight.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(downRight);
        }
        if (upLeft != null && (upLeft.getType() == type || type == SkyscrapersType.ANY)) {
            dia.add(upLeft);
        }
        return dia;
    }

    /**
     * Gets the cells of a certain type in a given row/column
     *
     * @param index: y pos of row or x pos of col,
     *        type of cell to collect,
     *        boolean true if row, false if col
     * @return list of cells of the given type, ordered west to east or north to south
     */
    public List<SkyscrapersCell> getRowCol(int index, SkyscrapersType type, boolean isRow) {
        List<SkyscrapersCell> list = new ArrayList<>();
        for (int i = 0; i < dimension.height; i++) {
            SkyscrapersCell cell;
            if (isRow) {
                cell = getCell(i, index);
            }
            else {
                cell = getCell(index, i);
            }

            if (cell.getType() == type || type == SkyscrapersType.ANY) {
                list.add(cell);
            }
        }
        return list;
    }

    /**
     * Prints a semblance of the board to console (helps in debugging)
     */
    public void printBoard(){
        for(int i =0; i<this.dimension.height; i++){
            for(SkyscrapersCell cell : this.getRowCol(i, SkyscrapersType.ANY,true)){
                if(cell.getType() == SkyscrapersType.Number){
                    System.out.print(cell.getData()+" ");
                }
                else {
                    System.out.print(0+ " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Determines if this board contains the equivalent puzzle elements as the one specified
     *
     * @param board board to check equivalence
     * @return true if the boards are equivalent, false otherwise
     */
    @Override
    public boolean equalsBoard(Board board) {
        SkyscrapersBoard skyscrapersBoard= (SkyscrapersBoard) board;
        for (SkyscrapersLine l1 : lines) {
            boolean hasLine = false;
            for (SkyscrapersLine l2 : skyscrapersBoard.lines) {
                if (l1.compare(l2)) {
                    hasLine = true;
                }
            }
            if (!hasLine) {
                return false;
            }
        }
        return super.equalsBoard(skyscrapersBoard);
    }

    @Override
    public SkyscrapersBoard copy() {
        SkyscrapersBoard copy = new SkyscrapersBoard(dimension.width);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (SkyscrapersLine line : lines) {
            SkyscrapersLine lineCpy = line.copy();
            lineCpy.setModifiable(false);
            copy.getLines().add(lineCpy);
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.eastClues = eastClues;
        copy.southClues = southClues;
        copy.westClues = westClues;
        copy.northClues = northClues;

        copy.dupeFlag=dupeFlag;
        copy.viewFlag=viewFlag;
        return copy;
    }
}
