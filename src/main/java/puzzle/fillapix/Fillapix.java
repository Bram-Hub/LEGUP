package puzzle.fillapix;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import puzzle.fillapix.rules.*;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Fillapix extends Puzzle
{
    private FillapixView boardView;

    /**
     * Fillapix Constructor
     */
    public Fillapix()
    {
        super();

        this.name = "Fillapix";

        this.importer = new FillapixImporter(this);
        this.exporter = new FillapixExporter(this);

        this.factory = new FillapixCellFactory();

        //basicRules.add(new AdvancedDeductionBasicRule());
        basicRules.add(new FinishWithBlackBasicRule());
        basicRules.add(new FinishWithWhiteBasicRule());

        caseRules.add(new BlackOrWhiteCaseRule());

        contradictionRules.add(new TooFewBlackCellsContradictionRule());
        contradictionRules.add(new TooManyBlackCellsContradictionRule());
    }

    /**
     * Initializes the game board
     */
    @Override
    public void initializeView()
    {
        FillapixBoard board = (FillapixBoard) currentBoard;
        boardView = new FillapixView(new Dimension(board.getWidth(), board.getHeight()));
        for(PuzzleElement element : boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            FillapixCell cell = (FillapixCell) currentBoard.getElementData(index);

            cell.setIndex(index);
            element.setData(cell);
        }
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
    public boolean isBoardComplete(Board board)
    {
        return false;
    }

    @Override
    public void onBoardChange(Board board)
    {

    }

    public BoardView getBoardView()
    {
        return boardView;
    }
}