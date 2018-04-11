package app;

import model.PuzzleImporter;
import model.gameboard.Board;
import model.Puzzle;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import save.InvalidFileFormatException;
import ui.LegupUI;
import ui.boardview.IBoardListener;
import ui.rulesview.ITransitionListener;
import ui.rulesview.ITreeSelectionListener;
import utility.History;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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
        this.legupUI.setPuzzleView(puzzle);
        this.history.clear();
    }

    public void setConfig(Config config)
    {
        this.config = config;
    }

    /**
     * Loads a board file
     *
     * @param fileName file name of the board file
     */
    public void loadBoardFile(String fileName) throws InvalidFileFormatException
    {
        Document document;
        try
        {
            InputStream inputStream = new FileInputStream(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        }
        catch(IOException | SAXException | ParserConfigurationException e)
        {
            LOGGER.log(Level.SEVERE, "Invalid file");
            throw new InvalidFileFormatException("Could not find file");
        }

        Element rootNode = document.getDocumentElement();
        if(rootNode.getTagName().equals("Legup"))
        {
            try
            {
                Node node = rootNode.getElementsByTagName("puzzle").item(0);
                String qualifiedClassName = config.getPuzzleClassForName(node.getAttributes().getNamedItem("name").getNodeValue());
                System.err.println(qualifiedClassName);
                Class<?> c = Class.forName(qualifiedClassName);
                Constructor<?> cons = c.getConstructor();
                Puzzle puzzle = (Puzzle)cons.newInstance();

                PuzzleImporter importer = puzzle.getImporter();
                if(importer == null)
                {
                    throw new InvalidFileFormatException("Puzzle importer null");
                }
                importer.initializePuzzle(node);
                puzzle.initializeView();
                setPuzzle(puzzle);
            }
            catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                    IllegalAccessException | InstantiationException e)
            {
                LOGGER.log(Level.SEVERE, e.getMessage());
                throw new InvalidFileFormatException("Puzzle creation error");
            }
        }
        else
        {
            LOGGER.log(Level.ALL, "Invalid file");
            throw new InvalidFileFormatException("Invalid file: must be a Legup file");
        }
        setWindowTitle(puzzle.getName(), fileName);
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
     * @param puzzleName puzzle name for the file
     * @param fileName file name of the puzzle
     */
    public void setWindowTitle(String puzzleName, String fileName)
    {
        File file = new File(fileName);
        legupUI.setTitle(puzzleName + " - " + file.getName());
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

    public History getHistory()
    {
        return history;
    }
}

