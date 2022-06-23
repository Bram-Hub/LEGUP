package edu.rpi.legup.puzzle.battleship;

public enum BattleshipType {
    UNKNOWN, WATER, SHIP_SEGMENT_UNKNOWN, SHIP_SIZE_1, SHIP_SEGMENT_TOP,
    SHIP_SEGMENT_RIGHT, SHIP_SEGMENT_BOTTOM, SHIP_SEGMENT_LEFT,
    SHIP_SEGMENT_MIDDLE, CLUE_NORTH, CLUE_EAST, CLUE_SOUTH, CLUE_WEST;

    public int value;

    BattleshipType() {
        this.value = this.ordinal();
    }

    /**
     * Gets the enum of this BattleShipType
     *
     * @return enum equivalent BattleShipType of integer value
     */
    public static BattleshipType getType(int value) {
        BattleshipType[] vals = values();
        if (value >= 0 && value < vals.length) {
            return vals[value];
        }
        return null;
    }
}
