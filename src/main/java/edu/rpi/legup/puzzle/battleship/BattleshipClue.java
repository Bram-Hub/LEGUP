package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class BattleshipClue extends PuzzleElement {

    private BattleshipType type;

    public BattleshipClue(int value, int index, BattleshipType type) {
        super(value);
        this.index = index;
        this.type = type;
    }

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

    public BattleshipType getType() {
        return type;
    }

    public void setType(BattleshipType type) {
        this.type = type;
    }

    public BattleshipClue copy() {
        return null;
    }
}
