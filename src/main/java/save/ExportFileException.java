package save;

public class ExportFileException extends Exception
{

    public ExportFileException(String message)
    {
        super("Export File Exception: " + message);
    }

}
