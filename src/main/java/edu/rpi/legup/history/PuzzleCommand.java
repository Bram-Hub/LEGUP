package edu.rpi.legup.history;

public abstract class PuzzleCommand implements ICommand {
    private CommandState state;
    private boolean isCached;
    private String cachedError;

    /** Puzzle Command Constructor for creating an undoable and redoable change to the model. */
    protected PuzzleCommand() {
        this.state = CommandState.CREATED;
        this.isCached = false;
        this.cachedError = null;
    }

    /** Executes an command */
    @Override
    public final void execute() {
        if (canExecute()) {
            executeCommand();
            state = CommandState.EXECUTED;
        }
    }

    /** Determines whether this command can be executed */
    @Override
    public final boolean canExecute() {
        cachedError = getError();
        isCached = true;
        return cachedError == null;
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     *     otherwise null if command can be executed
     */
    @Override
    public final String getError() {
        if (isCached) {
            return cachedError;
        } else {
            return getErrorString();
        }
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     *     otherwise null if command can be executed
     */
    public abstract String getErrorString();

    /** Executes a command */
    public abstract void executeCommand();

    /** Undoes a command */
    public abstract void undoCommand();

    /** Redoes a command */
    public void redoCommand() {
        if (state == CommandState.UNDOED) {
            executeCommand();
            state = CommandState.REDOED;
        } else {
            throw new InvalidCommandStateTransition(this, state, CommandState.REDOED);
        }
    }

    /** Undoes a command */
    @Override
    public final void undo() {
        if (state == CommandState.EXECUTED || state == CommandState.REDOED) {
            undoCommand();
            state = CommandState.UNDOED;
        } else {
            throw new InvalidCommandStateTransition(this, state, CommandState.UNDOED);
        }
    }

    /** Redoes a command */
    public final void redo() {
        if (state == CommandState.UNDOED) {
            redoCommand();
            state = CommandState.REDOED;
        } else {
            throw new InvalidCommandStateTransition(this, state, CommandState.REDOED);
        }
    }
}
