package edu.rpi.legup.app;

public class InvalidPuzzleCreationException extends Exception
{
    public InvalidPuzzleCreationException(String errorMessage)
    {
        super(errorMessage);
    }
}
