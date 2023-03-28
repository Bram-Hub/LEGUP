package edu.rpi.legup.ui;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
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
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;
import edu.rpi.legup.user.Submission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

public class ProofEditorPanel extends LegupPanel implements IHistoryListener {
    private final static Logger LOGGER = LogManager.getLogger(ProofEditorPanel.class.getName());
    private JMenuBar mBar;
    private TreePanel treePanel;
    private FileDialog fileDialog;
    private JFrame frame;
    private RuleFrame ruleFrame;
    private DynamicView dynamicBoardView;
    private JSplitPane topHalfPanel, mainPanel;
    private TitledBorder boardBorder;

    private JButton[] toolBarButtons;
    private JMenu file;
    private JMenuItem newPuzzle, resetPuzzle, saveProofAs,saveProofChange,helpTutorial, preferences, exit;
    private JMenu edit;
    private JMenuItem undo, redo, fitBoardToScreen, fitTreeToScreen;

    private JMenu view;

    private JMenu proof;
    private JMenuItem add, delete, merge, collapse;
    private JCheckBoxMenuItem allowDefault, caseRuleGen, imdFeedback;

    private JMenu about, help;
    private JMenuItem helpLegup, aboutLegup;

    private JToolBar toolBar;
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
    final static int[] TOOLBAR_SEPARATOR_BEFORE = {2, 4, 8};
    private static final String[] PROFILES = {"No Assistance", "Rigorous Proof", "Casual Proof", "Assisted Proof", "Guided Proof", "Training-Wheels Proof", "No Restrictions"};
    private static final int[] PROF_FLAGS = {0, ALLOW_JUST | REQ_STEP_JUST, ALLOW_JUST, ALLOW_HINTS | ALLOW_JUST | AUTO_JUST, ALLOW_HINTS | ALLOW_JUST | REQ_STEP_JUST, ALLOW_HINTS | ALLOW_DEFAPP | ALLOW_JUST | IMD_FEEDBACK | INTERN_RO, ALLOW_HINTS | ALLOW_DEFAPP | ALLOW_FULLAI | ALLOW_JUST};
    private JMenu proofMode = new JMenu("Proof Mode");
    private JCheckBoxMenuItem[] proofModeItems = new JCheckBoxMenuItem[PROF_FLAGS.length];

    private static int CONFIG_INDEX = 0;

    protected JMenu ai = new JMenu("AI");
    protected JMenuItem runAI = new JMenuItem("Run AI to completion");
    protected JMenuItem setpAI = new JMenuItem("Run AI one Step");
    protected JMenuItem testAI = new JMenuItem("Test AI!");
    protected JMenuItem hintAI = new JMenuItem("Hint");

