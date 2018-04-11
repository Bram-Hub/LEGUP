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
import ui.Selection;
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

    @Override
    public void onTreeSelectionChange(ArrayList<Selection> newSelection)
    {

    }

    /**
     * Imports the board using the file stream
     *
     * @param fileName
     */
    @Override
    public void importPuzzle(String fileName) throws IOException, ParserConfigurationException, SAXException
    {
        if(fileName != null)
        {
            InputStream inputStream = new FileInputStream(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            FillapixBoard fillapixBoard;

            Element rootNode = document.getDocumentElement();
            Element puzzleElement = (Element) rootNode.getElementsByTagName("puzzle").item(0);
            Element boardElement = (Element) puzzleElement.getElementsByTagName("board").item(0);
            Element dataElement = (Element) boardElement.getElementsByTagName("data").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("element");

            int width = Integer.valueOf(boardElement.getAttribute("width"));
            int height = Integer.valueOf(boardElement.getAttribute("height"));
            fillapixBoard = new FillapixBoard(width, height);

            ArrayList<ElementData> fillapixData = new ArrayList<>();
            for(int i = 0; i < width * height; i++)
            {
                fillapixData.add(null);
            }

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                FillapixCell cell = new FillapixCell(value, new Point(x, y));
                cell.setModifiable(true);
                fillapixBoard.setCell(x, y, cell);
            }

            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    if(fillapixBoard.getCell(x, y) == null)
                    {
                        FillapixCell cell = new FillapixCell(-1, new Point(x, y));
                        cell.setModifiable(true);
                        fillapixBoard.setCell(x, y, cell);
                    }
                }
            }

            this.currentBoard = fillapixBoard;
            this.tree = new Tree(currentBoard);
        }
    }

    public BoardView getBoardView()
    {
        return boardView;
    }
}