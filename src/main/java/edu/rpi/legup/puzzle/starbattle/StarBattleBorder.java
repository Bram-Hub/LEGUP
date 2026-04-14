package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class StarBattleBorder extends PuzzleElement<Integer> {
    private StarBattleCellType type;

    public StarBattleBorder(StarBattleCellType border_type) {
        super(); // just use the empty constructor since it doesn't hold a value
        type = border_type;
        this.setModifiable(false);
    }

    public StarBattleCellType getType() {
        return type;
    }

    public StarBattleBorder copy() {
        StarBattleBorder copy = new StarBattleBorder(type);
        copy.setIndex(index);
        this.setModifiable(false);
        return copy;
    }
}
