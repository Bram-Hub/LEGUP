package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.util.ArrayList;
import java.util.List;

public class BattleShipBoard extends GridBoard {

    private List<BattleShipClue> east;
    private List<BattleShipClue> south;

    /**
     * BattleShipBoard Constructor creates a new battleship board from the specified width and height
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
     * BattleShipBoard Constructor creates a new battleship board which is square from the specified size
     *
     * @param size size of the board
     */
    public BattleShipBoard(int size) {
        this(size, size);
    }

    /**
     * Gets the east {@link BattleShipClue}
     *
     * @return east battle ship clues
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
        BattleShipBoard copy = new BattleShipBoard(dimension.width, dimension.height);
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