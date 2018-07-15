package puzzle.battleship;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ui.boardview.PuzzleElement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BattleShip extends Puzzle
{
    public BattleShip()
    {
        super();

        this.name = "BattleShip";

        this.importer = null;
        this.exporter = null;

        this.factory = null;

        // ADD RULES ONCE THEY ARE CREATED
    }

    @Override
    public void initializeView()
    {
        BattleShipBoard board = (BattleShipBoard) currentBoard;
        boardView = new BattleShipView(new Dimension(board.getWidth(), board.getHeight()));
        for(PuzzleElement element : boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            BattleShipCell cell = (BattleShipCell) currentBoard.getElementData(index);

            cell.setIndex(index);
            element.setData(cell);
        }
    }

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

    @Override
    public void onBoardChange(Board board)
    {

    }
}