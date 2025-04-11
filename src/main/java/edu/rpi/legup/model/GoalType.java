package edu.rpi.legup.model;

public enum GoalType {
    DEFAULT, PROVE_CELL_MUST_BE, PROVE_CELL_MIGHT_NOT_BE,
    PROVE_SINGLE_CELL_VALUE, PROVE_MULTIPLE_CELL_VALUE;
    // Prove cell value as indicated, prove cell value not necessarily as indicated
    // Figure out cell value must be value, figure out cell can be different values
    // In the future, multiple cell goals?
    // Solve the puzzle aka find one solution
    // Default: find all solutions

    public int toValue() { return this.ordinal(); }
}
