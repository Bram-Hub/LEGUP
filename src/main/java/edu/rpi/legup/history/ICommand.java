package edu.rpi.legup.history;

public interface ICommand {
    /**
     * Executes a command
     */
    void execute();

    /**
     * Determines whether this command can be executed
     * @return true if can execute, false otherwise
     */
    boolean canExecute();

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    String getError();

    /**
     * Undoes a command
     */
    void undo();

    /**
     * Redoes a command
     */
    void redo();
}