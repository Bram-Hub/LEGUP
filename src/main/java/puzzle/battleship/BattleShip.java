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

    @Override
    public void onTreeSelectionChange(ArrayList<Selection> newSelection)
    {

    }

    @Override
    public void importPuzzle(String fileName) throws IOException, ParserConfigurationException, SAXException
    {
        if(fileName != null)
        {
            InputStream inputStream = new FileInputStream(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            BattleShipBoard battleShipBoard;

            Element rootNode = document.getDocumentElement();
            Element puzzleElement = (Element) rootNode.getElementsByTagName("puzzle").item(0);
            Element boardElement = (Element) puzzleElement.getElementsByTagName("board").item(0);
            Element dataElement = (Element) boardElement.getElementsByTagName("data").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("element");

            int size = Integer.valueOf(boardElement.getAttribute("size"));
            battleShipBoard = new BattleShipBoard(size);

            ArrayList<ElementData> battleShipData = new ArrayList<>();
            for(int i = 0; i < size * size; i++)
            {
                battleShipData.add(null);
            }

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());

                BattleShipCell cell = new BattleShipCell(value, new Point(x, y));
                battleShipBoard.setCell(x, y, cell);
                cell.setModifiable(true); // CHANGE MODIFIABILITY LATER ACCORDING TO RULES, I JUST THREW SOMETHING IN THERE FOR NOW
                cell.setGiven(true);
            }

            for(int x = 0; x < size; x++)
            {
                for(int y = 0; y < size; y++)
                {
                    if(battleShipBoard.getCell(x, y) == null)
                    {
                        BattleShipCell cell = new BattleShipCell(0, new Point(x, y));
                        cell.setModifiable(true);
                        battleShipBoard.setCell(x, y, cell);
                    }
                }
            }
            this.currentBoard = battleShipBoard;
            this.tree = new Tree(currentBoard);
        }
    }
}