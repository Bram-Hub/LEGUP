package edu.rpi.legup.history;

public class InvalidCommandStateTransition extends RuntimeException {

    public InvalidCommandStateTransition(CommandState from, CommandState to) {
        super("Invalid command state transition from " + from + " to " + to);
    }
}
