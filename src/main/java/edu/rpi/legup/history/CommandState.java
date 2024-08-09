package edu.rpi.legup.history;

/**
 * The CommandState enum represents the various states that a command can be in during its lifecycle.
 * Each state is associated with a descriptive name.
 */
public enum CommandState {
    CREATED("Created"),
    EXECUTED("Executed"),
    UNDOED("Undoed"),
    REDOED("Redoed");

    private String value;

    /**
     * Constructs a CommandState with the specified state name
     *
     * @param value The name associated with the command state
     */
    CommandState(String value) {
        this.value = value;
    }

    /**
     * Returns the name associated with this CommandState
     *
     * @return The state name
     */
    @Override
    public String toString() {
        return value;
    }
}
