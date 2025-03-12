package edu.rpi.legup.model;

public enum GoalType {
    NONE, SETCELL, DETERMINECELL;
    // Prove cell value as indicated, prove cell value not necessarily as indicated
    // Figure out cell value must be value, figure out cell can be different values
    // In future, multiple cell goals?

    public int toValue() { return this.ordinal(); }
}
