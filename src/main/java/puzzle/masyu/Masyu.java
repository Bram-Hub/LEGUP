package puzzle.masyu;

import model.Puzzle;
import model.gameboard.Board;
import ui.boardview.ElementView;

public class Masyu extends Puzzle
{

    public Masyu()
    {
        super();

        this.name=  "Masyu";

        this.importer = new MasyuImporter(this);
        this.exporter = new MasyuExporter(this);

        this.factory = new MasyuCellFactory();
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        boardView = new MasyuView(((MasyuBoard)currentBoard).getDimension());
        for(ElementView element: boardView.getElementViews())
        {
            int index = element.getIndex();
            MasyuCell cell = (MasyuCell)currentBoard.getElementData(index);

            cell.setIndex(index);
            element.setElement(cell);
        }
    }

    /**
     * Generates a random puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     *
     * @return board of the random puzzle
     */
    @Override
    public Board generatePuzzle(int difficulty)
    {
        return null;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     *
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board)
    {
        return false;
    }

    /**
     * Callback for when the board element changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board)
    {

    }
}
