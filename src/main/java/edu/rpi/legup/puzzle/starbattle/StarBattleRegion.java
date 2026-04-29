package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.gameboard.GridRegion;

public class StarBattleRegion extends GridRegion<StarBattleCell> {

    /** Constructs an empty StarBattleRegion. */
    public StarBattleRegion() {
        super();
    }

    /**
     * Creates a deep copy of this StarBattleRegion, including copies of all contained cells.
     *
     * @return a new StarBattleRegion with copied cells
     */
    public StarBattleRegion copy() {
        StarBattleRegion copy = new StarBattleRegion();
        for (StarBattleCell c : regionCells) {
            copy.addCell(c.copy());
        }
        return copy;
    }

    /**
     * Counts the number of star cells within this region.
     *
     * @return the number of cells of type STAR in this region
     */
    public int numStars() {
        int stars = 0;
        for (StarBattleCell c : regionCells) {
            if (c.getType() == StarBattleCellType.STAR) {
                ++stars;
            }
        }
        return stars;
    }
}
