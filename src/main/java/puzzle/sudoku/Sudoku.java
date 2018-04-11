package puzzle.sudoku;

import controller.BoardController;
import model.gameboard.Board;
import model.Puzzle;
import model.gameboard.ElementData;
import model.tree.Tree;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import puzzle.sudoku.rules.*;
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

public class Sudoku extends Puzzle
{
    private SudokuView boardView;

    /**
     * Sudoku Constructor
     */
    public Sudoku()
    {
        super();

        this.name = "Sudoku";

        this.importer = new SudokuImporter(this);
        this.exporter = new SudokuExporter(this);

        this.factory = new SudokuCellFactory();

        basicRules.add(new AdvancedDeductionBasicRule());
        basicRules.add(new LastCellForNumberBasicRule());
        basicRules.add(new LastNumberForCellBasicRule());

        caseRules.add(new PossibleCellCaseRule());
        caseRules.add(new PossibleNumberCaseRule());

        contradictionRules.add(new NoSolutionContradictionRule());
        contradictionRules.add(new RepeatedNumberContradictionRule());
    }

    public BoardView getBoardView()
    {
        return boardView;
    }

    /**
     * Initializes the game board
     */
    @Override
    public void initializeView()
    {
        SudokuBoard board= (SudokuBoard)currentBoard;
        boardView = new SudokuView(board.getDimension());
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            SudokuCell cell = (SudokuCell) currentBoard.getElementData(index);

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
     * Determines if the puzzle was solves correctly
     *
     * @return true if the board was solved correctly, false otherwise
     */
    @Override
    public boolean isPuzzleComplete()
    {
        return false;
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

            SudokuBoard sudokuBoard;

            Element rootNode = document.getDocumentElement();
            Element puzzleElement = (Element)rootNode.getElementsByTagName("puzzle").item(0);
            Element boardElement = (Element)puzzleElement.getElementsByTagName("board").item(0);
            Element dataElement = (Element)boardElement.getElementsByTagName("data").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("element");

            int size = Integer.valueOf(boardElement.getAttribute("size"));
            sudokuBoard = new SudokuBoard(size);

            ArrayList<ElementData> sudokuData = new ArrayList<>();
            for(int i = 0; i < size * size; i++)
            {
                sudokuData.add(null);
            }

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
                int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
                int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
                int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
                SudokuCell cell = new SudokuCell(value, new Point(x, y));
                sudokuBoard.setCell(x, y, cell);
                if(cell.getValueInt() > 0)
                {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
            }

            for(int y = 0; y < size; y++)
            {
                for(int x = 0; x < size; x++)
                {
                    if(sudokuBoard.getCell(x, y) == null)
                    {
                        SudokuCell cell = new SudokuCell(0, new Point(x, y));
                        cell.setModifiable(true);
                        sudokuBoard.setCell(x, y, cell);
                    }
                }
            }

            this.currentBoard = sudokuBoard;
            this.tree = new Tree(currentBoard);
        }
    }
}
