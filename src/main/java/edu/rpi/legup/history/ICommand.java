package edu.rpi.legup.history;

/**
 * The ICommand interface defines the structure for command objects in a command pattern.
 * It provides methods to execute, undo, redo commands, and to check if a command can be executed.
 */
public interface ICommand {
    /**
     * Executes the command. The specific behavior depends on the implementation
     */
    void execute();

    /**
     * Determines whether this command can be executed
     *
     * @return true if can execute, false otherwise
     */
    boolean canExecute();

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     *     otherwise null if command can be executed
     */
    String getError();

    /**
     * Undoes the command. Reverts the changes made by the execute method
     */
    void undo();

    /**
     * Redoes the command. Re-applies the changes made by the execute method after undoing
     */
    void redo();
}
