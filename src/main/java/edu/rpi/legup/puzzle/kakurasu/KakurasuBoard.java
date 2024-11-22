package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KakurasuBoard extends GridBoard {

    private ArrayList<KakurasuClue> rowClues;
    private ArrayList<KakurasuClue> colClues;

    public KakurasuBoard(int width, int height) {
        super(width, height);

        this.rowClues = new ArrayList<>();
        this.colClues = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            rowClues.add(null);
        }
        for (int i = 0; i < width; i++) {
            colClues.add(null);
        }
    }

    public KakurasuBoard(int size) {
        this(size, size);
    }

    public ArrayList<KakurasuClue> getRowClues() {
        return rowClues;
    }

    public ArrayList<KakurasuClue> getColClues() {
        return colClues;
    }

    @Override
    public KakurasuCell getCell(int x, int y) {
        return (KakurasuCell) super.getCell(x, y);
    }

    @Override
    public PuzzleElement getPuzzleElement(PuzzleElement element) {
        return switch (element.getIndex()) {
            case -2, -1 -> element;
            default -> super.getPuzzleElement(element);
        };
    }

    @Override
    public void setPuzzleElement(int index, PuzzleElement puzzleElement) {
        if (index < puzzleElements.size()) {
            puzzleElements.set(index, puzzleElement);
        }
    }

    @Override
    public void notifyChange(PuzzleElement puzzleElement) {
        int index = puzzleElement.getIndex();
        if (index < puzzleElements.size()) {
            puzzleElements.set(index, puzzleElement);
        }
    }

    public KakurasuClue getClue(int x, int y) {
        if (x == getWidth() && 0 <= y && y < getHeight()) {
            return rowClues.get(y);
        } else if (y == getHeight() && 0 <= x && x < getWidth()) {
            return colClues.get(x);
        }
        return null;
    }

    /**
     * Get a list of all orthogonally adjacent cells.
     *
     * @param cell The cell to get adjacent cells from.
     * @param type The cell types to get.
     * @return List of adjacent cells in the form { up, right, down, left }. If an adjacent cell is
     *     null, it will not be added to the list.
     */
    public List<KakurasuCell> getAdjacent(KakurasuCell cell, KakurasuType type) {
        List<KakurasuCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        for (int i = -2; i < 2; i++) {
            KakurasuCell adjCell = getCell(loc.x + (i % 2), loc.y + ((i + 1) % 2));
            if (adjCell != null && adjCell.getType() == type) {
                adj.add(adjCell);
            }
        }
        return adj;
    }

    /**
     * Gets all cells of a specified type that are diagonals of a specified cell
     *
     * @param cell the base cell
     * @param type the type to look for
     * @return a list of TreeTentCells that are diagonals of the given KakurasuCell and are of the
     *     given KakurasuType
     */
    public List<KakurasuCell> getDiagonals(KakurasuCell cell, KakurasuType type) {
        List<KakurasuCell> dia = new ArrayList<>();
        Point loc = cell.getLocation();
        KakurasuCell upRight = getCell(loc.x + 1, loc.y - 1);
        KakurasuCell downRight = getCell(loc.x + 1, loc.y + 1);
        KakurasuCell downLeft = getCell(loc.x - 1, loc.y + 1);
        KakurasuCell upLeft = getCell(loc.x - 1, loc.y - 1);
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
     * Creates and returns a list of TreeTentCells that match the given KakurasuType
     *
     * @param index the row or column number
     * @param type type of Kakurasu element
     * @param isRow boolean value based on whether a row of column is being checked
     * @return List of TreeTentCells that match the given KakurasuType
     */
    public List<KakurasuCell> getRowCol(int index, KakurasuType type, boolean isRow) {
        List<KakurasuCell> list = new ArrayList<>();
        if (isRow) {
            for (int i = 0; i < dimension.width; i++) {
                KakurasuCell cell = getCell(i, index);
                if (cell.getType() == type) {
                    list.add(cell);
                }
            }
        } else {
            for (int i = 0; i < dimension.height; i++) {
                KakurasuCell cell = getCell(index, i);
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
        KakurasuBoard kakurasuBoard = (KakurasuBoard) board;
        return super.equalsBoard(kakurasuBoard);
    }

    /**
     * Performs a deep copy of the KakurasuBoard
     *
     * @return a KakurasuBoard object that is a deep copy of the current KakurasuBoard
     */
    @Override
    public KakurasuBoard copy() {
        KakurasuBoard copy = new KakurasuBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.rowClues = rowClues;
        copy.colClues = colClues;
        return copy;
    }
}
