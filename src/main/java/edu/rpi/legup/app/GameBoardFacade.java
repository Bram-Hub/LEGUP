package edu.rpi.legup.app;

import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.history.IHistorySubject;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.ui.ProofEditorPanel;
import edu.rpi.legup.ui.PuzzleEditorPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.LegupUI;
import edu.rpi.legup.history.History;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameBoardFacade implements IHistorySubject {
    private final static Logger LOGGER = LogManager.getLogger(GameBoardFacade.class.getName());

    protected volatile static GameBoardFacade instance;

    private Config config;

    protected Puzzle puzzle;

    private LegupUI legupUI;

    private ProofEditorPanel puzzleSolver;

    private PuzzleEditorPanel puzzleEditor;

    private String curFileName;

    private History history;
    private List<IHistoryListener> historyListeners;

    /**
     * Private GameBoardFacade Constructor creates a game board facade
     */
    protected GameBoardFacade() {
        history = new History();
        historyListeners = new ArrayList<>();
        curFileName = null;
        LegupPreferences.getInstance();
        initializeUI();
    }

    /**
     * Gets the singleton instance of GameBoardFacade
     *
     * @return single instance of GameBoardFacade
     */
    public synchronized static GameBoardFacade getInstance() {
        if (instance == null) {
            instance = new GameBoardFacade();
        }
        return instance;
    }

    public void initializeUI() {
        EventQueue.invokeLater(() ->{
            legupUI = new LegupUI();
            puzzleSolver = legupUI.getProofEditor();
            puzzleEditor = legupUI.getPuzzleEditor();
            addHistoryListener(legupUI.getProofEditor());
            addHistoryListener(legupUI.getPuzzleEditor());
        });
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.puzzleSolver.setPuzzleView(puzzle);
        this.history.clear();
    }

    public static void setupConfig() {
        Config config = null;
        try {
            config = new Config();
        }
        catch (InvalidConfigException e) {
            System.exit(1);
        }
        GameBoardFacade.getInstance().setConfig(config);
    }


    public void setPuzzleEditor(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.puzzleEditor.setPuzzleView(puzzle);
//        this.history.clear();
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Validates the given dimensions for the given puzzle
     *
     * @param game    name of the puzzle
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     * @return true if it is possible to create a board for the given game with the given number of
     * rows and columns, false otherwise
     * @throws RuntimeException if any of the given input is invalid
     */
    public boolean validateDimensions(String game, int rows, int columns) throws RuntimeException {
        String qualifiedClassName = config.getPuzzleClassForName(game);
        try {
            Class<?> c = Class.forName(qualifiedClassName);
            Constructor<?> constructor = c.getConstructor();
            Puzzle puzzle = (Puzzle) constructor.newInstance();
            return puzzle.isValidDimensions(rows, columns);
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
               InstantiationException e) {
            LOGGER.error(e);
            throw new RuntimeException("Error validating puzzle dimensions");
        }
    }

    /**
     * Loads an empty puzzle
     *
     * @param game    name of the puzzle
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     */
    public void loadPuzzle(String game, int rows, int columns) throws RuntimeException {
        String qualifiedClassName = config.getPuzzleClassForName(game);
        LOGGER.debug("Loading " + qualifiedClassName);

        try {
            Class<?> c = Class.forName(qualifiedClassName);
            Constructor<?> cons = c.getConstructor();
            Puzzle puzzle = (Puzzle) cons.newInstance();
            setWindowTitle(puzzle.getName(), "New " + puzzle.getName() + " Puzzle");

            PuzzleImporter importer = puzzle.getImporter();
            if (importer == null) {
                LOGGER.error("Puzzle importer is null");
                throw new RuntimeException("Puzzle importer null");
            }

            importer.initializePuzzle(rows, columns);

            puzzle.initializeView();
//            puzzle.getBoardView().onTreeElementChanged(puzzle.getTree().getRootNode());
            setPuzzleEditor(puzzle);
        }
        catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
        catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
               IllegalAccessException | InstantiationException e) {
            LOGGER.error(e);
            throw new RuntimeException("Puzzle creation error");
        }
    }

    /**
     * Loads a puzzle file
     *
     * @param fileName file name of the board file
     */
    public void loadPuzzle(String fileName) throws InvalidFileFormatException {
        try {
            loadPuzzle(new FileInputStream(fileName));
            curFileName = fileName;
            setWindowTitle(puzzle.getName(), fileName);
        }
        catch (IOException e) {
            LOGGER.error("Invalid file " + fileName, e);
            throw new InvalidFileFormatException("Could not find file");
        }
    }

    public void loadPuzzleEditor(String fileName) throws InvalidFileFormatException {
        try {
            loadPuzzleEditor(new FileInputStream(fileName));
            curFileName = fileName;
            setWindowTitle(puzzle.getName(), fileName);
        }
        catch (IOException e) {
            LOGGER.error("Invalid file " + fileName, e);
            throw new InvalidFileFormatException("Could not find file");
        }
    }

    public void loadPuzzleEditor(InputStream inputStream) throws InvalidFileFormatException {
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Invalid file", e);
            throw new InvalidFileFormatException("Could not find file");
        }

        Element rootNode = document.getDocumentElement();
        if (rootNode.getTagName().equals("Legup")) {
            try {
                Node node = rootNode.getElementsByTagName("puzzle").item(0);
                String qualifiedClassName = config.getPuzzleClassForName(node.getAttributes().getNamedItem("name").getNodeValue());
                if (qualifiedClassName == null) {
                    throw new InvalidFileFormatException("Puzzle creation error: cannot find puzzle with that name");
                }
                LOGGER.debug("Loading " + qualifiedClassName);

                Class<?> c = Class.forName(qualifiedClassName);
                Constructor<?> cons = c.getConstructor();
                Puzzle puzzle = (Puzzle) cons.newInstance();

                PuzzleImporter importer = puzzle.getImporter();
                if (importer == null) {
                    LOGGER.error("Puzzle importer is null");
                    throw new InvalidFileFormatException("Puzzle importer null");
                }
                importer.initializePuzzle(node);
                puzzle.initializeView();
                puzzle.getBoardView().onTreeElementChanged(puzzle.getTree().getRootNode());
                setPuzzleEditor(puzzle);
            }
            catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                   IllegalAccessException | InstantiationException e) {
                LOGGER.error(e);
                throw new InvalidFileFormatException("Puzzle creation error");
            }
        }
        else {
            LOGGER.error("Invalid file");
            throw new InvalidFileFormatException("Invalid file: must be a Legup file");
        }
    }

    /**
     * Loads a puzzle file from the input stream
     *
     * @param inputStream input stream for the puzzle file
     */
    public void loadPuzzle(InputStream inputStream) throws InvalidFileFormatException {
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        }
        catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Invalid file", e);
            throw new InvalidFileFormatException("Could not find file");
        }

        Element rootNode = document.getDocumentElement();
        if (rootNode.getTagName().equals("Legup")) {
            try {
                Node node = rootNode.getElementsByTagName("puzzle").item(0);
                String qualifiedClassName = config.getPuzzleClassForName(node.getAttributes().getNamedItem("name").getNodeValue());
                if (qualifiedClassName == null) {
                    throw new InvalidFileFormatException("Puzzle creation error: cannot find puzzle with that name");
                }
                LOGGER.debug("Loading " + qualifiedClassName);

                Class<?> c = Class.forName(qualifiedClassName);
                Constructor<?> cons = c.getConstructor();
                Puzzle puzzle = (Puzzle) cons.newInstance();

                PuzzleImporter importer = puzzle.getImporter();
                if (importer == null) {
                    LOGGER.error("Puzzle importer is null");
                    throw new InvalidFileFormatException("Puzzle importer null");
                }
                importer.initializePuzzle(node);
                puzzle.initializeView();
                puzzle.getBoardView().onTreeElementChanged(puzzle.getTree().getRootNode());
                setPuzzle(puzzle);
            }
            catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                   IllegalAccessException | InstantiationException e) {
                LOGGER.error(e);
                throw new InvalidFileFormatException("Puzzle creation error");
            }
        }
        else {
            LOGGER.error("Invalid file");
            throw new InvalidFileFormatException("Invalid file: must be a Legup file");
        }
    }

    /**
     * Sets the window title to 'PuzzleName - FileName'
     * Removes the extension
     *
     * @param puzzleName puzzle name for the file
     * @param fileName   file name of the edu.rpi.legup.puzzle
     */
    public void setWindowTitle(String puzzleName, String fileName) {
        File file = new File(fileName);
        legupUI.setTitle(puzzleName + " - " + file.getName());
    }

    /**
     * Gets the Config info
     *
     * @return Config object
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Gets the LegupUI
     *
     * @return LegupUI
     */
    public LegupUI getLegupUI() {
        return legupUI;
    }

    /**
     * Gets the initial board state
     *
     * @return the initial board state
     */
    public Board getBoard() {
        return puzzle == null ? null : puzzle.getCurrentBoard();
    }

    /**
     * Sets the initial board state and the tree
     *
     * @param board initial board
     */
    public void setBoard(Board board) {
        puzzle.setCurrentBoard(board);
    }

    /**
     * Gets the rules tree
     *
     * @return rules tree
     */
    public Tree getTree() {
        return puzzle == null ? null : puzzle.getTree();
    }

    /**
     * Gets the Puzzle for the board
     *
     * @return the Puzzle for the board
     */
    public Puzzle getPuzzleModule() {
        return puzzle;
    }

    /**
     * Sets the Puzzle for the board
     *
     * @param puzzle the PuzzleModule for the board
     */
    public void setPuzzleModule(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public String getCurFileName() {
        return this.curFileName;
    }

    /**
     * Adds a {@link IHistoryListener}
     *
     * @param listener listener to add
     */
    @Override
    public void addHistoryListener(IHistoryListener listener) {
        historyListeners.add(listener);
    }

    /**
     * Adds a {@link IHistoryListener}
     *
     * @param listener listener to remove
     */
    @Override
    public void removeHistoryListener(IHistoryListener listener) {
        historyListeners.remove(listener);
    }

    /**
     * Notifies listeners
     *
     * @param algorithm algorithm to notify the listeners with
     */
    @Override
    public void notifyHistoryListeners(Consumer<? super IHistoryListener> algorithm) {
        historyListeners.forEach(algorithm);
    }

    /**
     * Gets the History object for storing changes to the board states
     *
     * @return History object
     */
    public History getHistory() {
        return history;
    }
}

