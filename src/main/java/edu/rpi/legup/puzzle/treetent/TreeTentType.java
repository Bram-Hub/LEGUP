package edu.rpi.legup.puzzle.treetent;

public enum TreeTentType {
    UNKNOWN(0), TREE(1), GRASS(2), TENT(3), CLUE_NORTH(-1), CLUE_EAST(-2), CLUE_SOUTH(-3), CLUE_WEST(-4);

    public int value;

    TreeTentType(int value) {
        this.value = value;
    }
}