package utility;

public abstract class PuzzleCommand implements ICommand
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
