package utility;

public interface Command
{
    /**
     * Executes an command
     */
    void execute();

    /**
     * Undoes an command
     */
    void undo();

    /**
     * Redoes an command
     */
    void redo();
}