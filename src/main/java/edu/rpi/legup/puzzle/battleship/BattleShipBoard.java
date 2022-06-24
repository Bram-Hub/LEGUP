package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleShipBoard extends GridBoard {

    /**
     * The column of clues on the east side of the board.
     */
    private List<BattleShipClue> east;

    /**
     * The column of clues on the south side of the board.
     */
    private List<BattleShipClue> south;

    /**
     * Constructor for creating a rectangular battleship board.
     *
     * @param width  width of the board
     * @param height height of the board
     */
    public BattleShipBoard(int width, int height) {
        super(width, height);

        this.east = new ArrayList<>();
        this.south = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            east.add(null);
        }
        for (int i = 0; i < width; i++) {
            south.add(null);
        }
    }

    /**
     * Constructor for creating a square-sized battleship board.
     *
     * @param size size of the board
     */
    public BattleShipBoard(int size) {
        this(size, size);
    }

    /**
     * Gets the east {@link BattleShipClue}
     *
     * @return  List of <code>BattleShipClue</code> objects on the east
     *          side of the board
     */
    public List<BattleShipClue> getEast() {
        return east;
    }

    /**
     * Gets the east {@link BattleShipClue}
     *
     * @return east battle ship clues
     */
    public List<BattleShipClue> getSouth() {
        return south;
    }

    @Override
    public BattleShipCell getCell(int x, int y) {
        return (BattleShipCell) super.getCell(x, y);
    }

    @Override
    public BattleShipBoard copy() {
        BattleShipBoard copy = new BattleShipBoard(dimension.width,
                dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.east = this.east;
        copy.south = this.south;
        return copy;
    }

    /**
     * Get a list of all orthogonally adjacent cells.
     * 
     * @param cell  The cell to get adjacent cells from.
     * @return      List of adjacent cells in clockwise order:
     *              <code>{ up, right, down, left }</code>
     */
    public List<BattleShipCell> getAdjacent(@NotNull BattleShipCell cell) {
        List<BattleShipCell> adj = new ArrayList<>();
        Point loc = cell.getLocation();
        BattleShipCell up = getCell(loc.x, loc.y - 1);
        BattleShipCell right = getCell(loc.x + 1, loc.y);
        BattleShipCell down = getCell(loc.x, loc.y + 1);
        BattleShipCell left = getCell(loc.x - 1, loc.y);
        adj.add(up);
        adj.add(right);
        adj.add(down);
        adj.add(left);
        return adj;
    }

    /**
     * Get a list of all diagonally adjacent cells.
     *
     * @param cell  The cell to get diagonally adjacent cells from.
     * @return      List of diagonally adjacent cells in clockwise order:
     *              <code>{ upRight, downRight, downLeft, upLeft }</code>
     */
    public List<BattleShipCell> getAdjDiagonals(@NotNull BattleShipCell cell) {
        List<BattleShipCell> dia = new ArrayList<>();
        Point loc = cell.getLocation();
        BattleShipCell upRight = getCell(loc.x + 1, loc.y - 1);
        BattleShipCell downRight = getCell(loc.x + 1, loc.y + 1);
        BattleShipCell downLeft = getCell(loc.x - 1, loc.y + 1);
        BattleShipCell upLeft = getCell(loc.x - 1, loc.y - 1);
        dia.add(upRight);
        dia.add(downRight);
        dia.add(downLeft);
        dia.add(upLeft);
        return dia;
    }

    /**
     * Get a list of cells in a row.
     *
     * @param y The y-coordinate of the row.
     * @return  List of cells in the row in increasing x-coordinate order.
     */
    public List<BattleShipCell> getRow(int y) {
        List<BattleShipCell> row = new ArrayList<>();
        for (int x = 0; x < dimension.width; x++) {
            row.add(getCell(x, y));
        }
        return row;
    }

    /**
     * Get a list of cells in a column.
     *
     * @param x The x-coordinate of the column.
     * @return  List of cells in the column in increasing y-coordinate order.
     */
    public List<BattleShipCell> getColumn(int x) {
        List<BattleShipCell> column = new ArrayList<>();
        for (int y = 0; y < dimension.height; y++) {
            column.add(getCell(x, y));
        }
        return column;
    }
}