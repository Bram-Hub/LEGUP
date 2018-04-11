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
import ui.Selection;
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

    /**
     * Callback for when the tree selection changes
     *
     * @param newSelection
     */
    @Override
    public void onTreeSelectionChange(ArrayList<Selection> newSelection)
    {

    }

    /**
     * Imports the board using the file stream
     *
     * @param fileName
     *
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
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

            MasyuBoard masyuBoard;

            Element rootNode = document.getDocumentElement();
            Element puzzleElement = (Element)rootNode.getElementsByTagName("puzzle").item(0);
            Element boardElement = (Element)puzzleElement.getElementsByTagName("board").item(0);
            Element dataElement = (Element)boardElement.getElementsByTagName("data").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("element");

            int width = Integer.valueOf(boardElement.getAttribute("width"));
            int height = Integer.valueOf(boardElement.getAttribute("height"));
            masyuBoard = new MasyuBoard(width, height);

            ArrayList<ElementData> masyuData = new ArrayList<>();
            for(int i = 0; i < width * height; i++)
            {
                masyuData.add(null);
            }

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                MasyuCell cell = new MasyuCell(value, new Point(x, y));
                masyuBoard.setCell(x, y, cell);
                if(cell.getValueInt() != 0)
                {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
            }

            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    if(masyuBoard.getCell(x, y) == null)
                    {
                        MasyuCell cell = new MasyuCell(0, new Point(x, y));
                        cell.setModifiable(true);
                        masyuBoard.setCell(x, y, cell);
                    }
                }
            }
            this.currentBoard = masyuBoard;
            this.tree = new Tree(currentBoard);
        }
    }
}
