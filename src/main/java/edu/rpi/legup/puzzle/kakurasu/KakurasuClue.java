package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class KakurasuClue extends PuzzleElement<Integer> {
    private KakurasuType type;
    private int clueIndex;

    public KakurasuClue(int value, int clueIndex, KakurasuType type) {
        super(value);
        this.index = -2;
        this.clueIndex = clueIndex;
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

    public int getClueIndex() {
        return clueIndex;
    }

    public void setClueIndex(int clueIndex) {
        this.clueIndex = clueIndex;
    }

    public KakurasuType getType() {
        return type;
    }

    public void setType(KakurasuType type) {
        this.type = type;
    }

    public KakurasuClue copy() {
        return new KakurasuClue(data, clueIndex, type);
    }
}
