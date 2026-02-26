package edu.rpi.legup.app;

import edu.rpi.legup.history.History;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.history.IHistorySubject;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.LegupUI;
import edu.rpi.legup.ui.ProofEditorPanel;
import edu.rpi.legup.ui.PuzzleEditorPanel;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * {@code GameBoardFacade} is a class designed to manage the game board operations within the
 * application. It integrates various components such as UI elements, puzzle management, and history
 * tracking
 */
public class GameBoardFacade implements IHistorySubject {
    private static final Logger LOGGER = LogManager.getLogger(GameBoardFacade.class.getName());

    protected static volatile GameBoardFacade instance;

    private Config config;

    protected Puzzle puzzle;

    private LegupUI legupUI;

    private ProofEditorPanel puzzleSolver;

    private PuzzleEditorPanel puzzleEditor;

    private String curFileName;

    private History history;
    private List<IHistoryListener> historyListeners;

    /** Private GameBoardFacade Constructor creates a game board facade */
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
    public static synchronized GameBoardFacade getInstance() {
        if (instance == null) {
            instance = new GameBoardFacade();
        }
        return instance;
    }

    /** Initializes the UI components */
    public void initializeUI() {
        EventQueue.invokeLater(
                () -> {
                    legupUI = new LegupUI();
                    puzzleSolver = legupUI.getProofEditor();
                    puzzleEditor = legupUI.getPuzzleEditor();
                    addHistoryListener(legupUI.getProofEditor());
                    addHistoryListener(legupUI.getPuzzleEditor());
                });
    }

