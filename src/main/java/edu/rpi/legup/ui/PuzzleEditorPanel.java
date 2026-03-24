package edu.rpi.legup.ui;

import static java.lang.System.exit;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.ExportFileException;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.puzzleeditorui.elementsview.ElementFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import edu.rpi.legup.model.observer.IBoardListener;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.tree.TreeElement;

/**
 * Represents the panel used for puzzle editor in the LEGUP. This panel includes a variety of UI
 * components such as toolbars, menus, and split panes. It handles puzzle file operations, including
 * creating and editing puzzles.
 */
public class PuzzleEditorPanel extends LegupPanel implements IHistoryListener {

    private static final Logger LOGGER = LogManager.getLogger(PuzzleEditorPanel.class.getName());
    private JMenu[] menus;
    private JMenuItem helpLegup, aboutLegup;
    private JMenuBar menuBar;
    private JToolBar toolBar1;
    private JToolBar toolBar2;
    private JFileChooser folderBrowser;
    private JFrame frame;
    private JButton[] buttons;
    JSplitPane splitPanel;
    private JButton[] toolBar1Buttons;
    private JButton[] toolBar2Buttons;
    private JPanel elementPanel;
    private DynamicView dynamicBoardView;
    private BoardView boardView;
    private TitledBorder boardBorder;
    // private JSplitPane splitPanel, topHalfPanel;
    private FileDialog fileDialog;
    private JMenuItem undo, redo, fitBoardToScreen;
    private ElementFrame elementFrame;
    private JPanel treePanel;
    private LegupUI legupUI;
    private EditorElementController editorElementController;
    private CreatePuzzleDialog cpd;
    private HomePanel hp;
    private boolean existingPuzzle;
    private String fileName;
    private File puzzleFile;
    private JTextArea goalText;
    private JPanel boardSidePanel;
    // Add a reusable scroll pane for the goal text so it is created once and reused
    private JScrollPane goalPane;
    // Listener to update goal text when the board changes
    private IBoardListener boardListener;
    // Track which puzzle the listener is attached to so we can remove it when switching
    private Puzzle currentPuzzle;

