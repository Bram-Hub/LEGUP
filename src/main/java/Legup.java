import app.GameBoardFacade;
import puzzles.sudoku.Sudoku;

public class Legup
{

    /**
     * Starts the Legup Program
     *
     * @param args arguments to Legup
     */
    public static void main(String[] args)
    {
        GameBoardFacade.getInstance();
        GameBoardFacade.getInstance().setPuzzle(new Sudoku());
    }
}
