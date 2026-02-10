package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.RuleController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.save.ExportFileException;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.proofeditorui.rulesview.RuleFrame;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;
import edu.rpi.legup.user.Submission;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@code ProofEditorPanel} is a panel that serves as the main user interface component for the
 * proof editing functionality of LEGUP. It provides the graphical components and interactive
 * elements necessary for editing and managing proofs, including toolbars, menus, and views for
 * different aspects of proof editing. It also manages interactions with the rest of the application
 * and updates the UI based on user actions and application state changes.
 */
public class ProofEditorPanel extends LegupPanel implements IHistoryListener {
    private static final Logger LOGGER = LogManager.getLogger(ProofEditorPanel.class.getName());
    private JMenuBar mBar;
    private TreePanel treePanel;
    private FileDialog fileDialog;
    private JFrame frame;
    private RuleFrame ruleFrame;
    private DynamicView dynamicBoardView;
    private JLabel goalLabel;
    private JPanel boardSidePanel;
    private JSplitPane topHalfPanel, mainPanel;
    private TitledBorder boardBorder;
    private JButton[] toolBar1Buttons;
    private JButton[] toolBar2Buttons;
    private JMenu file;
    private JMenuItem newPuzzle,
            resetPuzzle,
            saveProofAs,
            saveProofChange,
            helpTutorial,
            preferences,
            exit;
    private JMenu edit;
    private JMenuItem undo, redo, fitBoardToScreen, fitTreeToScreen;

    private JMenu view;

    private JMenu proof;
    private JMenuItem add, delete, merge, collapse;
    private JCheckBoxMenuItem allowDefault, caseRuleGen, imdFeedback;
    private JMenu about, help;
    private JMenuItem helpLegup, aboutLegup;

    private JToolBar toolBar1;
    private JToolBar toolBar2;
    private BoardView boardView;
    private JFileChooser folderBrowser;

    private LegupUI legupUI;

    public static final int ALLOW_HINTS = 1;
    public static final int ALLOW_DEFAPP = 2;
    public static final int ALLOW_FULLAI = 4;
    public static final int ALLOW_JUST = 8;
    public static final int REQ_STEP_JUST = 16;
    public static final int IMD_FEEDBACK = 32;
    public static final int INTERN_RO = 64;
    public static final int AUTO_JUST = 128;
    private static final String[] PROFILES = {
        "No Assistance",
        "Rigorous Proof",
        "Casual Proof",
        "Assisted Proof",
        "Guided Proof",
        "Training-Wheels Proof",
        "No Restrictions"
    };
    private static final int[] PROF_FLAGS = {
        0,
        ALLOW_JUST | REQ_STEP_JUST,
        ALLOW_JUST,
        ALLOW_HINTS | ALLOW_JUST | AUTO_JUST,
        ALLOW_HINTS | ALLOW_JUST | REQ_STEP_JUST,
        ALLOW_HINTS | ALLOW_DEFAPP | ALLOW_JUST | IMD_FEEDBACK | INTERN_RO,
        ALLOW_HINTS | ALLOW_DEFAPP | ALLOW_FULLAI | ALLOW_JUST
    };
    private JMenu proofMode = new JMenu("Proof Mode");
    private JCheckBoxMenuItem[] proofModeItems = new JCheckBoxMenuItem[PROF_FLAGS.length];

    private static int CONFIG_INDEX = 0;

    protected JMenu ai = new JMenu("AI");
    protected JMenuItem runAI = new JMenuItem("Run AI to completion");
    protected JMenuItem setpAI = new JMenuItem("Run AI one Step");
    protected JMenuItem testAI = new JMenuItem("Test AI!");
    protected JMenuItem hintAI = new JMenuItem("Hint");

