package edu.rpi.legup.history;

/**
 * The CommandState enum represents the various states that a command can be in during its
 * lifecycle. Each state is associated with a descriptive name.
 */
public enum CommandState {
    CREATED("Created"),
    EXECUTED("Executed"),
    UNDOED("Undoed"),
    REDOED("Redoed");

    private String value;

    CommandState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
