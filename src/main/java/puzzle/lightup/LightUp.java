package puzzle.lightup;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.ContradictionRule;
import model.tree.Tree;
import model.tree.TreeTransition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import puzzle.lightup.rules.*;
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

public class LightUp extends Puzzle
{

    public LightUp()
    {
        super();
        name = "LightUp";

        importer = new LightUpImporter(this);
        exporter = new LightUpExporter(this);

        factory = new LightUpCellFactory();

        basicRules.add(new BulbsOutsideDiagonalBasicRule());
        basicRules.add(new EmptyCellinLightBasicRule());
        basicRules.add(new EmptyCornersBasicRule());
        basicRules.add(new FinishWithBulbsBasicRule());
        basicRules.add(new FinishWithEmptyBasicRule());
        basicRules.add(new MustLightBasicRule());

        contradictionRules.add(new BulbsInPathContradictionRule());
        contradictionRules.add(new CannotLightACellContradictionRule());
        contradictionRules.add(new TooFewBulbsContradictionRule());
        contradictionRules.add(new TooManyBulbsContradictionRule());

        caseRules.add(new LightOrEmptyCaseRule());
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        LightUpBoard board = (LightUpBoard) currentBoard;
        boardView = new LightUpView(new Dimension(board.getWidth(), board.getHeight()));
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            LightUpCell cell = (LightUpCell)currentBoard.getElementData(index);

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
        LightUpBoard lightUpBoard = (LightUpBoard)board;
        lightUpBoard.fillWithLight();
        TreeTransition transition = new TreeTransition(null, lightUpBoard);

        for(ContradictionRule rule : contradictionRules)
        {
            if(rule.checkContradiction(transition) == null)
            {
                return false;
            }
        }
        for(ElementData data : lightUpBoard.getElementData())
        {
            LightUpCell cell = (LightUpCell)data;
            if((cell.getType() == LightUpCellType.UNKNOWN || cell.getType() == LightUpCellType.EMPTY) && !cell.isLite())
            {
                return false;
            }
        }
        return true;
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
     */
    @Override
    public void importPuzzle(String fileName) throws IOException, ParserConfigurationException, SAXException
    {
        InputStream inputStream = new FileInputStream(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        LightUpBoard lightUpBoard;

        Element rootNode = document.getDocumentElement();
        Element puzzleElement = (Element)rootNode.getElementsByTagName("puzzle").item(0);
        Element boardElement = (Element)puzzleElement.getElementsByTagName("board").item(0);
        Element dataElement = (Element)boardElement.getElementsByTagName("data").item(0);
        NodeList elementDataList = dataElement.getElementsByTagName("element");

        int size = Integer.valueOf(boardElement.getAttribute("size"));
        lightUpBoard = new LightUpBoard(size);

        ArrayList<ElementData> lightUpData = new ArrayList<>();
        for(int i = 0; i < size * size; i++)
        {
            lightUpData.add(null);
        }

        for(int i = 0; i < elementDataList.getLength(); i++)
        {
            NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            LightUpCell cell = new LightUpCell(value, new Point(x, y));
            lightUpBoard.setCell(x, y, cell);
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
                if(lightUpBoard.getCell(x, y) == null)
                {
                    LightUpCell cell = new LightUpCell(-2, new Point(x, y));
                    cell.setModifiable(true);
                    lightUpBoard.setCell(x, y, cell);
                }
            }
        }
        this.currentBoard = lightUpBoard;
        this.tree = new Tree(currentBoard);
    }
}
