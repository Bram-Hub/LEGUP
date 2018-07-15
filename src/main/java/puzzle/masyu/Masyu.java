package puzzle.masyu;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import save.InvalidFileFormatException;
import ui.boardview.PuzzleElement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            MasyuCell cell = (MasyuCell)currentBoard.getElementData(index);

            cell.setIndex(index);
            element.setData(cell);
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
     * Callback for when the board data changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board)
    {

    }
}
