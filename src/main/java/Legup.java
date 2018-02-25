import app.GameBoardFacade;
import puzzles.sudoku.Sudoku;

public class Legup
{

    /**
     * Starts the Legup Program
     *
     * @param args arguments to Legup
     */
    public static void main(String[] args) throws Exception
    {
        GameBoardFacade.getInstance();
        GameBoardFacade.getInstance().setPuzzle(new Sudoku());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        Thread.sleep(100);
        GameBoardFacade.getInstance().getLegupUI().getBoardView().zoomFit();
    }
}
