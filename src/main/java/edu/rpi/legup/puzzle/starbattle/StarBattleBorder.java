package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class StarBattleBorder extends PuzzleElement<Integer> {
    private StarBattleCellType type;

    /**
     * Constructs a StarBattleBorder with the specified border type.
     *
     * @param border_type the type of the border cell
     */
    public StarBattleBorder(StarBattleCellType border_type) {
        super(); // just use the empty constructor since it doesn't hold a value
        type = border_type;
        this.setModifiable(false);
    }

    /**
     * Gets the type of this border.
     *
     * @return the StarBattleCellType representing this border
     */
    public StarBattleCellType getType() {
        return type;
    }

    /**
     * Creates a deep copy of this StarBattleBorder.
     *
     * @return a new StarBattleBorder with the same properties
     */
    public StarBattleBorder copy() {
        StarBattleBorder copy = new StarBattleBorder(type);
        copy.setIndex(index);
        this.setModifiable(false);
        return copy;
    }
}
