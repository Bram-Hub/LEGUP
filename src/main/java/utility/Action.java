package utility;

public interface Action
{
    /**
     * Undoes an action on the board
     */
    void undo();

    /**
     * Redoes an action on the board
     */
    void redo();
}
