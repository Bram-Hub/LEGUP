package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkyscrapersBoard extends GridBoard {

    private ArrayList<SkyscrapersLine> lines;

    private ArrayList<SkyscrapersClue> rowClues;
    private ArrayList<SkyscrapersClue> colClues;
    private ArrayList<SkyscrapersClue> row;
    private ArrayList<SkyscrapersClue> col;

    public SkyscrapersBoard(int width, int height) {
        super(width, height);

        this.lines = new ArrayList<>();

        this.rowClues = new ArrayList<>();
        this.colClues = new ArrayList<>();
        this.row = new ArrayList<>();
        this.col = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            rowClues.add(null);
        }
        for (int i = 0; i < width; i++) {
            colClues.add(null);
        }
        for (int i = 0; i < height; i++) {
            row.add(null);
        }
        for (int i = 0; i < width; i++) {
            col.add(null);
        }
    }

    public SkyscrapersBoard(int size) {
        this(size, size);
    }

    public ArrayList<SkyscrapersLine> getLines() {
        return lines;
    }

    public ArrayList<SkyscrapersClue> getRowClues() { //EAST CLUE
        return rowClues;
    }

    public ArrayList<SkyscrapersClue> getColClues() { //SOUTH CLUE
        return colClues;
    }
    
    public ArrayList<SkyscrapersClue> getRow() { //WEST CLUE
        return row;
    }

    public ArrayList<SkyscrapersClue> getCol() { //NORTH CLUE
        return col;
    }

    @Override
    public SkyscrapersCell getCell(int x, int y) {
        return (SkyscrapersCell) super.getCell(x, y);
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
        if(puzzleElement instanceof SkyscrapersLine) {
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
        if(puzzleElement instanceof SkyscrapersLine) {
            for(SkyscrapersLine line : lines) {
                if(line.compare((SkyscrapersLine)puzzleElement)) {
                    lines.remove(line);
                    break;
                }
            }
        }
    }

    public List<SkyscrapersCell> getAdjacent(SkyscrapersCell cell, SkyscrapersType type) {
        List<SkyscrapersCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        SkyscrapersCell up = getCell(loc.x, loc.y - 1);
        SkyscrapersCell right = getCell(loc.x + 1, loc.y);
        SkyscrapersCell down = getCell(loc.x, loc.y + 1);
        SkyscrapersCell left = getCell(loc.x - 1, loc.y);
        if (up != null && up.getType() == type) {
            adj.add(up);
        }
        if (right != null && right.getType() == type) {
            adj.add(right);
        }
        if (down != null && down.getType() == type) {
            adj.add(down);
        }
        if (left != null && left.getType() == type) {
            adj.add(left);
        }
        return adj;
    }

    public List<SkyscrapersCell> getDiagonals(SkyscrapersCell cell, SkyscrapersType type) {
        List<SkyscrapersCell> dia = new ArrayList<>();
        Point loc = cell.getLocation();
        SkyscrapersCell upRight = getCell(loc.x + 1, loc.y - 1);
        SkyscrapersCell downRight = getCell(loc.x + 1, loc.y + 1);
        SkyscrapersCell downLeft = getCell(loc.x - 1, loc.y + 1);
        SkyscrapersCell upLeft = getCell(loc.x - 1, loc.y - 1);
        if (upRight != null && upRight.getType() == type) {
            dia.add(upRight);
        }
        if (downLeft != null && downLeft.getType() == type) {
            dia.add(downLeft);
        }
        if (downRight != null && downRight.getType() == type) {
            dia.add(downRight);
        }
        if (upLeft != null && upLeft.getType() == type) {
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
        if (isRow) {
            for (int i = 0; i < dimension.height; i++) {
            	SkyscrapersCell cell = getCell(i, index);
                if (cell.getType() == type) {
                    list.add(cell);
                }
            }
        } else {
            for (int i = 0; i < dimension.width; i++) {
            	SkyscrapersCell cell = getCell(index, i);
                if (cell.getType() == type) {
                    list.add(cell);
                }
            }
        }
        return list;
    }

    /**
     * Determines if this board contains the equivalent puzzle elements as the one specified
     *
     * @param board board to check equivalence
     * @return true if the boards are equivalent, false otherwise
     */
    @Override
    public boolean equalsBoard(Board board) {
    	SkyscrapersBoard treeTentBoard = (SkyscrapersBoard) board;
        for (SkyscrapersLine l1 : lines) {
            boolean hasLine = false;
            for (SkyscrapersLine l2 : treeTentBoard.lines) {
                if (l1.compare(l2)) {
                    hasLine = true;
                }
            }
            if (!hasLine) {
                return false;
            }
        }
        return super.equalsBoard(treeTentBoard);
    }

    @Override
    public SkyscrapersBoard copy() {
    	SkyscrapersBoard copy = new SkyscrapersBoard(dimension.width, dimension.height);
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
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.rowClues = rowClues;
        copy.colClues = colClues;
        copy.row = row;
        copy.col = col;
        return copy;
    }
}
