package edu.rpi.legup.history;

public abstract class PuzzleCommand implements ICommand
{
    private CommandState state;

    protected PuzzleCommand() {
        this.state = CommandState.CREATED;
    }

    /**
     * Executes an command
     */
    @Override
    public final void execute() {
        if(canExecute()) {
            executeCommand();
            state = CommandState.EXECUTED;
        }
    }

    /**
     * Executes an command
     */
    public abstract void executeCommand();

    /**
     * Undoes an command
     */
    public abstract void undoCommand();

    /**
     * Redoes an command
     */
    public void redoCommand() {
        if(state == CommandState.UNDOED) {
            executeCommand();
            state = CommandState.REDOED;
        }
        else {
            throw new InvalidCommandStateTransition(state, CommandState.REDOED);
        }
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getExecutionError() {
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public final void undo() {
        if(state == CommandState.EXECUTED || state == CommandState.REDOED) {
            undoCommand();
            state = CommandState.UNDOED;
        }
        else {
            throw new InvalidCommandStateTransition(state, CommandState.UNDOED);
        }
    }

    /**
     * Redoes an command
     */
    public final void redo()
    {
        if(state == CommandState.UNDOED) {
            redoCommand();
            state = CommandState.REDOED;
        }
        else {
            throw new InvalidCommandStateTransition(state, CommandState.REDOED);
        }
    }
}
