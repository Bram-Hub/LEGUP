package save;

public class InvalidFileFormatException extends Exception
{

    public InvalidFileFormatException(String message)
    {
        super("InvalidFileFormatException: " + message);
    }

}
