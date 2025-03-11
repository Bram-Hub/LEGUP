package edu.rpi.legup.model;

public enum GoalType {
    NONE, SETCELL, DETERMINECELL;

    public int toValue() { return this.ordinal(); }
}
