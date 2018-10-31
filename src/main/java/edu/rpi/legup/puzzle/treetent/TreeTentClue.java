package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class TreeTentClue extends PuzzleElement<Integer> {
    private TreeTentType type;
    private int clueIndex;

    public TreeTentClue(int value, int clueIndex, TreeTentType type) {
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

    public TreeTentType getType() {
        return type;
    }

    public void setType(TreeTentType type) {
        this.type = type;
    }

    public TreeTentClue copy() {
        return null;
    }
}
