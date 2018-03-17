package app;

import model.gameboard.Board;
import model.Puzzle;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import puzzle.sudoku.Sudoku;
import ui.LegupUI;
import ui.Selection;
import ui.boardview.BoardView;
import ui.boardview.IBoardListener;
import ui.rulesview.ITransitionListener;
import ui.rulesview.ITreeSelectionListener;
import utility.History;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBoardFacade
{
    private final static Logger LOGGER = Logger.getLogger(GameBoardFacade.class.getName());

    private volatile static GameBoardFacade instance;

    private Config config;

    private Puzzle puzzle;

    private ArrayList<IBoardListener> boardListeners;
    private ArrayList<ITransitionListener> transitionListener;
    private ArrayList<ITreeSelectionListener> selectionListeners;

    private LegupUI legupUI;

    private History history;

    /**
     * Private GameBoardFacade Constructor - creates a game board facade
     */
    private GameBoardFacade()
    {
        config = new Config();

        boardListeners = new ArrayList<>();
        transitionListener = new ArrayList<>();
        selectionListeners = new ArrayList<>();

        legupUI = new LegupUI();

        history = new History();
    }

    /**
     * Gets the singleton instance of GameBoardFacade
     *
     * @return single instance of GameBoardFacade
     */
    public synchronized static GameBoardFacade getInstance()
    {
        if(instance == null)
        {
            instance = new GameBoardFacade();
        }
        return instance;
    }

    public void setPuzzle(Puzzle puzzle)
    {
        this.puzzle = puzzle;
        this.puzzle.setTree(new Tree(puzzle.getCurrentBoard()));
        this.legupUI.setPuzzleView(puzzle);
    }

    /**
     * Loads a board file
     *
     * @param fileName file name of the board file
     */
    public void loadBoardFile(String fileName)
    {
        Document document = null;
        try
        {
            InputStream inputStream = new FileInputStream(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        }
        catch(IOException e)
        {
            LOGGER.log(Level.ALL, "Invalid file");
        }
        catch(SAXException e)
        {
            LOGGER.log(Level.ALL, "Invalid file");
        }
        catch(ParserConfigurationException e)
        {
            LOGGER.log(Level.ALL, "Invalid file");
        }

        Element rootNode = document.getDocumentElement();
        if(rootNode.getTagName().equals("Legup"))
        {
            try
            {
                Node node = rootNode.getElementsByTagName("puzzle").item(0);
                System.err.println(node.getAttributes().getNamedItem("qualifiedClassName").getNodeValue());
                Class<?> c = Class.forName(node.getAttributes().getNamedItem("qualifiedClassName").getNodeValue());
                Constructor<?> cons = c.getConstructor();
                Puzzle puzzle = (Puzzle)cons.newInstance();
                try
                {
                    puzzle.importPuzzle(fileName);
                    puzzle.initializeBoard();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
                setPuzzle(puzzle);
            }
            catch(ClassNotFoundException e)
            {

            }
            catch(NoSuchMethodException e)
            {

            }
            catch(InvocationTargetException e)
            {

            }
            catch(IllegalAccessException e)
            {

            }
            catch(InstantiationException e)
            {

            }
        }
        else
        {
            LOGGER.log(Level.ALL, "Invalid file");
        }
    }

    /**
     * Loads a proof file
     *
     * @param fileName file name of the proof file
     */
    public void loadProofFile(String fileName)
    {

    }

    /**
     * Sets the window title to 'PuzzleName - FileName'
     * Removes the extension
     *
     * @param fileName file name of the puzzle
     * @param puzzleName puzzle name for the file
     */
    public void setWindowTitle(String fileName, String puzzleName)
    {
        String fileNameNoExt = fileName;
        if(fileName.substring(fileName.length() - 4).equals(".xml"))
        {
            fileNameNoExt = fileName.substring(0, fileName.length() - 4);
        }
        else if(fileName.substring(fileName.length() - 6).equals(".proof"))
        {
            fileNameNoExt = fileName.substring(0, fileName.length() - 6);
        }
        String[] filePath = fileNameNoExt.split("/");
        legupUI.setTitle(puzzleName + " - " + filePath[filePath.length - 1]);
    }

    /**
     * Repaints the entire GUI
     */
    public void repaintGui()
    {
        legupUI.reloadGui();
    }

    /**
     * Gets the Config info
     *
     * @return Config object
     */
    public Config getConfig()
    {
        return config;
    }


    /**
     * Gets the LegupUI
     *
     * @return LegupUI
     */
    public LegupUI getLegupUI()
    {
        return legupUI;
    }

    /**
     * Gets the initial board state
     *
     * @return the initial board state
     */
    public Board getBoard()
    {
        return puzzle == null ? null : puzzle.getCurrentBoard();
    }

    /**
     * Sets the initial board state and the tree
     *
     * @param board initial board
     */
    public void setBoard(Board board)
    {
        puzzle.setCurrentBoard(board);
    }

    /**
     * Gets the rules tree
     *
     * @return rules tree
     */
    public Tree getTree()
    {
        return puzzle == null ? null : puzzle.getTree();
    }

    /**
     * Gets the Puzzle for the board
     *
     * @return the Puzzle for the board
     */
    public Puzzle getPuzzleModule()
    {
        return puzzle;
    }

    /**
     * Sets the Puzzle for the board
     *
     * @param puzzle the PuzzleModule for the board
     */
    public void setPuzzleModule(Puzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    /**
     * Adds a board listener
     *
     * @param listener board listener
     */
    public void addBoardListener(IBoardListener listener)
    {
        boardListeners.add(listener);
    }

    /**
     * Removes a board listener
     *
     * @param listener board listener
     */
    public void removeBoardListener(IBoardListener listener)
    {
        boardListeners.remove(listener);
    }

    /**
     * Adds a transition listener
     *
     * @param listener transition listener
     */
    public void addBoardListeners(ITransitionListener listener)
    {
        transitionListener.add(listener);
    }

    /**
     * Removes a transition listener
     *
     * @param listener transition listener
     */
    public void removeBoardListeners(ITransitionListener listener)
    {
        transitionListener.remove(listener);
    }
}