    /**
     * Constructs a {@code PuzzleEditorPanel} with the specified file dialog, frame, and Legup UI
     * instance
     *
     * @param fileDialog the file dialog used for file operations
     * @param frame the main application frame
     * @param legupUI the Legup UI instance
     */
    public PuzzleEditorPanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        this.legupUI = legupUI;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 700));
    }

    /**
     * Sets up the content of the panel, including the layout and UI components. Initializes and
     * configures the {@code DynamicView} and {@code ElementFrame}, and adds them to the panel.
     */
    protected void setupContent() {
        JSplitPane splitPanel;
        JPanel elementBox = new JPanel(new BorderLayout());

        editorElementController = new EditorElementController();
        elementFrame = new ElementFrame(editorElementController);
        elementBox.add(elementFrame, BorderLayout.WEST);

        dynamicBoardView =
                new DynamicView(new ScrollView(new BoardController()), DynamicViewType.BOARD);
        TitledBorder titleBoard = BorderFactory.createTitledBorder("Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        // Use a multi-line, wrapped, non-editable JTextArea so
        // longer goal descriptions wrap and look consistent with the solver UI.
        goalText = new JTextArea();
        goalText.setRows(2);
        goalText.setEditable(false);
        // Make opaque so it renders reliably inside the scroll pane
        goalText.setOpaque(true);
        // Show a default message so the box is visible before a puzzle is loaded
        goalText.setText("Find all solutions to the puzzle or prove none exist.");
        // Use the panel background so it blends with the UI and remains readable
        goalText.setBackground(UIManager.getColor("Panel.background"));
        goalText.setFocusable(false);
        goalText.setLineWrap(true);
        goalText.setWrapStyleWord(true);
        // Create and store the scroll pane on the field so it can be reused
        goalPane = new JScrollPane(goalText);
        // Give the pane a small preferred height so it doesn't collapse in the layout
        goalPane.setPreferredSize(new Dimension(0, 50));
        // Also set a minimum size and reasonable max height to prevent layout collapsing
        goalPane.setMinimumSize(new Dimension(0, 40));
        goalPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        CompoundBorder goalBorder = new CompoundBorder(
                BorderFactory.createTitledBorder("Goal Condition"),
                new EmptyBorder(0, 10, 3, 10));
        ((TitledBorder) goalBorder.getOutsideBorder()).setTitleJustification(TitledBorder.CENTER);
        goalPane.setBorder(goalBorder);

        boardSidePanel = new JPanel(new BorderLayout());
        boardSidePanel.add(goalPane, BorderLayout.NORTH);
        boardSidePanel.add(dynamicBoardView);

        JPanel boardPanel = new JPanel(new BorderLayout());
        splitPanel =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, elementFrame, boardSidePanel);
        splitPanel.setPreferredSize(new Dimension(600, 400));

        boardPanel.add(splitPanel);
        boardBorder = BorderFactory.createTitledBorder("Board");
        boardBorder.setTitleJustification(TitledBorder.CENTER);

        elementBox.add(boardPanel);
        this.add(elementBox);

        splitPanel.setDividerLocation(splitPanel.getMaximumDividerLocation() + 100);
        this.splitPanel = splitPanel;
        revalidate();
    }

    /**
     * Configures the menu bar with menus and menu items for the application. Adds actions for
     * opening, creating, and exiting puzzles. Also sets up help and about menu items.
     */
    public void setMenuBar() {
        String os = LegupUI.getOS();
        menuBar = new JMenuBar();
        menus = new JMenu[3];

        // create menus

        // FILE
        menus[0] = new JMenu("File");

        // file>new
        JMenuItem openPuzzle = new JMenuItem("Open");
        openPuzzle.addActionListener((ActionEvent) -> loadPuzzle());
        if (os.equals("mac")) {
            openPuzzle.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            openPuzzle.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        }
        // file>create
        JMenuItem createPuzzle = new JMenuItem("Create");
        createPuzzle.addActionListener(
                (ActionEvent) -> {
                    hp = new HomePanel(this.frame, this.legupUI);
                    cpd = new CreatePuzzleDialog(this.frame, hp);
                    cpd.setLocationRelativeTo(null);
                    cpd.setVisible(true);
                    existingPuzzle = false;
                });
        if (os.equals("mac")) {
            createPuzzle.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            createPuzzle.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        }

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((ActionEvent) -> exitEditor());
        if (os.equals("mac")) {
            exit.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            exit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        }
        menus[0].add(openPuzzle);
        menus[0].add(createPuzzle);
        // menus[0].add(directSavePuzzle);
        menus[0].add(exit);

        // EDIT
        menus[1] = new JMenu("Edit");
        // edit>undo
        undo = new JMenuItem("Undo");
        // edit>redo
        redo = new JMenuItem("Redo");
        fitBoardToScreen = new JMenuItem("Fit Board to Screen");

        // TODO: Undo operation currently does not get updated correctly in history
        // menus[1].add(undo);
        undo.addActionListener((ActionEvent) -> GameBoardFacade.getInstance().getHistory().undo());
        if (os.equals("mac")) {
            undo.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        }

        // TODO: Redo operation currently does not get updated correctly in history
        // menus[1].add(redo);
        // Created action to support two keybinds (CTRL-SHIFT-Z, CTRL-Y)
        Action redoAction =
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameBoardFacade.getInstance().getHistory().redo();
                    }
                };
        if (os.equals("mac")) {
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(
                            KeyStroke.getKeyStroke(
                                    'Z',
                                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
                                            + InputEvent.SHIFT_DOWN_MASK),
                            "redoAction");
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(
                            KeyStroke.getKeyStroke(
                                    'Y', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                            "redoAction");
            redo.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Z',
                            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
                                    + InputEvent.SHIFT_DOWN_MASK));
        } else {
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK), "redoAction");
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(
                            KeyStroke.getKeyStroke(
                                    'Z', InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK),
                            "redoAction");
            redo.getActionMap().put("redoAction", redoAction);

            // Button in menu will show CTRL-SHIFT-Z as primary keybind
            redo.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Z', InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        }

        menus[1].add(fitBoardToScreen);
        fitBoardToScreen.addActionListener(
                (ActionEvent) -> dynamicBoardView.fitBoardViewToScreen());

        // HELP
        menus[2] = new JMenu("Help");
        helpLegup = new JMenuItem("Help Legup");
        aboutLegup = new JMenuItem("About Legup");
        menus[2].add(helpLegup);
        menus[2].add(aboutLegup);
        helpLegup.addActionListener(
                l -> {
                    try {
                        java.awt.Desktop.getDesktop()
                                .browse(URI.create("https://github.com/Bram-Hub/LEGUP/wiki"));
                    } catch (IOException e) {
                        LOGGER.error("Can't open web page");
                    }
                });
        menus[2].add(aboutLegup);
        aboutLegup.addActionListener(
                l -> {
                    JOptionPane.showMessageDialog(null, "Version: 5.1.0");
                });
        // add menus to menubar
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        frame.setJMenuBar(menuBar);
    }

    /**
     * Exits the puzzle editor and resets the application state to its initial condition. This
     * method clears the current puzzle from the {@code GameBoardFacade}, resets the display to the
     * initial panel, and nullifies references to the tree panel and board view.
     */
    public void exitEditor() {
        // Wipes the puzzle entirely as if LEGUP just started
        GameBoardFacade.getInstance().clearPuzzle();
        this.legupUI.displayPanel(0);
        treePanel = null;
        boardView = null;
    }

    /**
     * Makes the panel visible by setting up the toolbar, content, and menu bar. This method is
     * called to refresh the panel's user interface.
     */
    @Override
    public void makeVisible() {
        this.removeAll();
        setupToolBar1();
        setupContent();
        setMenuBar();
    }

    /**
     * Sets up the first toolbar with buttons for opening and creating puzzles. This method
     * initializes the toolbar buttons with their icons and actions.
     */
    private void setupToolBar1() {
        setToolBar1Buttons(new JButton[2]);

        URL open_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Open.png");
        ImageIcon OpenImageIcon = new ImageIcon(open_url);
        Image OpenImage = OpenImageIcon.getImage();
        OpenImageIcon =
                new ImageIcon(
                        OpenImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton open = new JButton("Open", OpenImageIcon);
        open.setFocusPainted(false);
        open.addActionListener((ActionEvent) -> loadPuzzle());

        getToolBar1Buttons()[0] = open;

        toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        toolBar1.setRollover(true);
        toolBar1.add(getToolBar1Buttons()[0]);

        URL create_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Open Puzzle.png");
        ImageIcon CreateImageIcon = new ImageIcon(create_url);
        Image CreateImage = CreateImageIcon.getImage();
        CreateImageIcon =
                new ImageIcon(
                        CreateImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton create = new JButton("Create", CreateImageIcon);
        create.setFocusPainted(false);
        create.addActionListener(
                (ActionEvent) -> {
                    hp = new HomePanel(this.frame, this.legupUI);
                    cpd = new CreatePuzzleDialog(this.frame, hp);
                    cpd.setLocationRelativeTo(null);
                    cpd.setVisible(true);
                    existingPuzzle = false;
                });
        getToolBar1Buttons()[1] = create;

        toolBar1.setFloatable(false);
        toolBar1.setRollover(true);
        toolBar1.add(getToolBar1Buttons()[1]);

        this.add(toolBar1, BorderLayout.NORTH);
    }

    /**
     * Sets up the second toolbar with buttons for resetting, saving, and saving & solving puzzles.
     * This method initializes the toolbar buttons with their icons and actions.
     */
    private void setupToolBar2() {
        toolBar2 = new JToolBar();
        toolBar2.setFloatable(false);
        toolBar2.setRollover(true);
        setToolBar2Buttons(new JButton[3]);

        URL reset =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Reset.png");
        ImageIcon ResetImageIcon = new ImageIcon(reset);
        Image ResetImage = ResetImageIcon.getImage();
        ResetImageIcon =
                new ImageIcon(
                        ResetImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton resetButton = new JButton("Reset", ResetImageIcon);
        resetButton.setFocusPainted(false);

        resetButton.addActionListener(
                a -> {
                    if (existingPuzzle) {
                        legupUI.getPuzzleEditor().loadPuzzle(fileName, puzzleFile);
                    } else {
                        if (cpd.getGame().equals("ShortTruthTable")) {
                            GameBoardFacade.getInstance()
                                    .loadPuzzle(cpd.getGame(), cpd.getTextArea());
                        } else {
                            GameBoardFacade.getInstance()
                                    .loadPuzzle(
                                            cpd.getGame(),
                                            Integer.valueOf(cpd.getRows()),
                                            Integer.valueOf(cpd.getColumns()));
                        }
                    }
                });

        getToolBar2Buttons()[0] = resetButton;
        toolBar2.add(getToolBar2Buttons()[0]);

        URL save_as =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Save.png");
        ImageIcon SaveAsImageIcon = new ImageIcon(save_as);
        Image SaveAsImage = SaveAsImageIcon.getImage();
        SaveAsImageIcon =
                new ImageIcon(
                        SaveAsImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton saveas = new JButton("Save As", SaveAsImageIcon);
        saveas.setFocusPainted(false);
        saveas.addActionListener((ActionEvent) -> savePuzzle());

        getToolBar2Buttons()[1] = saveas;
        toolBar2.add(getToolBar2Buttons()[1]);

        URL save_and_solve =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Check.png");
        ImageIcon SaveSolveImageIcon = new ImageIcon(save_and_solve);
        Image SaveSolveImage = SaveSolveImageIcon.getImage();
        SaveSolveImageIcon =
                new ImageIcon(
                        SaveSolveImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton saveandsolve = new JButton("Save & Solve", SaveSolveImageIcon);
        saveandsolve.setFocusPainted(false);
        saveandsolve.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (GameBoardFacade.getInstance().getPuzzleModule() != null) {
                            Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
                            Board board = GameBoardFacade.getInstance().getBoard();
                            if (puzzle.getGoal() == null || puzzle.getGoal().getType() == GoalType.DEFAULT) {
                                return;
                            }

                            for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
                                if (puzzleElement instanceof GridCell gridCell && gridCell.isGoal()) {
                                    GridCell tmp = new GridCell(gridCell.getData(), gridCell.getLocation());
                                    gridCell.setData(gridCell.getGoalData());
                                    gridCell.setGoalData(tmp.getData());
                                }
                            }


                            String filename = savePuzzle();
                            File puzzlename = new File(filename);
                            System.out.println(filename);

                            GameBoardFacade.getInstance().getLegupUI().displayPanel(1);
                            GameBoardFacade.getInstance()
                                    .getLegupUI()
                                    .getProofEditor()
                                    .loadPuzzle(filename, new File(filename));
                            String puzzleName =
                                    GameBoardFacade.getInstance().getPuzzleModule().getName();
                            frame.setTitle(puzzleName + " - " + puzzlename.getName());
                        }
                    }
                });
        getToolBar2Buttons()[2] = saveandsolve;
        toolBar2.add(getToolBar2Buttons()[2]);

//        goalLabel = new JLabel();
//        CompoundBorder goalBorder = new CompoundBorder(
//                BorderFactory.createTitledBorder("Goal Condition"),
//                new EmptyBorder(0, 10, 3, 10));
//        ((TitledBorder) goalBorder.getOutsideBorder()).setTitleJustification(TitledBorder.CENTER);
//        goalLabel.setPreferredSize(new Dimension(0, 50));
//        goalLabel.setBorder(goalBorder);

        //JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, toolBar2, goalLabel);

        this.add(toolBar2, BorderLayout.NORTH);
    }

    /**
     * Initializes a puzzle based on the provided game name, rows, and columns.
     *
     * @param game the name of the game or puzzle to load
     * @param rows the number of rows in the puzzle
     * @param columns the number of columns in the puzzle
     * @throws IllegalArgumentException if the provided arguments are invalid
     */
    public void loadPuzzleFromHome(String game, int rows, int columns)
            throws IllegalArgumentException {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        try {
            facade.loadPuzzle(game, rows, columns);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Initializes a puzzle based on the provided game name and an array of statements.
     *
     * @param game the name of the game or puzzle to load
     * @param statements an array of statements to initialize the puzzle
     * @throws IllegalArgumentException if the provided arguments are invalid
     */
    public void loadPuzzleFromHome(String game, String[] statements) {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        try {
            facade.loadPuzzle(game, statements);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Prompts the user to select a puzzle file to open. Opens a file chooser dialog and returns the
     * selected file's name and file object. If a puzzle is currently loaded, prompts the user to
     * confirm if they want to open a new puzzle.
     *
     * @return an array containing the selected file name and file object, or null if the operation
     *     was canceled
     */
    public Object[] promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noQuit("Open an existing puzzle?")) {
                return new Object[0];
            }
        }

        LegupPreferences preferences = LegupPreferences.getInstance();
        String preferredDirectory = preferences.getUserPref(LegupPreferences.WORK_DIRECTORY);
        if (preferences.getSavedPath() != "") {
            preferredDirectory = preferences.getSavedPath();
        }

        File preferredDirectoryFile = new File(preferredDirectory);
        JFileChooser fileBrowser = new JFileChooser(preferredDirectoryFile);
        String fileName = null;
        File puzzleFile = null;

        fileBrowser.showOpenDialog(this);
        fileBrowser.setVisible(true);
        fileBrowser.setCurrentDirectory(new File(preferredDirectory));
        fileBrowser.setDialogTitle("Select Proof File");
        fileBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileBrowser.setAcceptAllFileFilterUsed(false);

        File puzzlePath = fileBrowser.getSelectedFile();

        if (puzzlePath != null) {
            fileName = puzzlePath.getAbsolutePath();
            String lastDirectoryPath = fileName.substring(0, fileName.lastIndexOf(File.separator));
            preferences.setSavedPath(lastDirectoryPath);
            puzzleFile = puzzlePath;
        } else {
            // The attempt to prompt a puzzle ended gracefully (cancel)
            return null;
        }

        return new Object[] {fileName, puzzleFile};
    }

    /**
     * Loads a puzzle by prompting the user to select a puzzle file. If the user cancels the
     * operation, no action is taken. If a puzzle file is selected, it will be loaded using the file
     * name and file object.
     */
    public void loadPuzzle() {
        Object[] items = promptPuzzle();
        // Return if items == null (cancel)
        if (items == null) {
            return;
        }
        String fileName = (String) items[0];
        File puzzleFile = (File) items[1];
        loadPuzzle(fileName, puzzleFile);
    }

    /**
     * Loads a puzzle from the specified file. If the puzzle file is valid and exists, it loads the
     * puzzle and updates the UI. If the file format is invalid, an error message is displayed.
     *
     * @param fileName the name of the puzzle file
     * @param puzzleFile the file object representing the puzzle file
     */
    public void loadPuzzle(String fileName, File puzzleFile) {
        if (puzzleFile != null && puzzleFile.exists()) {
            try {
                legupUI.displayPanel(2);
                GameBoardFacade.getInstance().loadPuzzleEditor(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                frame.setTitle(puzzleName + " - " + puzzleFile.getName());
                existingPuzzle = true;
                this.fileName = fileName;
                this.puzzleFile = puzzleFile;
                Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
                Goal goal = puzzle.getGoal();


                Board board = GameBoardFacade.getInstance().getBoard();
                for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
                    if (puzzleElement instanceof GridCell gridCell && gridCell.isGoal()) {
                        GridCell tmp = new GridCell(gridCell.getData(), gridCell.getLocation());
                        gridCell.setData(gridCell.getGoalData());
                        goal.addCell(gridCell.copy());
                        gridCell.setGoalData(tmp.getData());
                    }
                }
                updateGoalLabel(puzzle);

            } catch (InvalidFileFormatException e) {
                legupUI.displayPanel(0);
                LOGGER.error(e.getMessage());
                JOptionPane.showMessageDialog(
                        null,
                        "File does not exist, cannot be read, or cannot be edited",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                loadPuzzle();
            }
        }
    }

    /**
     * Displays a confirmation dialog with the given instruction message. The method returns true if
     * the user selected "No" or cancelled the dialog, and false if the user selected "Yes".
     *
     * @param instr the instruction message to display in the confirmation dialog
     * @return true if the user selected "No" or canceled; false if the user selected "Yes"
     */
    public boolean noQuit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    /** {@inheritDoc} */
    @Override
    public void onPushChange(ICommand command) {}

    /** {@inheritDoc} */
    @Override
    public void onUndo(boolean isBottom, boolean isTop) {}

    /** {@inheritDoc} */
    @Override
    public void onRedo(boolean isBottom, boolean isTop) {}

    /** {@inheritDoc} */
    @Override
    public void onClearHistory() {}

    /**
     * Returns the current board view
     *
     * @return the board view
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Returns the array of buttons for the first toolbar
     *
     * @return the array of toolbar1 buttons
     */
    public JButton[] getToolBar1Buttons() {
        return toolBar1Buttons;
    }

    /**
     * Sets the array of buttons for the first toolbar
     *
     * @param toolBar1Buttons the array of toolbar1 buttons
     */
    public void setToolBar1Buttons(JButton[] toolBar1Buttons) {
        this.toolBar1Buttons = toolBar1Buttons;
    }

    /**
     * Returns the array of buttons for the second toolbar
     *
     * @return the array of toolbar2 buttons
     */
    public JButton[] getToolBar2Buttons() {
        return toolBar2Buttons;
    }

    /**
     * Sets the array of buttons for the second toolbar
     *
     * @param toolBar2Buttons the array of toolbar2 buttons
     */
    public void setToolBar2Buttons(JButton[] toolBar2Buttons) {
        this.toolBar2Buttons = toolBar2Buttons;
    }

    /** Repaints the current board view */
    private void repaintAll() {
        boardView.repaint();
    }

    /**
     * Sets the puzzle view based on the provided puzzle object. Updates the UI components to
     * display the new puzzle.
     *
     * @param puzzle the puzzle object to display
     */
    public void setPuzzleView(Puzzle puzzle) {
        this.boardView = puzzle.getBoardView();
        if (boardView.getBoard() instanceof GridBoard gridBoard && puzzle.getGoal() != null) {
            for (GridCell goalCell : puzzle.getGoal().getCells()) {
                GridCell boardCell = gridBoard.getCell(goalCell.getLocation());
                if (boardCell != null) {
                    boardCell.setGoal(true);
                }
            }
        }
        editorElementController.setElementController(boardView.getElementController());
        dynamicBoardView = new DynamicView(boardView, DynamicViewType.BOARD);

        // Ensure we attach a listener to refresh the goal text whenever the board changes
        if (boardListener == null) {
            boardListener = new IBoardListener() {
                @Override
                public void onTreeElementChanged(TreeElement treeElement) {
                    SwingUtilities.invokeLater(() -> updateGoalLabel(currentPuzzle));
                }

                @Override
                public void onCaseBoardAdded(CaseBoard caseBoard) {
                    // not relevant for goal text updates
                }

                @Override
                public void onBoardDataChanged(PuzzleElement puzzleElement) {
                    SwingUtilities.invokeLater(() -> updateGoalLabel(currentPuzzle));
                }
            };
        }

        // Detach listener from previous puzzle (if any) and attach to the new puzzle
        if (currentPuzzle != null && boardListener != null) {
            currentPuzzle.removeBoardListener(boardListener);
        }
        currentPuzzle = puzzle;
        if (currentPuzzle != null && boardListener != null) {
            currentPuzzle.addBoardListener(boardListener);
        }

        // Rebuild the right-hand side panel so the goal info box (goalText) remains visible
        if (this.splitPanel != null) {
            // Ensure boardSidePanel exists (created in setupContent). Reuse it and refresh contents.
            if (boardSidePanel == null) {
                boardSidePanel = new JPanel(new BorderLayout());
            }
            boardSidePanel.removeAll();

            // Reuse the existing goalPane if available; if not, create it and configure once.
            if (goalPane == null) {
                goalPane = new JScrollPane(goalText);
                goalPane.setPreferredSize(new Dimension(0, 50));
                goalPane.setMinimumSize(new Dimension(0, 40));
                goalPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                CompoundBorder goalBorder = new CompoundBorder(
                        BorderFactory.createTitledBorder("Goal Condition"),
                        new EmptyBorder(0, 10, 3, 10));
                ((TitledBorder) goalBorder.getOutsideBorder()).setTitleJustification(TitledBorder.CENTER);
                goalPane.setBorder(goalBorder);
            }

            boardSidePanel.add(goalPane, BorderLayout.NORTH);
            boardSidePanel.add(dynamicBoardView, BorderLayout.CENTER);

            this.splitPanel.setRightComponent(boardSidePanel);
            this.splitPanel.setVisible(true);
        }

        TitledBorder titleBoard =
                BorderFactory.createTitledBorder(boardView.getClass().getSimpleName());
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        puzzle.addBoardListener(puzzle.getBoardView());
        if (this.elementFrame != null) {
            elementFrame.setElements(puzzle);
        }
        updateGoalLabel(puzzle);
        toolBar1.setVisible(false);
        setupToolBar2();
    }

    /**
     * Updates the goal label to display the current goal condition text
     *
     * @param puzzle the puzzle to display the goal for
     */
    private void updateGoalLabel(Puzzle puzzle) {
        if (puzzle != null && puzzle.getGoal() != null) {
            setGoalText(puzzle.getGoal().getGoalText());
        } else {
            setGoalText("No goal condition set");
        }
    }

    /** Public accessor to set the goal text displayed in the editor. */
    public void setGoalText(String text) {
        if (goalText != null) goalText.setText(text);
    }

    /** Public accessor to retrieve the goal text shown in the editor. */
    public String getGoalText() {
        return goalText == null ? "" : goalText.getText();
    }

    /** Saves a puzzle */
    private void direct_save() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
        }
        String fileName = GameBoardFacade.getInstance().getCurFileName();
        if (fileName != null) {
            try {
                PuzzleExporter exporter = puzzle.getExporter();
                if (exporter == null) {
                    throw new ExportFileException("Puzzle exporter null");
                }
                exporter.exportPuzzle(fileName);
            } catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current puzzle to a user-selected directory. Prompts the user to select a directory
     * and saves the puzzle to that directory. Returns the path where the puzzle was saved.
     *
     * @return the path where the puzzle was saved, or an empty string if the save operation was
     *     canceled
     */
    private String savePuzzle() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return "";
        }

        //  for TreeTent, need to check validity before saving
        if (Objects.equals(puzzle.getName(), "TreeTent")) {
            if (!puzzle.checkValidity()) {
                int input =
                        JOptionPane.showConfirmDialog(
                                null,
                                "The puzzle you edited is not "
                                        + "valid, would you still like to save? ");
                if (input != 0) {
                    return "";
                }
            }
        }

        LegupPreferences preferences = LegupPreferences.getInstance();
        File preferredDirectory =
                new File(preferences.getUserPref(LegupPreferences.WORK_DIRECTORY));
        if (preferences.getSavedPath() != "") {
            preferredDirectory = new File(preferences.getSavedPath());
        }
        folderBrowser = new JFileChooser(preferredDirectory);

        folderBrowser.showSaveDialog(this);
        folderBrowser.setVisible(true);
        folderBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);

        String path = folderBrowser.getSelectedFile().getAbsolutePath();
        preferences.setSavedPath(path);
        if (path != null) {
            try {
                PuzzleExporter exporter = puzzle.getExporter();
                if (exporter == null) {
                    throw new ExportFileException("Puzzle exporter null");
                }
                puzzle.setTag(path.substring(path.lastIndexOf(File.separator) + 1));
                exporter.exportPuzzle(path);
            } catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * Returns the current dynamic board view
     *
     * @return the dynamic board view
     */
    public DynamicView getDynamicBoardView() {
        return dynamicBoardView;
    }
}
