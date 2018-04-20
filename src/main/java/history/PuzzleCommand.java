package history;

public abstract class PuzzleCommand implements ICommand
{
    protected CommandState state;

    public PuzzleCommand()
    {
        this.state = CommandState.CREATED;
    }

    /**
     * Executes an command
     */
    public void execute()
    {
        if(!canExecute())
        {
            return;
        }
        state = CommandState.EXECUTED;
    }

    /**
     * Undoes an command
     */
    public void undo()
    {
        if(state == CommandState.CREATED || state == CommandState.UNDOED)
        {
            return;
        }
        state = CommandState.UNDOED;
    }

    /**
     * Redoes an command
     */
    public void redo()
    {
        if(state != CommandState.UNDOED)
        {
            return;
        }
        execute();
        state = CommandState.REDOED;
    }
}