    /**
     * Sets the current puzzle in the game board
     *
     * @param puzzle the Puzzle to set
     */
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.puzzleSolver.setPuzzleView(puzzle);
        this.history.clear();
    }

    /** Clears the current puzzle */
    public void clearPuzzle() {
        this.puzzle = null;
        this.curFileName = null;
        this.history.clear();
    }

    /** Sets up the configuration by initializing the Config object */
    public static void setupConfig() {
        Config config = null;
        try {
            config = new Config();
        } catch (InvalidConfigException e) {
            System.exit(1);
        }
        GameBoardFacade.getInstance().setConfig(config);
    }

    /**
     * Sets the current puzzle editor with the given puzzle
     *
     * @param puzzle the Puzzle to set in the editor
     */
    public void setPuzzleEditor(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.puzzleEditor.setPuzzleView(puzzle);
    }

    /**
     * Sets the configuration object for the GameBoardFacade
     *
     * @param config config the Config object to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Validates the given dimensions for the given puzzle
     *
     * @param game name of the puzzle
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     * @return true if it is possible to create a board for the given game with the given number of
     *     rows and columns, false otherwise
     * @throws RuntimeException if any of the given input is invalid
     */
    public boolean validateDimensions(String game, int rows, int columns) throws RuntimeException {
        String qualifiedClassName = config.getPuzzleClassForName(game);
        try {
            Class<?> c = Class.forName(qualifiedClassName);
            Constructor<?> constructor = c.getConstructor();
            Puzzle puzzle = (Puzzle) constructor.newInstance();
            return puzzle.isValidDimensions(rows, columns);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e) {
            LOGGER.error(e);
            throw new RuntimeException("Error validating puzzle dimensions");
        }
    }

    /**
     * Validates the given text input for the given puzzle
     *
     * @param game the name of the puzzle
     * @param statements an array of statements
     * @return true if it is possible to create a board for the given game with the given
     *     statements, false otherwise
     * @throws RuntimeException if any of the input is invalid
     */
    public boolean validateTextInput(String game, String[] statements) throws RuntimeException {
        String qualifiedClassName = config.getPuzzleClassForName(game);
        try {
            Class<?> c = Class.forName(qualifiedClassName);
            Constructor<?> constructor = c.getConstructor();
            Puzzle puzzle = (Puzzle) constructor.newInstance();
            return puzzle.isValidTextInput(statements);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e) {
            LOGGER.error(e);
            throw new RuntimeException("Error validating puzzle text input");
        }
    }

    /**
     * Loads an empty puzzle with the specified dimensions
     *
     * @param game name of the puzzle
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     */
    public void loadPuzzle(String game, int rows, int columns) throws RuntimeException {
        if (!game.isEmpty()) {
            String qualifiedClassName = config.getPuzzleClassForName(game);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Loading " + qualifiedClassName);
            }

            try {
                Class<?> c = Class.forName(qualifiedClassName);
                Constructor<?> cons = c.getConstructor();
                Puzzle puzzle = (Puzzle) cons.newInstance();

                PuzzleImporter importer = puzzle.getImporter();
                if (importer == null) {
                    LOGGER.error("Puzzle importer is null");
                    throw new RuntimeException("Puzzle importer null");
                }

                // Theoretically, this exception should never be thrown, since LEGUP should not be
                // allowing the user to give row/column input for a puzzle that doesn't support it
                if (!importer.acceptsRowsAndColumnsInput()) {
                    throw new IllegalArgumentException(
                            puzzle.getName() + " does not accept rows and columns input");
                }

                setWindowTitle(puzzle.getName(), "New " + puzzle.getName() + " Puzzle");
                importer.initializePuzzle(rows, columns);

                puzzle.initializeView();
                //
                // puzzle.getBoardView().onTreeElementChanged(puzzle.getTree().getRootNode());
                setPuzzleEditor(puzzle);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(exception.getMessage());
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException
                    | InstantiationException e) {
                LOGGER.error(e);
                throw new RuntimeException("Puzzle creation error");
            }
        }
    }

    /**
     * Loads an empty puzzle with the specified input
     *
     * @param game name of the puzzle
     * @param statements an array of statements to load the puzzle with
     */
    public void loadPuzzle(String game, String[] statements) {
        String qualifiedClassName = config.getPuzzleClassForName(game);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading " + qualifiedClassName);
        }

        try {
            Class<?> c = Class.forName(qualifiedClassName);
            Constructor<?> cons = c.getConstructor();
            Puzzle puzzle = (Puzzle) cons.newInstance();

            PuzzleImporter importer = puzzle.getImporter();
            if (importer == null) {
                LOGGER.error("Puzzle importer is null");
                throw new RuntimeException("Puzzle importer null");
            }

            // Theoretically, this exception should never be thrown, since LEGUP should not be
            // allowing the user to give text input for a puzzle that doesn't support it
            if (!importer.acceptsTextInput()) {
                throw new IllegalArgumentException(
                        puzzle.getName() + " does not accept text input");
            }

            setWindowTitle(puzzle.getName(), "New " + puzzle.getName() + " Puzzle");
            importer.initializePuzzle(statements);

            puzzle.initializeView();
            //
            // puzzle.getBoardView().onTreeElementChanged(puzzle.getTree().getRootNode());
            setPuzzleEditor(puzzle);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e) {
            LOGGER.error(e);
            throw new RuntimeException("Puzzle creation error");
        }
    }

    /**
     * Loads a puzzle file from the specified file
     *
     * @param fileName file name of the board file
     * @throws InvalidFileFormatException if the file format is invalid or if the file cannot be
     *     created
     */
    public void loadPuzzle(String fileName) throws InvalidFileFormatException {
        try {
            loadPuzzle(new FileInputStream(fileName));
            curFileName = fileName;
            setWindowTitle(puzzle.getName(), fileName);
        } catch (IOException e) {
            LOGGER.error("Invalid file " + fileName, e);
            throw new InvalidFileFormatException("Could not find file");
        }
    }

    /**
     * Loads a puzzle into the editor from the specified file name
     *
     * @param fileName the name of the file to load
     * @throws InvalidFileFormatException if the file format is invalid or if the file cannot be
     *     created
     */
    public void loadPuzzleEditor(String fileName) throws InvalidFileFormatException {
        try {
            loadPuzzleEditor(new FileInputStream(fileName));
            curFileName = fileName;
            setWindowTitle(puzzle.getName(), fileName);
        } catch (IOException e) {
            LOGGER.error("Invalid file " + fileName, e);
            throw new InvalidFileFormatException("Could not find file");
        }
    }

    /**
     * Loads a puzzle into the editor from the specified input stream
     *
     * @param inputStream the input stream to load the puzzle from
     * @throws InvalidFileFormatException if the input stream cannot be processed or the file format
     *     is invalid
     */
    public void loadPuzzleEditor(InputStream inputStream) throws InvalidFileFormatException {
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Invalid file", e);
            throw new InvalidFileFormatException("Could not find file");
        }

        Element rootNode = document.getDocumentElement();
        if (rootNode.getTagName().equals("Legup")) {
            try {
                Node node = rootNode.getElementsByTagName("puzzle").item(0);
                String qualifiedClassName =
                        config.getPuzzleClassForName(
                                node.getAttributes().getNamedItem("name").getNodeValue());
                if (qualifiedClassName == null) {
                    throw new InvalidFileFormatException(
                            "Puzzle creation error: cannot find puzzle with that name");
                }
                // Check if puzzle is a "FileCreationEnabled" puzzle (meaning it is editable).
                String[] editablePuzzles =
                        config.getFileCreationEnabledPuzzles().toArray(new String[0]);
                boolean isEditablePuzzle = false;
                for (int i = 0; i < editablePuzzles.length; i++) {
                    if (qualifiedClassName.contains(editablePuzzles[i])) {
                        isEditablePuzzle = true;
                        break;
                    }
                }
                if (!isEditablePuzzle) {
                    LOGGER.error("Puzzle is not editable");
                    throw new InvalidFileFormatException("Puzzle is not editable");
                }
                // If it is editable, start loading it
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Loading " + qualifiedClassName);
                }

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
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException
                    | InstantiationException e) {
                LOGGER.error(e);
                throw new InvalidFileFormatException("Puzzle creation error");
            }
        } else {
            LOGGER.error("Invalid file");
            throw new InvalidFileFormatException("Invalid file: must be a Legup file");
        }
    }

    /**
     * Loads a puzzle file from the input stream
     *
     * @throws InvalidFileFormatException if input is invalid
     * @param inputStream input stream for the puzzle file
     */
    public void loadPuzzle(InputStream inputStream) throws InvalidFileFormatException {
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Invalid file", e);
            throw new InvalidFileFormatException("Could not find file");
        }

        Element rootNode = document.getDocumentElement();
        if (rootNode.getTagName().equals("Legup")) {
            try {
                Node node = rootNode.getElementsByTagName("puzzle").item(0);
                String qualifiedClassName =
                        config.getPuzzleClassForName(
                                node.getAttributes().getNamedItem("name").getNodeValue());
                if (qualifiedClassName == null) {
                    throw new InvalidFileFormatException(
                            "Puzzle creation error: cannot find puzzle with that name");
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Loading " + qualifiedClassName);
                }

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
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException
                    | InstantiationException e) {
                LOGGER.error(e);
                throw new InvalidFileFormatException("Puzzle creation error");
            }
        } else {
            LOGGER.error("Invalid file");
            throw new InvalidFileFormatException("Invalid file: must be a Legup file");
        }
    }

    /**
     * Sets the window title to 'PuzzleName - FileName' Removes the extension
     *
     * @param puzzleName puzzle name for the file
     * @param fileName file name of the edu.rpi.legup.puzzle
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
