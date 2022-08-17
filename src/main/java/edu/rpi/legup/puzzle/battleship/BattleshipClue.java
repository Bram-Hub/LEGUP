package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class BattleshipClue extends PuzzleElement {

    private BattleshipType type;

    public BattleshipClue(int value, int index, BattleshipType type) {
        super(value);
        this.index = index;
        this.type = type;
    }

    /**
     * Returns the column number as a string
     * @param col the column number that is to be converted and returned
     * @return int value
     */
    public static String colNumToString(int col) {
        final StringBuilder sb = new StringBuilder();
        col--;
        while (col >= 0) {
            int numChar = (col % 26) + 65;
            sb.append((char) numChar);
            col = (col / 26) - 1;
        }
        return sb.reverse().toString();
    }

    /**
     * Returns the column string as an integer
     * @param col the column number as a string that is to be converted and returned
     * @return string value
     */
    public static int colStringToColNum(String col) {
        int result = 0;
        for (int i = 0; i < col.length(); i++) {
            result *= 26;
            result += col.charAt(i) - 'A' + 1;
        }
        return result;
    }

    /**
     * Gets the int value that represents this puzzleElement
     *
     * @return int value
     */
    @Override
    public Integer getData() {
        return (Integer) super.getData();
    }

    /**
     * Returns the type of the battleship object (ship or clue)
     * @return BattleshipType type
     */
    public BattleshipType getType() {
        return type;
    }

    /**
     * Sets the type of the battleship object (ship or clue) to the given type
     * @param type given Battleship type
     */
    public void setType(BattleshipType type) {
        this.type = type;
    }

    public BattleshipClue copy() {
        return null;
    }
}
