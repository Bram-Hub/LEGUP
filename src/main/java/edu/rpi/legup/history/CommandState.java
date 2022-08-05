package edu.rpi.legup.history;

public enum CommandState {
    CREATED("Created"), EXECUTED("Executed"), UNDOED("Undoed"), REDOED("Redoed");

    private String value;

    CommandState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
