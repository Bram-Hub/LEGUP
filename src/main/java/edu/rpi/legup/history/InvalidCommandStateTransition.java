package edu.rpi.legup.history;

/**
 * The InvalidCommandStateTransition exception is thrown when an invalid state transition
 * is attempted on a PuzzleCommand
 */
public class InvalidCommandStateTransition extends RuntimeException {

    /**
     * Constructs a new InvalidCommandStateTransition exception with a detailed message
     *
     * @param puzzleCommand the PuzzleCommand involved in the invalid transition
     * @param from the state from which the transition was attempted
     * @param to the state to which the transition was attempted
     */
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
