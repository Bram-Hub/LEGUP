package utility;

public interface ICommand
{
    /**
     * Executes an command
     */
    void execute();

    /**
     * Determines whether this command can be executed
     */
    boolean canExecute();

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    String getExecutionError();

    /**
     * Undoes an command
     */
    void undo();

    /**
     * Redoes an command
     */
    void redo();
}