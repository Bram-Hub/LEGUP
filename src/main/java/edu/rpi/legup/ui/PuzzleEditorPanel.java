package edu.rpi.legup.ui;

import static java.lang.System.exit;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
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

public class PuzzleEditorPanel extends LegupPanel implements IHistoryListener {

    private static final Logger LOGGER = LogManager.getLogger(PuzzleEditorPanel.class.getName());
    private JMenu[] menus;
    private JMenuItem helpLegup, aboutLegup;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JFileChooser folderBrowser;
    private JFrame frame;
    private JButton[] buttons;
    JSplitPane splitPanel;
    private JButton[] toolBarButtons;
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
    static final int[] TOOLBAR_SEPARATOR_BEFORE = {2, 4, 8};

    public PuzzleEditorPanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        this.legupUI = legupUI;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 700));
    }

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

        JPanel boardPanel = new JPanel(new BorderLayout());
        splitPanel =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, elementFrame, dynamicBoardView);
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

    public void setMenuBar() {
        String os = LegupUI.getOS();
        menuBar = new JMenuBar();
        menus = new JMenu[3];

        // create menus

        // FILE
        menus[0] = new JMenu("File");

        // file>new
        JMenuItem newPuzzle = new JMenuItem("New");
        newPuzzle.addActionListener((ActionEvent) -> loadPuzzle());
        if (os.equals("mac")) {
            newPuzzle.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        }
        // file>save
        JMenuItem savePuzzle = new JMenuItem("Save As");
        savePuzzle.addActionListener((ActionEvent) -> savePuzzle());
        JMenuItem directSavePuzzle = new JMenuItem("Direct Save Proof ");
        directSavePuzzle.addActionListener((ActionEvent) -> direct_save());
        if (os.equals("mac")) {
            newPuzzle.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
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
        menus[0].add(newPuzzle);
        menus[0].add(savePuzzle);
        menus[0].add(directSavePuzzle);
        menus[0].add(exit);

        // EDIT
        menus[1] = new JMenu("Edit");
        // edit>undo
        undo = new JMenuItem("Undo");
        // edit>redo
        redo = new JMenuItem("Redo");
        fitBoardToScreen = new JMenuItem("Fit Board to Screen");

        menus[1].add(undo);
        undo.addActionListener((ActionEvent) -> GameBoardFacade.getInstance().getHistory().undo());
        if (os.equals("mac")) {
            undo.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        }

        menus[1].add(redo);

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

    public void exitEditor() {
        // Wipes the puzzle entirely as if LEGUP just started
        GameBoardFacade.getInstance().clearPuzzle();
        this.legupUI.displayPanel(0);
        treePanel = null;
        boardView = null;
    }

    @Override
    public void makeVisible() {
        this.removeAll();

        setupToolBar();
        setupContent();
        setMenuBar();
    }

    private void setupToolBar() {
        setToolBarButtons(new JButton[ToolbarName.values().length + 1]);
        int lastone = 0;
        for (int i = 0; i < ToolbarName.values().length - 1; i++) {
            String toolBarName = ToolbarName.values()[i].toString();
            URL resourceLocation =
                    ClassLoader.getSystemClassLoader()
                            .getResource("edu/rpi/legup/images/Legup/" + toolBarName + ".png");

            // Scale the image icons down to make the buttons smaller
            ImageIcon imageIcon = new ImageIcon(resourceLocation);
            Image image = imageIcon.getImage();
            imageIcon =
                    new ImageIcon(
                            image.getScaledInstance(
                                    this.TOOLBAR_ICON_SCALE,
                                    this.TOOLBAR_ICON_SCALE,
                                    Image.SCALE_SMOOTH));

            JButton button = new JButton(toolBarName, imageIcon);
            button.setFocusPainted(false);
            getToolBarButtons()[i] = button;
            lastone = i;
        }

        URL check_and_save =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Check.png");
        ImageIcon imageIcon = new ImageIcon(check_and_save);
        Image image = imageIcon.getImage();
        imageIcon =
                new ImageIcon(
                        image.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton checkandsave = new JButton("check and Save", imageIcon);
        checkandsave.setFocusPainted(false);
        checkandsave.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // savePuzzle();
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
                });
        getToolBarButtons()[lastone + 1] = checkandsave;
        System.out.println("it is create new file");

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        for (int i = 0; i < getToolBarButtons().length - 1; i++) {
            for (int s = 0; s < TOOLBAR_SEPARATOR_BEFORE.length; s++) {
                if (i == TOOLBAR_SEPARATOR_BEFORE[s]) {
                    toolBar.addSeparator();
                }
            }
            String toolBarName = ToolbarName.values()[i].toString();

            toolBar.add(getToolBarButtons()[i]);
            getToolBarButtons()[i].setToolTipText(toolBarName);

            getToolBarButtons()[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            getToolBarButtons()[i].setHorizontalTextPosition(SwingConstants.CENTER);
        }

        //        toolBarButtons[ToolbarName.OPEN_PUZZLE.ordinal()].addActionListener((ActionEvent
        // e) ->
        // promptPuzzle());
        //        toolBarButtons[ToolbarName.SAVE.ordinal()].addActionListener((ActionEvent e) ->
        // saveProof());
        //        toolBarButtons[ToolbarName.UNDO.ordinal()].addActionListener((ActionEvent e) ->
        // GameBoardFacade.getInstance().getHistory().undo());
        //        toolBarButtons[ToolbarName.REDO.ordinal()].addActionListener((ActionEvent e) ->
        // GameBoardFacade.getInstance().getHistory().redo());
        toolBarButtons[ToolbarName.HINT.ordinal()].addActionListener((ActionEvent e) -> {});
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].addActionListener((ActionEvent e) -> {});
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].addActionListener((ActionEvent e) -> {});

        //        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(false);
        //        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        //        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.HINT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].setEnabled(false);

        this.add(toolBar, BorderLayout.NORTH);
    }

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

    // File opener
    public Object[] promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noQuit("Opening a new puzzle?")) {
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
        System.out.println(puzzlePath.getAbsolutePath());

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

    public void loadPuzzle(String fileName, File puzzleFile) {
        if (puzzleFile != null && puzzleFile.exists()) {
            try {
                legupUI.displayPanel(2);
                GameBoardFacade.getInstance().loadPuzzleEditor(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                frame.setTitle(puzzleName + " - " + puzzleFile.getName());
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

    public boolean noQuit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    @Override
    public void onPushChange(ICommand command) {}

    @Override
    public void onUndo(boolean isBottom, boolean isTop) {}

    @Override
    public void onRedo(boolean isBottom, boolean isTop) {}

    @Override
    public void onClearHistory() {
        // undo.setEnabled(false);
        // redo.setEnabled(false);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public JButton[] getToolBarButtons() {
        return toolBarButtons;
    }

    public void setToolBarButtons(JButton[] toolBarButtons) {
        this.toolBarButtons = toolBarButtons;
    }

    private void repaintAll() {
        boardView.repaint();
    }

    public void setPuzzleView(Puzzle puzzle) {
        this.boardView = puzzle.getBoardView();
        editorElementController.setElementController(boardView.getElementController());
        dynamicBoardView = new DynamicView(boardView, DynamicViewType.BOARD);
        if (this.splitPanel != null) {
            this.splitPanel.setRightComponent(dynamicBoardView);
            this.splitPanel.setVisible(true);
        }

        TitledBorder titleBoard =
                BorderFactory.createTitledBorder(boardView.getClass().getSimpleName());
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        puzzle.addBoardListener(puzzle.getBoardView());
        System.out.println("Setting elements");
        if (this.elementFrame != null) {
            elementFrame.setElements(puzzle);
        }

        toolBarButtons[ToolbarName.CHECK.ordinal()].setEnabled(true);
        //        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(true);
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

        if (path != null) {
            try {
                PuzzleExporter exporter = puzzle.getExporter();
                if (exporter == null) {
                    throw new ExportFileException("Puzzle exporter null");
                }
                exporter.exportPuzzle(path);
            } catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public DynamicView getDynamicBoardView() {
        return dynamicBoardView;
    }
}
