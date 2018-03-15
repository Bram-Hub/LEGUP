package puzzles.fillapix;

import model.Puzzle;
import model.gameboard.Board;
import org.xml.sax.SAXException;
import ui.Selection;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class Fillapix extends Puzzle
{

    public Fillapix()
    {

    }

    @Override
    public void initializeBoard()
    {

    }

    @Override
    public Board generatePuzzle(int difficulty)
    {
        return null;
    }

    @Override
    public boolean isPuzzleComplete()
    {
        return false;
    }

    @Override
    public boolean isValidBoardState(Board board)
    {
        return false;
    }

    @Override
    public void onBoardChange(Board board)
    {

    }

    @Override
    public void onTreeSelectionChange(ArrayList<Selection> newSelection)
    {

    }

    @Override
    public void importPuzzle(String fileName) throws IOException, ParserConfigurationException, SAXException
    {

    }
}
