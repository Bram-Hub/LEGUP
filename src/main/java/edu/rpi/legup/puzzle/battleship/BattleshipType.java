package edu.rpi.legup.puzzle.battleship;

public enum BattleshipType {
    UNKNOWN, WATER, SUBMARINE, SHIP_UNKNOWN,
    SHIP_TOP, SHIP_RIGHT, SHIP_BOTTOM, SHIP_LEFT, SHIP_MIDDLE,
    CLUE_NORTH, CLUE_EAST, CLUE_SOUTH, CLUE_WEST;

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

    /**
     * Checks if the type is a ship.
     *
     * @param type the {@link BattleshipType} to check
     * @return <tt>true</tt> if the type is a ship, <tt>false</tt> otherwise
     */
    public static boolean isShip(BattleshipType type) {
        return type == SHIP_UNKNOWN || type == SHIP_TOP || type == SHIP_RIGHT
                || type == SHIP_BOTTOM || type == SHIP_LEFT || type == SHIP_MIDDLE;
    }
}
