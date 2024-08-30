package edu.rpi.legup.history;

/**
 * The PuzzleCommand class is an abstract base class for commands that can be executed, undone, and redone
 * within the puzzle model. It implements the ICommand interface and maintains the state and error handling
 * for the command.
 */
public abstract class PuzzleCommand implements ICommand {
    private CommandState state;
    private boolean isCached;
    private String cachedError;

    /**
     * Puzzle Command Constructor for creating an undoable and redoable change to the model
     */
    protected PuzzleCommand() {
        this.state = CommandState.CREATED;
        this.isCached = false;
        this.cachedError = null;
    }

    /**
     * Executes the command if it can be executed
     */
    @Override
    public final void execute() {
        if (canExecute()) {
            executeCommand();
            state = CommandState.EXECUTED;
        }
    }

    /**
     * Determines whether the command can be executed by checking the error state
     */
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

    /**
     * Executes the command.
     * This method must be implemented by subclasses to define the command's execution behavior.
     */
    public abstract void executeCommand();

    /**
     * Undoes the command.
     * This method must be implemented by subclasses to define the command's undo behavior.
     */
    public abstract void undoCommand();

    /**
     * Redoes the command. This method is called if the command was previously undone.
     */
    public void redoCommand() {
        if (state == CommandState.UNDOED) {
            executeCommand();
            state = CommandState.REDOED;
        } else {
            throw new InvalidCommandStateTransition(this, state, CommandState.REDOED);
        }
    }

    /**
     * Undoes the command if it was executed or redone
     */
    @Override
    public final void undo() {
        if (state == CommandState.EXECUTED || state == CommandState.REDOED) {
            undoCommand();
            state = CommandState.UNDOED;
        } else {
            throw new InvalidCommandStateTransition(this, state, CommandState.UNDOED);
        }
    }

    /**
     * Redoes the command if it was previously undone.
     */
    public final void redo() {
        if (state == CommandState.UNDOED) {
            redoCommand();
            state = CommandState.REDOED;
        } else {
            throw new InvalidCommandStateTransition(this, state, CommandState.REDOED);
        }
    }
}
