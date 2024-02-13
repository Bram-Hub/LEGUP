package edu.rpi.legup.history;

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
