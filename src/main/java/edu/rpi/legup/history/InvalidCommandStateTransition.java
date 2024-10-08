package edu.rpi.legup.history;

/**
 * The InvalidCommandStateTransition exception is thrown when an invalid state transition is
 * attempted on a PuzzleCommand
 */
public class InvalidCommandStateTransition extends RuntimeException {

    public InvalidCommandStateTransition(
            PuzzleCommand puzzleCommand, CommandState from, CommandState to) {
        super(
                "PuzzleCommand - "
                        + puzzleCommand.getClass().getSimpleName()
                        + " - Attempted invalid command state transition from "
                        + from
                        + " to "
                        + to);
    }
}
