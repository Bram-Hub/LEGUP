package edu.rpi.legup.model;

public enum GoalType {
    DEFAULT,
    PROVE_CELL_MUST_BE,
    PROVE_CELL_MIGHT_NOT_BE,
    PROVE_SINGLE_CELL_VALUE,
    PROVE_MULTIPLE_CELL_VALUE,
    PROVE_ANY_SOLUTION,
    PROVE_NO_SOLUTION,
    PROVE_VALUES_ARE_POSSIBLE,
    PROVE_VALUES_ARE_IMPOSSIBLE;

    // See main/java/edu.rpi.legup/model/GoalNotes.md for a table of what these all mean

    public int toValue() {
        return this.ordinal();
    }
}
