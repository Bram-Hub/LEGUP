package utility;

public abstract class PuzzleCommand implements Command
{
    /**
     * Redoes an command
     */
    @Override
    public void redo()
    {
        execute();
    }
}
