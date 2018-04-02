package utility;

public interface Action
{
    /**
     * Undoes an action
     */
    void undo();

    /**
     * Redoes an action
     */
    void redo();
}