    /**
     * Constructs a new {@code ProofEditorPanel} with the specified parameters
     *
     * @param fileDialog the {@code FileDialog} used for file operations
     * @param frame the {@code JFrame} that contains this panel
     * @param legupUI the {@code LegupUI} instance managing the user interface
     */
    public ProofEditorPanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        this.legupUI = legupUI;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 700));
    }

    /**
     * Makes the panel visible by setting up the toolbar and content components. This method also
     * sets the menu bar of the frame to the one used by this panel.
     */
    @Override
    public void makeVisible() {
        this.removeAll();

        setupToolBar1();
        setupContent();
        frame.setJMenuBar(getMenuBar());
    }

    /**
     * Constructs and returns the {@code JMenuBar} for this panel. It populates it with various
     * {@code JMenu} and {@code JMenuItem} components related to file operations, editing, viewing,
     * and proof management. The menu bar includes:
     *
     * <ul>
     *   <li>{@code File} menu with options to open a new puzzle, reset the puzzle, save the proof,
     *       access preferences, and exit the editor.
     *   <li>{@code Edit} menu with options for undo, redo, and fitting the board or tree to the
     *       screen.
     *   <li>{@code Proof} menu with options for adding, deleting, merging, collapsing elements, and
     *       toggling settings related to rule applications and feedback.
     *   <li>{@code About} menu with options to view information about the application and access
     *       help resources.
     * </ul>
     *
     * <p>Accelerator keys are set based on the operating system (Mac or non-Mac).
     *
     * @return the {@code JMenuBar} instance containing the menus and menu items for this panel
     */
    public JMenuBar getMenuBar() {
        if (mBar != null) return mBar;
        mBar = new JMenuBar();

        file = new JMenu("File");
        newPuzzle = new JMenuItem("Open");
        resetPuzzle = new JMenuItem("Reset");
        //        genPuzzle = new JMenuItem("Puzzle Generators"); // TODO: implement puzzle
        // generator
        saveProofAs = new JMenuItem("Save As"); // create a new file to save
        saveProofChange = new JMenuItem("Save"); // save to the current file
        preferences = new JMenuItem("Preferences");
        helpTutorial = new JMenuItem("Help"); // jump to web page
        exit = new JMenuItem("Exit");

        edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        fitBoardToScreen = new JMenuItem("Fit Board to Screen");
        fitTreeToScreen = new JMenuItem("Fit Tree to Screen");

        view = new JMenu("View");

        proof = new JMenu("Proof");

        String os = LegupUI.getOS();

        add = new JMenuItem("Add");
        add.addActionListener(a -> treePanel.add());
        if (os.equals("mac")) {
            add.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            add.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        }
        proof.add(add);

        delete = new JMenuItem("Delete");
        delete.addActionListener(a -> treePanel.delete());
        if (os.equals("mac")) {
            delete.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            delete.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        }
        proof.add(delete);

        merge = new JMenuItem("Merge");
        merge.addActionListener(a -> treePanel.merge());
        if (os.equals("mac")) {
            merge.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'M', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            merge.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
        }
        proof.add(merge);

        collapse = new JMenuItem("Collapse");
        collapse.addActionListener(a -> treePanel.collapse());
        if (os.equals("mac")) {
            collapse.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            collapse.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        }
        collapse.setEnabled(false);
        proof.add(collapse);

        allowDefault =
                new JCheckBoxMenuItem(
                        "Allow Default Rule Applications",
                        LegupPreferences.getInstance()
                                .getUserPref(LegupPreferences.ALLOW_DEFAULT_RULES)
                                .equalsIgnoreCase(Boolean.toString(true)));
        allowDefault.addChangeListener(
                e -> {
                    LegupPreferences.getInstance()
                            .setUserPref(
                                    LegupPreferences.ALLOW_DEFAULT_RULES,
                                    Boolean.toString(allowDefault.isSelected()));
                });
        proof.add(allowDefault);

        caseRuleGen =
                new JCheckBoxMenuItem(
                        "Automatically generate cases for CaseRule",
                        LegupPreferences.getInstance()
                                .getUserPref(LegupPreferences.AUTO_GENERATE_CASES)
                                .equalsIgnoreCase(Boolean.toString(true)));
        caseRuleGen.addChangeListener(
                e -> {
                    LegupPreferences.getInstance()
                            .setUserPref(
                                    LegupPreferences.AUTO_GENERATE_CASES,
                                    Boolean.toString(caseRuleGen.isSelected()));
                });
        proof.add(caseRuleGen);

        imdFeedback =
                new JCheckBoxMenuItem(
                        "Provide immediate feedback",
                        LegupPreferences.getInstance()
                                .getUserPref(LegupPreferences.IMMEDIATE_FEEDBACK)
                                .equalsIgnoreCase(Boolean.toString(true)));
        imdFeedback.addChangeListener(
                e -> {
                    LegupPreferences.getInstance()
                            .setUserPref(
                                    LegupPreferences.IMMEDIATE_FEEDBACK,
                                    Boolean.toString(imdFeedback.isSelected()));
                });
        proof.add(imdFeedback);

        about = new JMenu("About");
        helpLegup = new JMenuItem("Help Legup");
        aboutLegup = new JMenuItem("About Legup");

        mBar.add(file);
        file.add(newPuzzle);
        newPuzzle.addActionListener((ActionEvent) -> loadPuzzle());
        if (os.equals("mac")) {
            newPuzzle.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        }

        file.add(resetPuzzle);
        resetPuzzle.addActionListener(
                a -> {
                    Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
                    if (puzzle != null) {
                        Tree tree = GameBoardFacade.getInstance().getTree();
                        TreeNode rootNode = tree.getRootNode();
                        if (rootNode != null) {
                            int confirmReset =
                                    JOptionPane.showConfirmDialog(
                                            this,
                                            "Reset Puzzle to Root Node?",
                                            "Confirm Reset",
                                            JOptionPane.YES_NO_OPTION);
                            if (confirmReset == JOptionPane.YES_OPTION) {

                                List<TreeTransition> children = rootNode.getChildren();
                                children.forEach(
                                        t ->
                                                puzzle.notifyTreeListeners(
                                                        l -> l.onTreeElementRemoved(t)));
                                children.forEach(
                                        t ->
                                                puzzle.notifyBoardListeners(
                                                        l -> l.onTreeElementChanged(t)));
                                rootNode.clearChildren();
                                final TreeViewSelection selection =
                                        new TreeViewSelection(
                                                treePanel.getTreeView().getElementView(rootNode));
                                puzzle.notifyTreeListeners(
                                        l -> l.onTreeSelectionChanged(selection));
                                puzzle.notifyBoardListeners(
                                        listener ->
                                                listener.onTreeElementChanged(
                                                        selection
                                                                .getFirstSelection()
                                                                .getTreeElement()));
                                GameBoardFacade.getInstance().getHistory().clear();
                            }
                        }
                    }
                });
        if (os.equals("mac")) {
            resetPuzzle.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            resetPuzzle.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
        }

        file.addSeparator();

        file.add(saveProofAs);
        saveProofAs.addActionListener((ActionEvent) -> saveProofAs());

        // save proof as...
        if (os.equals("mac")) {
            saveProofAs.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            saveProofAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        }

        // save proof change
        if (os.equals("mac")) {
            saveProofChange.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            saveProofChange.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        }

        file.add(saveProofChange);
        saveProofChange.addActionListener((ActionEvent) -> saveProofChange());
        file.addSeparator();

        // preference
        file.add(preferences);
        preferences.addActionListener(
                a -> {
                    PreferencesDialog preferencesDialog =
                            PreferencesDialog.CreateDialogForProofEditor(
                                    this.frame, this.ruleFrame);
                });
        file.addSeparator();

        // help function
        if (os.equals("mac")) {
            helpTutorial.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'H', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            helpTutorial.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
        }
        file.add(helpTutorial);

        helpTutorial.addActionListener((ActionEvent) -> helpTutorial());
        file.addSeparator();

        // exit
        file.add(exit);
        exit.addActionListener((ActionEvent) -> exitEditor());
        if (os.equals("mac")) {
            exit.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            exit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        }
        mBar.add(edit);

        edit.add(undo);
        undo.addActionListener((ActionEvent) -> GameBoardFacade.getInstance().getHistory().undo());
        if (os.equals("mac")) {
            undo.setAccelerator(
                    KeyStroke.getKeyStroke(
                            'Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        }

        edit.add(redo);

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

        edit.add(fitBoardToScreen);
        fitBoardToScreen.addActionListener(
                (ActionEvent) -> dynamicBoardView.fitBoardViewToScreen());

        edit.add(fitTreeToScreen);
        fitTreeToScreen.addActionListener((ActionEvent) -> this.fitTreeViewToScreen());

        mBar.add(proof);

        about.add(aboutLegup);
        aboutLegup.addActionListener(
                l -> {
                    JOptionPane.showMessageDialog(null, "Version: 5.1.0");
                });

        about.add(helpLegup);
        helpLegup.addActionListener(
                l -> {
                    try {
                        java.awt.Desktop.getDesktop()
                                .browse(URI.create("https://github.com/Bram-Hub/LEGUP/wiki"));
                    } catch (IOException e) {
                        LOGGER.error("Can't open web page");
                    }
                });

        mBar.add(about);

        return mBar;
    }

    /**
     * Clears the current puzzle, resets the UI to display the initial panel, and nullifies the
     * references to the tree panel and board view.
     */
    public void exitEditor() {
        // Wipes the puzzle entirely as if LEGUP just started
        GameBoardFacade.getInstance().clearPuzzle();
        this.legupUI.displayPanel(0);
        treePanel = null;
        boardView = null;
    }

    /**
     * Opens a file chooser dialog allowing the user to select a directory. It uses the user's
     * preferred directory or the last saved path if available. The selected directory is used to
     * set the new working directory.
     *
     * @return an array containing the file name and the selected file, or {@code null} if the
     *     operation was canceled
     */
    public Object[] promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noquit("Opening a new puzzle?")) {
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

        System.out.println(preferences.getSavedPath());
        return new Object[] {fileName, puzzleFile};
    }

    /**
     * Calls {@link #promptPuzzle()} to get the file information and then loads the puzzle using the
     * provided file name and file object. Updates the frame title to reflect the puzzle name. If
     * the file is not valid or an error occurs, an error message is shown, and the user is prompted
     * to try loading another puzzle.
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
     * Attempts to load a puzzle from the given file. If successful, it updates the UI to display
     * the puzzle and changes the frame title to include the puzzle name. If the file is invalid or
     * cannot be read, it shows an appropriate error message and prompts the user to try loading
     * another puzzle.
     *
     * @param fileName the name of the file to load
     * @param puzzleFile the file object representing the puzzle file
     */
    public void loadPuzzle(String fileName, File puzzleFile) {
        if (puzzleFile == null && fileName.isEmpty()) {
            legupUI.displayPanel(1);
        }
        if (puzzleFile != null && puzzleFile.exists()) {
            try {
                legupUI.displayPanel(1);
                GameBoardFacade.getInstance().loadPuzzle(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                frame.setTitle(puzzleName + " - " + puzzleFile.getName());
            } catch (InvalidFileFormatException e) {
                legupUI.displayPanel(0);
                LOGGER.error(e.getMessage());
                if (e.getMessage()
                        .contains(
                                "Proof Tree construction error: could not find rule by ID")) { // TO
                    // DO: make error
                    // message not
                    // hardcoded
                    JOptionPane.showMessageDialog(
                            null,
                            "This file runs on an outdated version of Legup\n"
                                    + "and is not compatible with the current version.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    loadPuzzle();
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "File does not exist or it cannot be read",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    loadPuzzle();
                }
            }
        }
    }

    /**
     * Uses the current puzzle and its associated exporter to save the puzzle data to the file
     * currently being used. If the puzzle or exporter is null, or if an error occurs during export,
     * the method will catch the exception and print the stack trace.
     */
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
     * Opens a file chooser dialog for the user to select a directory. The chosen directory is used
     * to determine where the puzzle file will be saved. If an exporter is available, it will be
     * used to export the puzzle data to the selected path.
     */
    private void saveProofAs() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
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
    }

    // Hyperlink for help button; links to wiki page for tutorials
    /**
     * Opens the default web browser to a help page related to the type of puzzle currently being
     * used. The URL is chosen based on the name of the puzzle. If the puzzle type is not
     * recognized, a general tutorial page is opened.
     */
    private void helpTutorial() {
        // redirecting to certain help link in wiki
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
        }
        String puz = puzzle.getName();
        String url;
        switch (puz) {
            case "LightUp":
                url = "https://github.com/Bram-Hub/Legup/wiki/Light%20up-Rules";
                break;
            case "Nurikabe":
                url = "https://github.com/Bram-Hub/Legup/wiki/Nurikabe-Rules";
                break;
            case "TreeTent":
                url = "https://github.com/Bram-Hub/Legup/wiki/Tree-Tent-Rules";
                break;
            case "Skyscrapers":
                url = "https://github.com/Bram-Hub/Legup/wiki/Skyscrapers-Rules";
                break;
            case "ShortTruthTable":
                url = "https://github.com/Bram-Hub/Legup/wiki/Short-Truth-Table-Rules";
                break;
            default:
                url = "https://github.com/Bram-Hub/Legup/wiki/LEGUP-Tutorial";
        }
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // unfinished
    public void add_drop() {
        // add the mouse event then we can use the new listener to implement and
        // we should create a need jbuttom for it to ship the rule we select.
        JPanel panel = new JPanel();
        JButton moveing_buttom = new JButton();
        moveing_buttom.setFocusPainted(false);
        moveing_buttom.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // get the selected rule
                    }
                });
        panel.add(moveing_buttom);
    }

    /**
     * Saves the puzzle using the current file name and shows a message dialog to confirm that the
     * save operation was successful. If the puzzle or exporter is null, or if an error occurs
     * during export, the method will catch the exception and print the stack trace.
     */
    private void saveProofChange() {
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
                // Save confirmation
                JOptionPane.showMessageDialog(
                        null, "Successfully Saved", "Confirm", JOptionPane.INFORMATION_MESSAGE);
            } catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays a confirmation dialog with a specified message. Returns {@code true} if the user
     * selects "No" or cancels the action, and {@code false} if the user selects "Yes".
     *
     * @param instr the message to display in the confirmation dialog
     * @return {@code true} if the user chooses not to quit, {@code false} otherwise
     */
    public boolean noquit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    /**
     * Configures the layout and components for the main user interface. This includes setting up
     * panels, split panes, and borders, and adding them to the main content pane.
     */
    protected void setupContent() {
        //        JPanel consoleBox = new JPanel(new BorderLayout());
        JPanel treeBox = new JPanel(new BorderLayout());
        JPanel ruleBox = new JPanel(new BorderLayout());

        RuleController ruleController = new RuleController();
        ruleFrame = new RuleFrame(ruleController);
        ruleBox.add(ruleFrame, BorderLayout.WEST);

        treePanel = new TreePanel();

        dynamicBoardView =
                new DynamicView(new ScrollView(new BoardController()), DynamicViewType.BOARD);
        TitledBorder titleBoard = BorderFactory.createTitledBorder("Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        goalLabel = new JLabel();
        CompoundBorder goalBorder = new CompoundBorder(
                BorderFactory.createTitledBorder("Goal Condition"),
                new EmptyBorder(0, 10, 3, 10));
        ((TitledBorder) goalBorder.getOutsideBorder()).setTitleJustification(TitledBorder.CENTER);
        goalLabel.setPreferredSize(new Dimension(0, 50));
        goalLabel.setBorder(goalBorder);

        boardSidePanel = new JPanel(new BorderLayout());
        boardSidePanel.add(goalLabel, BorderLayout.NORTH);
        boardSidePanel.add(dynamicBoardView);

        JPanel boardPanel = new JPanel(new BorderLayout());
        topHalfPanel =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, ruleFrame, boardSidePanel);
        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topHalfPanel, treePanel);
        topHalfPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.setPreferredSize(new Dimension(600, 600));

        boardPanel.add(mainPanel);
        boardPanel.setVisible(true);
        boardBorder = BorderFactory.createTitledBorder("Board");
        boardBorder.setTitleJustification(TitledBorder.CENTER);

        ruleBox.add(boardPanel);
        treeBox.add(ruleBox);
        this.add(treeBox);

        mainPanel.setDividerLocation(mainPanel.getMaximumDividerLocation() + 100);

        revalidate();
    }

    /**
     * Initializes the first toolbar, configures its appearance, and adds an 'Open' button with an
     * associated icon. An action listener is attached to the button to trigger the loading of a
     * puzzle when clicked.
     */
    private void setupToolBar1() {
        toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        toolBar1.setRollover(true);
        setToolBar2Buttons(new JButton[1]);

        URL open_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Open.png");

        // Scale the image icons down to make the buttons smaller
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

        getToolBar2Buttons()[0] = open;
        toolBar1.add(getToolBar2Buttons()[0]);

        this.add(toolBar1, BorderLayout.NORTH);
    }

    /**
     * Initializes the second toolbar, configures its appearance, and adds four buttons each with
     * associated icons. Action listeners are attached to each button to trigger their respective
     * actions when clicked:
     *
     * <ul>
     *   <li>'Directions' button triggers the `directionsToolButton` method.
     *   <li>'Undo' button triggers the undo action in the puzzle's history.
     *   <li>'Redo' button triggers the redo action in the puzzle's history.
     *   <li>'Check' button triggers the `checkProof` method.
     * </ul>
     */
    private void setupToolBar2() {
        toolBar2 = new JToolBar();
        toolBar2.setFloatable(false);
        toolBar2.setRollover(true);
        setToolBar2Buttons(new JButton[4]);

        URL directions_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Directions.png");

        ImageIcon DirectionsImageIcon = new ImageIcon(directions_url);
        Image DirectionsImage = DirectionsImageIcon.getImage();
        DirectionsImageIcon =
                new ImageIcon(
                        DirectionsImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton directions = new JButton("Directions", DirectionsImageIcon);
        directions.setFocusPainted(false);
        directions.addActionListener((ActionEvent) -> directionsToolButton());

        getToolBar2Buttons()[0] = directions;
        toolBar2.add(getToolBar2Buttons()[0]);

        URL undo_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Undo.png");

        ImageIcon UndoImageIcon = new ImageIcon(undo_url);
        Image UndoImage = UndoImageIcon.getImage();
        UndoImageIcon =
                new ImageIcon(
                        UndoImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton undo = new JButton("Undo", UndoImageIcon);
        undo.setFocusPainted(false);
        undo.addActionListener((ActionEvent) -> GameBoardFacade.getInstance().getHistory().undo());

        getToolBar2Buttons()[1] = undo;
        toolBar2.add(getToolBar2Buttons()[1]);

        URL redo_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Redo.png");

        ImageIcon RedoImageIcon = new ImageIcon(redo_url);
        Image RedoImage = RedoImageIcon.getImage();
        RedoImageIcon =
                new ImageIcon(
                        RedoImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton redo = new JButton("Redo", RedoImageIcon);
        redo.setFocusPainted(false);
        redo.addActionListener(
                (ActionEvent) -> {
                    GameBoardFacade.getInstance().getHistory().redo();
                });

        getToolBar2Buttons()[2] = redo;
        toolBar2.add(getToolBar2Buttons()[2]);

        URL check_url =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/Check.png");

        ImageIcon CheckImageIcon = new ImageIcon(check_url);
        Image CheckImage = CheckImageIcon.getImage();
        CheckImageIcon =
                new ImageIcon(
                        CheckImage.getScaledInstance(
                                this.TOOLBAR_ICON_SCALE,
                                this.TOOLBAR_ICON_SCALE,
                                Image.SCALE_SMOOTH));

        JButton check = new JButton("Check", CheckImageIcon);
        check.setFocusPainted(false);
        check.addActionListener((ActionEvent) -> checkProof());

        getToolBar2Buttons()[3] = check;
        toolBar2.add(getToolBar2Buttons()[3]);

        this.add(toolBar2, BorderLayout.NORTH);
    }

    /**
     * Sets the toolbar1 buttons
     *
     * @param toolBar1Buttons toolbar buttons
     */
    public void setToolBar1Buttons(JButton[] toolBar1Buttons) {
        this.toolBar1Buttons = toolBar1Buttons;
    }

    /**
     * Sets the toolbar2 buttons
     *
     * @param toolBar2Buttons toolbar buttons
     */
    public void setToolBar2Buttons(JButton[] toolBar2Buttons) {
        this.toolBar2Buttons = toolBar2Buttons;
    }

    /**
     * Gets the toolbar1 buttons
     *
     * @return toolbar1 buttons
     */
    public JButton[] getToolBar1Buttons() {
        return toolBar1Buttons;
    }

    /**
     * Gets the toolbar2 buttons
     *
     * @return toolbar2 buttons
     */
    public JButton[] getToolBar2Buttons() {
        return toolBar2Buttons;
    }

    /**
     * Uses the {@link GameBoardFacade} to obtain the current puzzle and board. If the puzzle is
     * complete, it notifies the user of a correct proof. If not, it alerts the user that the board
     * is not solved.
     */
    private void checkProof() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Board board = facade.getBoard();
        Board finalBoard = null;
        boolean delayStatus = true; // board.evalDelayStatus();

        repaintAll();

        Puzzle puzzle = facade.getPuzzleModule();

        if (puzzle.isPuzzleComplete()) {
            // This is for submission which is not integrated yet
            /*int confirm = JOptionPane.showConfirmDialog(null, "Congratulations! Your proof is correct. Would you like to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Submission submission = new Submission(board);
                submission.submit();
            }*/
            JOptionPane.showMessageDialog(null, "Congratulations! Your proof is correct.");
        } else {
            String message = "\nThe game board is not solved.";
            JOptionPane.showMessageDialog(
                    null, message, "Invalid proof.", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves the puzzle name from the `GameBoardFacade` and opens a corresponding rules page in
     * the default web browser.
     *
     * @throws IOException if an error occurs while trying to open the web page
     */
    private void directionsToolButton() {
        String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        // System.out.println(puzzleName);
        try {
            if (puzzleName.equals("Fillapix")) {
                java.awt.Desktop.getDesktop()
                        .browse(
                                URI.create(
                                        "https://github.com/Bram-Hub/LEGUP/wiki/Fill-a-pix-rules"));
            } else if (puzzleName.equals("LightUp")) {
                java.awt.Desktop.getDesktop()
                        .browse(
                                URI.create(
                                        "https://github.com/Bram-Hub/LEGUP/wiki/Light-up-rules"));
            } else if (puzzleName.equals("TreeTent")) {
                java.awt.Desktop.getDesktop()
                        .browse(
                                URI.create(
                                        "https://github.com/Bram-Hub/LEGUP/wiki/Tree-tent-rules"));
            } else if (puzzleName.equals("ShortTruthTables")) {
                java.awt.Desktop.getDesktop()
                        .browse(
                                URI.create(
                                        "https://github.com/Bram-Hub/LEGUP/wiki/Short-truth-table-rules"));
            } else {
                java.awt.Desktop.getDesktop()
                        .browse(
                                URI.create(
                                        "https://github.com/Bram-Hub/LEGUP/wiki/"
                                                + puzzleName
                                                + "-rules"));
            }
        } catch (IOException e) {
            LOGGER.error("Can't open web page");
        }
    }

    /** Repaints the board view and tree panel */
    private void repaintAll() {
        boardView.repaint();
        treePanel.repaint();
    }

    /**
     * Initializes the dynamic board view, updates the tree panel, and sets rules and search panels
     * based on the provided puzzle. It also updates toolbars and reloads the GUI.
     *
     * @param puzzle the puzzle to be displayed
     */
    public void setPuzzleView(Puzzle puzzle) {
        this.boardView = puzzle.getBoardView();

        boardSidePanel.remove(dynamicBoardView);
        dynamicBoardView = new DynamicView(boardView, DynamicViewType.BOARD);
        boardSidePanel.add(dynamicBoardView);
        String boardType = boardView.getBoard().getClass().getSimpleName();
        boardType = boardType.substring(0, boardType.indexOf("Board"));
        TitledBorder titleBoard = BorderFactory.createTitledBorder(boardType + " Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        this.treePanel.getTreeView().resetView();
        this.treePanel.getTreeView().setTree(puzzle.getTree());

        puzzle.addTreeListener(treePanel.getTreeView());
        puzzle.addBoardListener(puzzle.getBoardView());

        ruleFrame.getDirectRulePanel().setRules(puzzle.getDirectRules());
        ruleFrame.getCasePanel().setRules(puzzle.getCaseRules());
        ruleFrame.getContradictionPanel().setRules(puzzle.getContradictionRules());
        ruleFrame.getSearchPanel().setSearchBar(puzzle);

        toolBar1.setVisible(false);
        setupToolBar2();
        reloadGui();
    }

    /** Calls {@code repaintTree()} to refresh the tree view. */
    public void reloadGui() {
        repaintTree();
    }

    /**
     * Updates the tree view displayed in the tree panel to reflect the current state of the tree.
     */
    public void repaintTree() {
        treePanel.repaintTreeView(GameBoardFacade.getInstance().getTree());
    }

    /** Checks the proof for all files */
    private void checkProofAll() {
        GameBoardFacade facade = GameBoardFacade.getInstance();

        /*
         * Select dir to grade; recursively grade sub-dirs using traverseDir()
         * Selected dir must have sub-dirs for each student:
         * GradeThis
         *    |
         *    | -> Student 1
         *    |       |
         *    |       | -> Proofs
         */

        LegupPreferences preferences = LegupPreferences.getInstance();
        File preferredDirectory =
                new File(preferences.getUserPref(LegupPreferences.WORK_DIRECTORY));
        folderBrowser = new JFileChooser(preferredDirectory);

        folderBrowser.showOpenDialog(this);
        folderBrowser.setVisible(true);
        folderBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);

        File folder = folderBrowser.getSelectedFile();

        // Write csv file (Path,File-Name,Puzzle-Type,Score,Solved?)
        File resultFile = new File(folder.getAbsolutePath() + File.separator + "result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name,File Name,Puzzle Type,Score,Solved?\n");

            // Go through student folders
            for (final File folderEntry :
                    Objects.requireNonNull(folder.listFiles(File::isDirectory))) {
                // Write path
                String path = folderEntry.getName();
                traverseDir(folderEntry, writer, path);
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Batch grading complete.");
    }

    private boolean basicCheckProof(int[][] origCells) {
        return false;
    }

    /**
     * Traverses a given directory, grades the proofs found in the directory, and writes the results
     * to the specified CSV writer.
     *
     * @param folder the folder to traverse
     * @param writer the CSV writer
     * @param path the current path in the directory traversal
     * @throws IOException if an error occurs while writing to the CSV file
     */
    private void traverseDir(File folder, BufferedWriter writer, String path) throws IOException {
        // Recursively traverse directory
        GameBoardFacade facade = GameBoardFacade.getInstance();

        // Folder is empty
        if (Objects.requireNonNull(folder.listFiles()).length == 0) {
            writer.append(path).append(",Empty folder,,Ungradeable\n");
            return;
        }

        // Travese directory, recurse if sub-directory found
        // If ungradeable, do not leave a score (0, 1)
        for (final File f : Objects.requireNonNull(folder.listFiles())) {
            // Recurse
            if (f.isDirectory()) {
                traverseDir(f, writer, path + "/" + f.getName());
                continue;
            }

            // Set path name
            writer.append(path).append(",");

            // Load puzzle, run checker
            // If wrong file type, ungradeable
            String fName = f.getName();
            String fPath = f.getAbsolutePath();
            File puzzleFile = new File(fPath);
            if (puzzleFile.exists()) {
                // Try to load file. If invalid, note in csv
                try {
                    // Load puzzle, run checker
                    GameBoardFacade.getInstance().loadPuzzle(fPath);
                    String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                    frame.setTitle(puzzleName + " - " + puzzleFile.getName());
                    facade = GameBoardFacade.getInstance();
                    Puzzle puzzle = facade.getPuzzleModule();

                    // Write data
                    writer.append(fName).append(",");
                    writer.append(puzzle.getName()).append(",");
                    if (puzzle.isPuzzleComplete()) {
                        writer.append("1,Solved\n");
                    } else {
                        writer.append("0,Unsolved\n");
                    }
                } catch (InvalidFileFormatException e) {
                    writer.append(fName).append(",Invalid,,Ungradeable\n");
                }
            } else {
                LOGGER.debug("Failed to run sim");
            }
        }
    }

    /**
     * Returns the current board view.
     *
     * @return the current {@link BoardView}
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Returns the current dynamic board view.
     *
     * @return the current {@link DynamicView}
     */
    public DynamicView getDynamicBoardView() {
        return dynamicBoardView;
    }

    /**
     * Returns the current tree panel.
     *
     * @return the current {@link TreePanel}
     */
    public TreePanel getTreePanel() {
        return treePanel;
    }

    public String getGoalText() {
        return goalLabel.getText();
    }

    public void setGoalText(String text) {
        goalLabel.setText(text);
    }

    /**
     * Called when an action is pushed onto the edu.rpi.legup.history stack
     *
     * @param command action to push onto the stack
     */
    @Override
    public void onPushChange(ICommand command) {
        LOGGER.info("Pushing " + command.getClass().getSimpleName() + " to stack.");
        undo.setEnabled(true);
        redo.setEnabled(false);

        String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
        frame.setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
    }

    /**
     * Updates the state of the undo and redo buttons to reflect that there are no actions available
     * to undo or redo. It disables both buttons when the history is cleared.
     */
    @Override
    public void onClearHistory() {}

    /**
     * Called when an action is redone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop true if there are no more changes to redo, false otherwise
     */
    @Override
    public void onRedo(boolean isBottom, boolean isTop) {
        undo.setEnabled(!isBottom);
        redo.setEnabled(!isTop);
        if (isBottom) {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            frame.setTitle(puzzleName + " - " + puzzleFile.getName());
        } else {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            frame.setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
        }
    }

    /**
     * Called when an action is undone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop true if there are no more changes to redo, false otherwise
     */
    @Override
    public void onUndo(boolean isBottom, boolean isTop) {
        undo.setEnabled(!isBottom);
        redo.setEnabled(!isTop);
        String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
        if (isBottom) {
            frame.setTitle(puzzleName + " - " + puzzleFile.getName());
        } else {
            frame.setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
        }
    }

    /** Submits the proof file */
    private void submit() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Board board = facade.getBoard();
        boolean delayStatus = true; // board.evalDelayStatus();
        repaintAll();

        Puzzle pm = facade.getPuzzleModule();
        if (pm.isPuzzleComplete() && delayStatus) {
            // 0 means yes, 1 means no (Java's fault...)
            int confirm =
                    JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you wish to submit?",
                            "Proof Submission",
                            JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                Submission submission = new Submission(board);
                submission.submit();
            }
        } else {
            JOptionPane.showConfirmDialog(
                    null,
                    "Your proof is incorrect! Are you sure you wish to submit?",
                    "Proof Submission",
                    JOptionPane.YES_NO_OPTION);
            Submission submit = new Submission(board);
        }
    }

    public void showStatus(String status, boolean error, int timer) {
        // TODO: implement
    }

    /** Zooms the tree view to fit within the available screen space */
    protected void fitTreeViewToScreen() {
        this.treePanel.getTreeView().zoomFit();
    }
}
