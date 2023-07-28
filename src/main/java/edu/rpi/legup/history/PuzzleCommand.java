package edu.rpi.legup.history;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PuzzleCommand implements ICommand {
    private CommandState state;
    private boolean isCached;
    private String cachedError;

    /**
     * Puzzle Command Constructor for creating an undoable and redoable change to the model.
     */
    protected PuzzleCommand() {
        this.state = CommandState.CREATED;
        this.isCached = false;
        this.cachedError = null;
    }

    /**
     * Executes an command
     */
    @Override
    public final void execute() {
        if (canExecute()) {
            executeCommand();
            state = CommandState.EXECUTED;
        }
    }

    /**
     * Executes an command
     */
    @Override
    public final void execute(TreeTransition thisTreeTransition) {
        if (canExecute()) {
            executeCommand(thisTreeTransition);
            state = CommandState.EXECUTED;
        }
    }    

    /**
     * Determines whether this command can be executed
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
     * otherwise null if command can be executed
     */
    @Override
    public final String getError() {
        if (isCached) {
            return cachedError;
        }
        else {
            return getErrorString();
        }
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    public abstract String getErrorString();

    /**
     * Executes an command
     */
    public abstract void executeCommand();
    /**
     * Executes an command
     */
    public abstract void executeCommand(TreeTransition thisTreeTransition);

    /**
     * Undoes an command
     */
    public abstract void undoCommand();

    /**
     * Redoes an command
     */
    public void redoCommand() {
        if (state == CommandState.UNDOED) {
            executeCommand();
            state = CommandState.REDOED;
        }
        else {
            throw new InvalidCommandStateTransition(this, state, CommandState.REDOED);
        }
    }

    /**
     * Undoes an command
     */
    @Override
    public final void undo() {
        if (state == CommandState.EXECUTED || state == CommandState.REDOED) {
            undoCommand();
            state = CommandState.UNDOED;
        }
        else {
            throw new InvalidCommandStateTransition(this, state, CommandState.UNDOED);
        }
    }

    /**
     * Redoes an command
     */
    public final void redo() {
        if (state == CommandState.UNDOED) {
            redoCommand();
            state = CommandState.REDOED;
        }
        else {
            throw new InvalidCommandStateTransition(this, state, CommandState.REDOED);
        }
    }
}