package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.util.ArrayList;
import java.util.List;

public class BattleshipBoard extends GridBoard {

    private List<BattleshipClue> east;
    private List<BattleshipClue> south;

    /**
     * BattleShipBoard Constructor creates a new battleship board from the specified width and height
     *
     * @param width  width of the board
     * @param height height of the board
     */
    public BattleshipBoard(int width, int height) {
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
     * BattleShipBoard Constructor creates a new battleship board which is square from the specified size
     *
     * @param size size of the board
     */
    public BattleshipBoard(int size) {
        this(size, size);
    }

    /**
     * Gets the east {@link BattleshipClue}
     *
     * @return east battle ship clues
     */
    public List<BattleshipClue> getEast() {
        return east;
    }

    /**
     * Gets the east {@link BattleshipClue}
     *
     * @return east battle ship clues
     */
    public List<BattleshipClue> getSouth() {
        return south;
    }

    @Override
    public BattleshipCell getCell(int x, int y) {
        return (BattleshipCell) super.getCell(x, y);
    }

    @Override
    public BattleshipBoard copy() {
        BattleshipBoard copy = new BattleshipBoard(dimension.width, dimension.height);
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
}