    public ProofEditorPanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        this.legupUI = legupUI;
        setLayout(new BorderLayout());
    }

    @Override
    public void makeVisible() {
        this.removeAll();

        setupToolBar();
        setupContent();
        frame.setJMenuBar(getMenuBar());
    }

    public JMenuBar getMenuBar() {
        if (mBar != null) return mBar;
        mBar = new JMenuBar();

        file = new JMenu("File");
        newPuzzle = new JMenuItem("Open");
        resetPuzzle = new JMenuItem("Reset Puzzle");
//        genPuzzle = new JMenuItem("Puzzle Generators"); // TODO: implement puzzle generator
        saveProofAs = new JMenuItem("Save Proof As"); // create a new file to save
        saveProofChange = new JMenuItem("Save Proof Change"); // save to the current file
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
            add.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            add.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        }
        proof.add(add);

        delete = new JMenuItem("Delete");
        delete.addActionListener(a -> treePanel.delete());
        if (os.equals("mac")) {
            delete.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            delete.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        }
        proof.add(delete);

        merge = new JMenuItem("Merge");
        merge.addActionListener(a -> treePanel.merge());
        if (os.equals("mac")) {
            merge.setAccelerator(KeyStroke.getKeyStroke('M', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            merge.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
        }
        proof.add(merge);

        collapse = new JMenuItem("Collapse");
        collapse.addActionListener(a -> treePanel.collapse());
        if (os.equals("mac")) {
            collapse.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            collapse.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        }
        collapse.setEnabled(false);
        proof.add(collapse);

        allowDefault = new JCheckBoxMenuItem("Allow Default Rule Applications",
                LegupPreferences.getInstance().getUserPref(LegupPreferences.ALLOW_DEFAULT_RULES).equalsIgnoreCase(Boolean.toString(true)));
        allowDefault.addChangeListener(e -> {
            LegupPreferences.getInstance().setUserPref(LegupPreferences.ALLOW_DEFAULT_RULES, Boolean.toString(allowDefault.isSelected()));
        });
        proof.add(allowDefault);

        caseRuleGen = new JCheckBoxMenuItem("Automatically generate cases for CaseRule",
                LegupPreferences.getInstance().getUserPref(LegupPreferences.AUTO_GENERATE_CASES).equalsIgnoreCase(Boolean.toString(true)));
        caseRuleGen.addChangeListener(e -> {
            LegupPreferences.getInstance().setUserPref(LegupPreferences.AUTO_GENERATE_CASES, Boolean.toString(caseRuleGen.isSelected()));
        });
        proof.add(caseRuleGen);

        imdFeedback = new JCheckBoxMenuItem("Provide immediate feedback",
                LegupPreferences.getInstance().getUserPref(LegupPreferences.IMMEDIATE_FEEDBACK).equalsIgnoreCase(Boolean.toString(true)));
        imdFeedback.addChangeListener(e -> {
            LegupPreferences.getInstance().setUserPref(LegupPreferences.IMMEDIATE_FEEDBACK, Boolean.toString(imdFeedback.isSelected()));
        });
        proof.add(imdFeedback);

        about = new JMenu("About");
        helpLegup = new JMenuItem("Help Legup");
        aboutLegup = new JMenuItem("About Legup");

        mBar.add(file);
        file.add(newPuzzle);
        newPuzzle.addActionListener((ActionEvent) -> loadPuzzle());
        if (os.equals("mac")) {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        }

        file.add(resetPuzzle);
        resetPuzzle.addActionListener(a -> {
            Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
            if (puzzle != null) {
                Tree tree = GameBoardFacade.getInstance().getTree();
                TreeNode rootNode = tree.getRootNode();
                if (rootNode != null) {
                    int confirmReset = JOptionPane.showConfirmDialog(this, "Reset Puzzle to Root Node?", "Confirm Reset", JOptionPane.YES_NO_OPTION);
                    if (confirmReset == JOptionPane.YES_OPTION) {
                        List<TreeTransition> children = rootNode.getChildren();
                        children.forEach(t -> puzzle.notifyTreeListeners(l -> l.onTreeElementRemoved(t)));
                        final TreeViewSelection selection = new TreeViewSelection(treePanel.getTreeView().getElementView(rootNode));
                        puzzle.notifyTreeListeners(l -> l.onTreeSelectionChanged(selection));
                        GameBoardFacade.getInstance().getHistory().clear();
                    }
                }
            }
        });
        if (os.equals("mac")) {
            resetPuzzle.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            resetPuzzle.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
        }
        file.addSeparator();

        file.add(saveProofAs);
        saveProofAs.addActionListener((ActionEvent) -> saveProofAs());


        //save proof as
        if (os.equals("mac")) {
            saveProofAs.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            saveProofAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        }

        // save proof change
        if (os.equals("mac")) {
            saveProofChange.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            saveProofChange.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        }


        file.add(saveProofChange);
        saveProofChange.addActionListener((ActionEvent) -> saveProofChange());
        file.addSeparator();

        // preference
        file.add(preferences);
        preferences.addActionListener(a -> {
            PreferencesDialog preferencesDialog = new PreferencesDialog(this.frame);
        });
        file.addSeparator();

        // help function
        if (os.equals("mac")) {
            helpTutorial.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            helpTutorial.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
        }
        file.add(helpTutorial);

        helpTutorial.addActionListener((ActionEvent) -> helpTutorial());
        file.addSeparator();


        //exit
        file.add(exit);
        exit.addActionListener((ActionEvent) -> this.legupUI.displayPanel(0));
        if (os.equals("mac")) {
            exit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            exit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        }
        mBar.add(edit);


        edit.add(undo);
        undo.addActionListener((ActionEvent) ->
                GameBoardFacade.getInstance().getHistory().undo());
        if (os.equals("mac")) {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        }

        edit.add(redo);

        // Created action to support two keybinds (CTRL-SHIFT-Z, CTRL-Y)
        Action redoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameBoardFacade.getInstance().getHistory().redo();
            }
        };
        if (os.equals("mac")) {
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_DOWN_MASK), "redoAction");
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "redoAction");
            redo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_DOWN_MASK));
        }
        else {
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK), "redoAction");
            redo.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('Z', InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK), "redoAction");
            redo.getActionMap().put("redoAction", redoAction);

            // Button in menu will show CTRL-SHIFT-Z as primary keybind
            redo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        }

        edit.add(fitBoardToScreen);
        fitBoardToScreen.addActionListener((ActionEvent) -> dynamicBoardView.fitBoardViewToScreen());

        edit.add(fitTreeToScreen);
        fitTreeToScreen.addActionListener((ActionEvent) -> this.fitTreeViewToScreen());

        mBar.add(proof);

        about.add(aboutLegup);
        aboutLegup.addActionListener(l -> {
            JOptionPane.showMessageDialog(null, "Version: 2.0.0");
        });

        about.add(helpLegup);
        helpLegup.addActionListener(l -> {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/Bram-Hub/LEGUP/wiki"));
            }
            catch (IOException e) {
                LOGGER.error("Can't open web page");
            }
        });

        mBar.add(about);

        return mBar;
    }

    // File opener
    public Object[] promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noquit("Opening a new puzzle?")) {
                return new Object[0];
            }
        }

        LegupPreferences preferences = LegupPreferences.getInstance();
        String preferredDirectory = preferences.getUserPref(LegupPreferences.WORK_DIRECTORY);

        File preferredDirectoryFile = new File(preferredDirectory);
        JFileChooser fileBrowser = new JFileChooser(preferredDirectoryFile);
        String fileName = null;
        File puzzleFile = null;

        fileBrowser.showOpenDialog(this);
        fileBrowser.setVisible(true);
        fileBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
        fileBrowser.setDialogTitle("Select Proof File");
        fileBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileBrowser.setAcceptAllFileFilterUsed(false);

        File puzzlePath = fileBrowser.getSelectedFile();
        System.out.println(puzzlePath.getAbsolutePath());

        if (puzzlePath != null) {
            fileName = puzzlePath.getAbsolutePath();
            puzzleFile = puzzlePath;
        }
        else {
            // The attempt to prompt a puzzle ended gracefully (cancel)
            return null;
        }

        return new Object[]{fileName, puzzleFile};
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
                legupUI.displayPanel(1);
                GameBoardFacade.getInstance().loadPuzzle(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                frame.setTitle(puzzleName + " - " + puzzleFile.getName());
            }
            catch (InvalidFileFormatException e) {
                legupUI.displayPanel(0);
                LOGGER.error(e.getMessage());
                if (e.getMessage().contains("Proof Tree construction error: could not find rule by ID")) { // TO DO: make error message not hardcoded
                    JOptionPane.showMessageDialog(null, "This file runs on an outdated version of Legup\nand is not compatible with the current version.", "Error", JOptionPane.ERROR_MESSAGE);
                    loadPuzzle();
                }
                else {
                    JOptionPane.showMessageDialog(null, "File does not exist or it cannot be read", "Error", JOptionPane.ERROR_MESSAGE);
                    loadPuzzle();
                }
            }
        }
    }

    /**
     * direct Saves the current prdoof in current file
     */
    private void direct_save(){
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
            }
            catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Create a new file and save proof to it
     */
    private void saveProofAs() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
        }

        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setTitle("Save Proof As");
        String curFileName = GameBoardFacade.getInstance().getCurFileName();
        if (curFileName == null) {
            fileDialog.setDirectory(LegupPreferences.getInstance().getUserPref(LegupPreferences.WORK_DIRECTORY));
        }
        else {
            File curFile = new File(curFileName);
            fileDialog.setDirectory(curFile.getParent());
        }
        fileDialog.setVisible(true);

        String fileName = null;
        if (fileDialog.getDirectory() != null && fileDialog.getFile() != null) {
            fileName = fileDialog.getDirectory() + File.separator + fileDialog.getFile();
        }

        if (fileName != null) {
            try {
                PuzzleExporter exporter = puzzle.getExporter();
                if (exporter == null) {
                    throw new ExportFileException("Puzzle exporter null");
                }
                exporter.exportPuzzle(fileName);
            }
            catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
    }

    // Hyperlink for help button; links to wiki page for tutorials
    private void helpTutorial() {
        //redirecting to certain help link in wiki
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if(puzzle == null){
            return;
        }
        String puz = puzzle.getName();
        String url;
        switch (puz){
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
        Runtime rt = Runtime.getRuntime();
        try{
            //rt.exec("rundll32 url.dll,FileProtocolHandler "+url);
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    //add the new function need to implement
    public void add_drop(){
        // add the mouse event then we can use the new listener to implement and
        // we should create a need jbuttom for it to ship the rule we select.
        JPanel panel= new JPanel();
        JButton moveing_buttom= new JButton();
        moveing_buttom.setFocusPainted(false);
        moveing_buttom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the selected rule
            }
        });
        panel.add(moveing_buttom);

    }




    // Quick save proof to the current file with a pop window to show "successfully saved"
    private void saveProofChange(){
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
                JOptionPane.showMessageDialog(null, "Successfully Saved","Confirm",JOptionPane.INFORMATION_MESSAGE);
            }
            catch (ExportFileException e) {
                e.printStackTrace();
            }
        }

    }


    //ask to edu.rpi.legup.save current proof
    public boolean noquit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    /**
     * Sets the main content for the edu.rpi.legup.user interface
     */
    protected void setupContent() {
//        JPanel consoleBox = new JPanel(new BorderLayout());
        JPanel treeBox = new JPanel(new BorderLayout());
        JPanel ruleBox = new JPanel(new BorderLayout());

        RuleController ruleController = new RuleController();
        ruleFrame = new RuleFrame(ruleController);
        ruleBox.add(ruleFrame, BorderLayout.WEST);

        treePanel = new TreePanel();

        dynamicBoardView = new DynamicView(new ScrollView(new BoardController()), DynamicViewType.BOARD);
        TitledBorder titleBoard = BorderFactory.createTitledBorder("Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        JPanel boardPanel = new JPanel(new BorderLayout());
        topHalfPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, ruleFrame, dynamicBoardView);
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
//        consoleBox.add(treeBox);
//
//        getContentPane().add(consoleBox);

//        JPopupPanel popupPanel = new JPopupPanel();
//        setGlassPane(popupPanel);
//        popupPanel.setVisible(true);

        mainPanel.setDividerLocation(mainPanel.getMaximumDividerLocation() + 100);
//        frame.pack();
        revalidate();
    }

    private void setupToolBar() {
        setToolBarButtons(new JButton[ToolbarName.values().length]);
        for (int i = 0; i < ToolbarName.values().length; i++) {
            String toolBarName = ToolbarName.values()[i].toString();
            URL resourceLocation = ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/" + toolBarName + ".png");

            // Scale the image icons down to make the buttons smaller
            ImageIcon imageIcon = new ImageIcon(resourceLocation);
            Image image = imageIcon.getImage();
            imageIcon = new ImageIcon(image.getScaledInstance(this.TOOLBAR_ICON_SCALE, this.TOOLBAR_ICON_SCALE, Image.SCALE_SMOOTH));

            JButton button = new JButton(toolBarName, imageIcon);
            button.setFocusPainted(false);
            getToolBarButtons()[i] = button;
        }

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        for (int i = 0; i < getToolBarButtons().length; i++) {
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

//        toolBarButtons[ToolbarName.OPEN_PUZZLE.ordinal()].addActionListener((ActionEvent e) -> promptPuzzle());
//        toolBarButtons[ToolbarName.SAVE.ordinal()].addActionListener((ActionEvent e) -> saveProof());
//        toolBarButtons[ToolbarName.UNDO.ordinal()].addActionListener((ActionEvent e) -> GameBoardFacade.getInstance().getHistory().undo());
//        toolBarButtons[ToolbarName.REDO.ordinal()].addActionListener((ActionEvent e) -> GameBoardFacade.getInstance().getHistory().redo());
        toolBarButtons[ToolbarName.HINT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.CHECK.ordinal()].addActionListener((ActionEvent e) -> checkProof());
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].addActionListener((ActionEvent e) -> {
        });

        toolBarButtons[ToolbarName.CHECK_ALL.ordinal()].addActionListener((ActionEvent e) -> checkProofAll());

//        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(false);
//        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
//        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.HINT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.CHECK.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.CHECK_ALL.ordinal()].setEnabled(true);

        this.add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Sets the toolbar buttons
     *
     * @param toolBarButtons toolbar buttons
     */
    public void setToolBarButtons(JButton[] toolBarButtons) {
        this.toolBarButtons = toolBarButtons;
    }

    /**
     * Gets the toolbar buttons
     *
     * @return toolbar buttons
     */
    public JButton[] getToolBarButtons() {
        return toolBarButtons;
    }

    /**
     * Checks the proof for correctness
     */
    private void checkProof() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Board board = facade.getBoard();
        Board finalBoard = null;
        boolean delayStatus = true; //board.evalDelayStatus();

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
        }
        else {
            String message = "\nThe game board is not solved.";
            JOptionPane.showMessageDialog(null, message, "Invalid proof.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void repaintAll() {
        boardView.repaint();
        treePanel.repaint();
    }

    public void setPuzzleView(Puzzle puzzle) {
        this.boardView = puzzle.getBoardView();

        dynamicBoardView = new DynamicView(boardView, DynamicViewType.BOARD);
        this.topHalfPanel.setRightComponent(dynamicBoardView);
        this.topHalfPanel.setVisible(true);
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
//        ruleFrame.getSearchPanel().setRules(puzzle.getBasicRules());


        toolBarButtons[ToolbarName.CHECK.ordinal()].setEnabled(true);
//        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(true);

        reloadGui();
    }

    public void reloadGui() {
        repaintTree();
    }

    public void repaintTree() {
        treePanel.repaintTreeView(GameBoardFacade.getInstance().getTree());
    }

    /**
     * Checks the proof for all files
     */
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
        File preferredDirectory = new File(preferences.getUserPref(LegupPreferences.WORK_DIRECTORY));
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
            for (final File folderEntry : Objects.requireNonNull(folder.listFiles(File::isDirectory))) {
                // Write path
                String path = folderEntry.getName();
                traverseDir(folderEntry, writer, path);
            }
        }
        catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Batch grading complete.");
    }

    private boolean basicCheckProof(int[][] origCells) {
        return false;
    }

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
                    }
                    else {
                        writer.append("0,Unsolved\n");
                    }
                }
                catch (InvalidFileFormatException e) {
                    writer.append(fName).append(",Invalid,,Ungradeable\n");
                }
            }
            else {
                LOGGER.debug("Failed to run sim");
            }
        }
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public DynamicView getDynamicBoardView() {
        return dynamicBoardView;
    }

    public TreePanel getTreePanel() {
        return treePanel;
    }

    /**
     * Called when a action is pushed onto the edu.rpi.legup.history stack
     *
     * @param command action to push onto the stack
     */
    @Override
    public void onPushChange(ICommand command) {
        LOGGER.info("Pushing " + command.getClass().getSimpleName() + " to stack.");
        undo.setEnabled(true);
//        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(true);
        redo.setEnabled(false);
//        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);

        String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
        frame.setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
    }

    /**
     * Called when the history is cleared
     */
    @Override
    public void onClearHistory() {
        undo.setEnabled(false);
//        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        redo.setEnabled(false);
//        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
    }

    /**
     * Called when an action is redone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop    true if there are no more changes to redo, false otherwise
     */
    @Override
    public void onRedo(boolean isBottom, boolean isTop) {
        undo.setEnabled(!isBottom);
//        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(!isBottom);
        redo.setEnabled(!isTop);
//        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(!isTop);
        if (isBottom) {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            frame.setTitle(puzzleName + " - " + puzzleFile.getName());
        }
        else {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            frame.setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
        }
    }

    /**
     * Called when an action is undone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop    true if there are no more changes to redo, false otherwise
     */
    @Override
    public void onUndo(boolean isBottom, boolean isTop) {
        undo.setEnabled(!isBottom);
//        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(!isBottom);
        redo.setEnabled(!isTop);
//        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(!isTop);
        String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
        if (isBottom) {
            frame.setTitle(puzzleName + " - " + puzzleFile.getName());
        }
        else {
            frame.setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
        }
    }

    /**
     * Submits the proof file
     */
    private void submit() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Board board = facade.getBoard();
        boolean delayStatus = true; //board.evalDelayStatus();
        repaintAll();

        Puzzle pm = facade.getPuzzleModule();
        if (pm.isPuzzleComplete() && delayStatus) {
            // 0 means yes, 1 means no (Java's fault...)
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you wish to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                Submission submission = new Submission(board);
                submission.submit();
            }
        }
        else {
            JOptionPane.showConfirmDialog(null, "Your proof is incorrect! Are you sure you wish to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            Submission submit = new Submission(board);
        }
    }
    public void showStatus(String status, boolean error, int timer) {
        // TODO: implement
    }

    protected void fitTreeViewToScreen() {
        this.treePanel.getTreeView().zoomFit();
    }
}
