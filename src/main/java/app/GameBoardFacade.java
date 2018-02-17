package app;

import model.gameboard.Board;
import model.Puzzle;
import model.rules.Tree;
import puzzles.sudoku.Sudoku;
import ui.LegupUI;
import ui.Selection;
import ui.boardview.BoardView;
import ui.boardview.IBoardListener;
import ui.rulesview.ITransitionListener;
import ui.rulesview.ITreeSelectionListener;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GameBoardFacade
{
    private final static Logger LOGGER = Logger.getLogger(GameBoardFacade.class.getName());

    private volatile static GameBoardFacade instance;

    private Config config;

    private Board board;
    private Puzzle puzzle;
    private Tree tree;

    private ArrayList<IBoardListener> boardListeners;
    private ArrayList<ITransitionListener> transitionListener;
    private ArrayList<ITreeSelectionListener> selectionListeners;

    private LegupUI legupUI;

    private ArrayList<Selection> selections;


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

        selections = new ArrayList<>();
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

    public void setBoardView(BoardView boardView)
    {
        legupUI.setBoardView(boardView);
    }

    public void setPuzzle(Puzzle puzzle)
    {
        this.puzzle = puzzle;
        this.tree = new Tree(puzzle.getCurrentBoard());
        this.legupUI.setBoardView(((Sudoku)puzzle).getBoardView());
    }

    /**
     * Loads a board file
     *
     * @param fileName file name of the board file
     */
    public void loadBoardFile(String fileName)
    {

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
        return board;
    }

    /**
     * Sets the initial board state and the tree
     *
     * @param board initial board
     */
    public void setBoard(Board board)
    {
        this.board = board;
        this.tree = new Tree(board);
    }

    /**
     * Gets the rules tree
     *
     * @return rules tree
     */
    public Tree getTree()
    {
        return tree;
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
     * Gets the currently selected ui elements
     *
     * @return list of the currently selected ui elements
     */
    public ArrayList<Selection> getSelections()
    {
        return selections;
    }

    /**
     * Sets the currently selected ui elements
     *
     * @param selections the list of the currently selected ui elements
     */
    public void setSelections(ArrayList<Selection> selections)
    {
        this.selections = selections;
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

