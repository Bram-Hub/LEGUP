package puzzle.nurikabe;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import puzzle.nurikabe.rules.*;
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

public class Nurikabe extends Puzzle
{
    public Nurikabe()
    {
        super();

        this.name = "Nurikabe";

        this.importer = new NurikabeImporter(this);
        this.exporter = new NurikabeExporter(this);

        this.factory = new NurikabeCellFactory();

        this.basicRules.add(new BlackBetweenRegionsBasicRule());
        this.basicRules.add(new BlackBottleNeckBasicRule());
        this.basicRules.add(new CornerBlackBasicRule());
        this.basicRules.add(new FillinBlackBasicRule());
        this.basicRules.add(new FillinWhiteBasicRule());
        this.basicRules.add(new PreventBlackSquareBasicRule());
        this.basicRules.add(new SurroundRegionBasicRule());
        this.basicRules.add(new WhiteBottleNeckBasicRule());

        this.contradictionRules.add(new BlackSquareContradictionRule());
        this.contradictionRules.add(new IsolateBlackContradictionRule());
        this.contradictionRules.add(new MultipleNumbersContradictionRule());
        this.contradictionRules.add(new NoNumberContradictionRule());
        this.contradictionRules.add(new TooFewSpacesContradictionRule());
        this.contradictionRules.add(new TooManySpacesContradictionRule());

        this.caseRules.add(new BlackOrWhiteCaseRule());
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        NurikabeBoard board= (NurikabeBoard)currentBoard;
        boardView = new NurikabeView(board.getDimension());
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            NurikabeCell cell = (NurikabeCell)currentBoard.getElementData(index);

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
     * @return
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

            NurikabeBoard nurikabeBoard;

            Element rootNode = document.getDocumentElement();
            Element puzzleElement = (Element)rootNode.getElementsByTagName("puzzle").item(0);
            Element boardElement = (Element)puzzleElement.getElementsByTagName("board").item(0);
            Element dataElement = (Element)boardElement.getElementsByTagName("data").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("element");

            int size = Integer.valueOf(boardElement.getAttribute("size"));
            nurikabeBoard = new NurikabeBoard(size);

            ArrayList<ElementData> nurikabeData = new ArrayList<>();
            for(int i = 0; i < size * size; i++)
            {
                nurikabeData.add(null);
            }

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                NurikabeCell cell = new NurikabeCell(value, new Point(x, y));
                nurikabeBoard.setCell(x, y, cell);
                if(cell.getValueInt() != -2)
                {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
            }

            for(int y = 0; y < size; y++)
            {
                for(int x = 0; x < size; x++)
                {
                    if(nurikabeBoard.getCell(x, y) == null)
                    {
                        NurikabeCell cell = new NurikabeCell(-2, new Point(x, y));
                        cell.setModifiable(true);
                        nurikabeBoard.setCell(x, y, cell);
                    }
                }
            }
            this.currentBoard = nurikabeBoard;
            this.tree = new Tree(currentBoard);
        }
    }
}
