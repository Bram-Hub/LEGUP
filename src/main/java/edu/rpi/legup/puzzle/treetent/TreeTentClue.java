package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class TreeTentClue extends PuzzleElement<Integer> {
    private TreeTentType type;
    private int clueIndex;

    /**
     * Constructs a TreeTentClue with the specified value, index, and type.
     *
     * @param value the integer value of the clue
     * @param clueIndex the index identifying the position of the clue
     * @param type the TreeTentType associated with this clue
     */
    public TreeTentClue(int value, int clueIndex, TreeTentType type) {
        super(value);
        this.index = -2;
        this.clueIndex = clueIndex;
        this.type = type;
    }

    /**
     * Converts a column number to its corresponding spreadsheet-style string representation.
     *
     * @param col the column number (1-based)
     * @return the column label as a string (e.g., 1 -> "A", 27 -> "AA")
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
     * Converts a spreadsheet-style column string to its corresponding column number.
     *
     * @param col the column label as a string (e.g., "A", "AA")
     * @return the corresponding column number (1-based)
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
     * Gets the index of this clue.
     *
     * @return the clue index
     */
    public int getClueIndex() {
        return clueIndex;
    }

    /**
     * Sets the index of this clue.
     *
     * @param clueIndex the new clue index
     */
    public void setClueIndex(int clueIndex) {
        this.clueIndex = clueIndex;
    }

    /**
     * Gets the type of this clue.
     *
     * @return the TreeTentType of this clue
     */
    public TreeTentType getType() {
        return type;
    }

    /**
     * Sets the type of this clue.
     *
     * @param type the new TreeTentType
     */
    public void setType(TreeTentType type) {
        this.type = type;
    }

    /**
     * Creates a deep copy of this TreeTentClue.
     *
     * @return a new TreeTentClue with the same data, index, and type
     */
    public TreeTentClue copy() {
        return new TreeTentClue(data, clueIndex, type);
    }
}
