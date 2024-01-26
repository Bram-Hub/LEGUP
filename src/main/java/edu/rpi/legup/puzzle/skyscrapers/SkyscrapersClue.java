package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class SkyscrapersClue extends PuzzleElement<Integer> {
    private SkyscrapersType type;
    private int clueIndex;

    public SkyscrapersClue(int value, int clueIndex, SkyscrapersType type) {
        super(value);
        this.index = -2;
        this.clueIndex = clueIndex;//index in list
        this.type = type;
        this.setModifiable(false);
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

    public int getClueIndex() {
        return clueIndex;
    }

    public SkyscrapersType getType() {
        return type;
    }

    public void setType(SkyscrapersType type) {
        this.type = type;
    }

    public SkyscrapersClue copy() {
        return null;
    }
}
