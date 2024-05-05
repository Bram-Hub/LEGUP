package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.gameboard.GridRegion;

public class StarBattleRegion extends GridRegion<StarBattleCell>{
    public StarBattleRegion() {
        super();
    }

    public StarBattleRegion copy() {
        StarBattleRegion copy = new StarBattleRegion();
        for (StarBattleCell c: regionCells) {
            copy.addCell(c.copy());
        }
        return copy;
    }

    public int numStars() {
        int stars = 0;
        for (StarBattleCell c: regionCells) {
            if (c.getType() == StarBattleCellType.STAR) {
                ++stars;
            }
        }
        return stars;
    }
}
