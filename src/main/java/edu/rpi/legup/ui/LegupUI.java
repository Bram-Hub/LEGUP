package edu.rpi.legup.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URL;

import javax.swing.*;

import java.util.List;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.RuleController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.ui.lookandfeel.LegupLookAndFeel;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.save.ExportFileException;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.rulesview.RuleFrame;
import edu.rpi.legup.ui.treeview.TreePanel;
import edu.rpi.legup.ui.treeview.TreeViewSelection;
import edu.rpi.legup.user.Submission;
import edu.rpi.legupupdate.Update;
import edu.rpi.legupupdate.UpdateProgress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.border.TitledBorder;

public class LegupUI extends JFrame implements WindowListener, IHistoryListener {
    private final static Logger LOGGER = LogManager.getLogger(LegupUI.class.getName());

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

    private static int CONFIG_INDEX = 0;

    protected JPanel contentPane;
    protected FileDialog fileDialog;
    protected PickGameDialog pickGameDialog;
    protected JButton[] toolBarButtons;

    protected JMenuBar mBar;

    protected JMenu file;
    protected JMenuItem newPuzzle, resetPuzzle, saveProof, preferences, exit;

    protected JMenu edit;
    protected JMenuItem undo, redo;

    protected JMenu view;

    protected JMenu proof;
    protected JMenuItem add, delete, merge, collapse;
    protected JCheckBoxMenuItem allowDefault, caseRuleGen, imdFeedback;

    protected JMenu about;
    protected JMenuItem checkUpdates, helpLegup, aboutLegup;

    protected JMenu proofMode = new JMenu("Proof Mode");
    protected JCheckBoxMenuItem[] proofModeItems = new JCheckBoxMenuItem[PROF_FLAGS.length];

    protected JMenu ai = new JMenu("AI");
    protected JMenuItem runAI = new JMenuItem("Run AI to completion");
    protected JMenuItem setpAI = new JMenuItem("Run AI one Step");
    protected JMenuItem testAI = new JMenuItem("Test AI!");
    protected JMenuItem hintAI = new JMenuItem("Hint");

    protected JMenu help;

    protected JToolBar toolBar;

    protected BoardView boardView;
    protected DynamicView dynamicBoardView;
    private RuleFrame ruleFrame;
    private TreePanel treePanel;

    protected TitledBorder boardBorder;

    protected JSplitPane topHalfPanel, mainPanel;

    /**
     * LegupUI Constructor - creates a new LegupUI to setup the menu and toolbar
     */
    public LegupUI() {
        setTitle("LEGUP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new LegupLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Not supported ui look and fel");
        }

        this.contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        setIconImage(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/Basic Rules.gif")).getImage());

        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            Graphics2D g = splash.createGraphics();
            if (g != null) {
                g.setComposite(AlphaComposite.Clear);
                g.setPaintMode();
                g.setColor(Color.BLACK);
                g.setFont(new Font(g.getFont().getName(), Font.BOLD, 24));
                g.drawString("Loading ...", 120, 350);
                splash.update();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                splash.close();
            }
        }

        setupMenu();
        setupToolBar();
        setupContent();
        if (LegupPreferences.getInstance().getUserPref(LegupPreferences.START_FULL_SCREEN).equals(Boolean.toString(true))) {
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }

        setVisible(true);

        this.addWindowListener(this);
        addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             *
             * @param e
             */
            @Override
            public void keyTyped(KeyEvent e) {
                System.err.println(e.getKeyChar());
                super.keyTyped(e);
            }
        });
        setLocationRelativeTo(null);
        setMinimumSize(getPreferredSize());
    }

    public static boolean profFlag(int flag) {
        return !((PROF_FLAGS[CONFIG_INDEX] & flag) == 0);
    }

    public void repaintTree() {
        treePanel.repaintTreeView(GameBoardFacade.getInstance().getTree());
    }

    /**
     * Sets up the menu bar
     */
    private void setupMenu() {
        mBar = new JMenuBar();
        fileDialog = new FileDialog(this);

        file = new JMenu("File");
        newPuzzle = new JMenuItem("Open");
        resetPuzzle = new JMenuItem("Reset Puzzle");
//        genPuzzle = new JMenuItem("Puzzle Generators");
        saveProof = new JMenuItem("Save Proof");
        preferences = new JMenuItem("Preferences");
        exit = new JMenuItem("Exit");

        edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        view = new JMenu("View");

        proof = new JMenu("Proof");

        add = new JMenuItem("Add");
        add.addActionListener(a -> treePanel.add());
        add.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        proof.add(add);

        delete = new JMenuItem("Delete");
        delete.addActionListener(a -> treePanel.delete());
        delete.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        proof.add(delete);

        merge = new JMenuItem("Merge");
        merge.addActionListener(a -> treePanel.merge());
        merge.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
        proof.add(merge);

        collapse = new JMenuItem("Collapse");
        collapse.addActionListener(a -> treePanel.collapse());
        collapse.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
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
        checkUpdates = new JMenuItem("Check for Updates...");
        helpLegup = new JMenuItem("Help Legup");
        aboutLegup = new JMenuItem("About Legup");

        // unused
        // help = new JMenu("Help");

        mBar.add(file);
        file.add(newPuzzle);
        newPuzzle.addActionListener((ActionEvent) -> promptPuzzle());
        newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));

//        file.add(genPuzzle);
////        genPuzzle.addActionListener((ActionEvent) ->
////        {
////            pickGameDialog = new PickGameDialog(this, true);
////            pickGameDialog.setVisible(true);
////        });
        file.add(resetPuzzle);
        resetPuzzle.addActionListener(a -> {
            Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
            if(puzzle != null) {
                Tree tree = GameBoardFacade.getInstance().getTree();
                TreeNode rootNode = tree.getRootNode();
                if(rootNode != null) {
                    int confirmReset = JOptionPane.showConfirmDialog(this, "Reset Puzzle to Root Node?", "Confirm Reset", JOptionPane.YES_NO_CANCEL_OPTION);
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
        resetPuzzle.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
        file.addSeparator();

        file.add(saveProof);
        saveProof.addActionListener((ActionEvent) -> saveProof());
        saveProof.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));

        file.add(preferences);
        preferences.addActionListener(a -> {
            PreferencesDialog preferencesDialog = new PreferencesDialog(this);
        });
        file.addSeparator();

        file.add(exit);
        exit.addActionListener((ActionEvent) -> System.exit(0));
        exit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        mBar.add(edit);


        edit.add(undo);
        undo.addActionListener((ActionEvent) ->
        {
            GameBoardFacade.getInstance().getHistory().undo();
        });
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));

        edit.add(redo);
        redo.addActionListener((ActionEvent) ->
        {
            GameBoardFacade.getInstance().getHistory().redo();
        });
        redo.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));

        mBar.add(proof);

        about.add(checkUpdates);
        checkUpdates.addActionListener(l -> {
            checkUpdates();
        });
        checkUpdates.setEnabled(false);

        about.add(aboutLegup);
        aboutLegup.addActionListener(l -> {
            JOptionPane.showMessageDialog(null, "Version: 2.0.0");
        });

        about.add(helpLegup);
        helpLegup.addActionListener(l -> {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/jpoegs/Legup2.0"));
            } catch (IOException e) {
                LOGGER.error("Can't open web page");
            }
        });

        mBar.add(about);

        setJMenuBar(mBar);
    }

    // contains all the code to setup the toolbar
    private void setupToolBar() {
        setToolBarButtons(new JButton[ToolbarName.values().length]);
        for (int i = 0; i < ToolbarName.values().length; i++) {
            String toolBarName = ToolbarName.values()[i].toString();
            URL resourceLocation = ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/" + toolBarName + ".png");
            JButton button = new JButton(toolBarName, new ImageIcon(resourceLocation));
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

        toolBarButtons[ToolbarName.OPEN_PUZZLE.ordinal()].addActionListener((ActionEvent e) -> promptPuzzle());
        toolBarButtons[ToolbarName.SAVE.ordinal()].addActionListener((ActionEvent e) -> saveProof());
        toolBarButtons[ToolbarName.UNDO.ordinal()].addActionListener((ActionEvent e) -> GameBoardFacade.getInstance().getHistory().undo());
        toolBarButtons[ToolbarName.REDO.ordinal()].addActionListener((ActionEvent e) -> GameBoardFacade.getInstance().getHistory().redo());
        toolBarButtons[ToolbarName.HINT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.CHECK.ordinal()].addActionListener((ActionEvent e) -> checkProof());
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].addActionListener((ActionEvent e) -> {
        });

        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.HINT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.CHECK.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].setEnabled(false);

        contentPane.add(toolBar, BorderLayout.NORTH);
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

        treePanel = new TreePanel(this);

        dynamicBoardView = new DynamicView(new ScrollView(new BoardController()));
        TitledBorder titleBoard = BorderFactory.createTitledBorder("Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        JPanel boardPanel = new JPanel(new BorderLayout());
        topHalfPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, ruleFrame, dynamicBoardView);
        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topHalfPanel, treePanel);
        topHalfPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.setPreferredSize(new Dimension(600, 600));

        boardPanel.add(mainPanel);
        boardBorder = BorderFactory.createTitledBorder("Board");
        boardBorder.setTitleJustification(TitledBorder.CENTER);

        ruleBox.add(boardPanel);
        treeBox.add(ruleBox);
        contentPane.add(treeBox);
//        consoleBox.add(treeBox);
//
//        getContentPane().add(consoleBox);

//        JPopupPanel popupPanel = new JPopupPanel();
//        setGlassPane(popupPanel);
//        popupPanel.setVisible(true);

        mainPanel.setDividerLocation(mainPanel.getMaximumDividerLocation() + 100);
        pack();
        revalidate();
    }

    /**
     * Saves a proof
     */
    private void saveProof() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
        }

        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setTitle("Save Proof");
        String curFileName = GameBoardFacade.getInstance().getCurFileName();
        if (curFileName == null) {
            fileDialog.setDirectory(LegupPreferences.getInstance().getUserPref(LegupPreferences.WORK_DIRECTORY));
        } else {
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
            } catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
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
            int confirm = JOptionPane.showConfirmDialog(null, "Congratulations! Your proof is correct. Would you like to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Submission submission = new Submission(board);
                submission.submit();
            }
            showStatus("Your proof is correct.", false);
        } else {
            String message = "\nThe game board is not solved.";
            JOptionPane.showMessageDialog(null, message, "Invalid proof.", JOptionPane.ERROR_MESSAGE);

            showStatus(message, true);
        }
    }

    private boolean basicCheckProof(int[][] origCells) {
        return false;
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
        } else {
            JOptionPane.showConfirmDialog(null, "Your proof is incorrect! Are you sure you wish to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            Submission submit = new Submission(board);
        }
    }

    private void directions() {
        JOptionPane.showMessageDialog(null, "For ever move you make, you must provide a rules for it (located in the Rules panel).\n" + "While working on the edu.rpi.legup.puzzle, you may click on the \"Check\" button to test your proof for correctness.", "Directions", JOptionPane.PLAIN_MESSAGE);
    }

    private void showAll() {
        getToolBarButtons()[ToolbarName.SAVE.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.UNDO.ordinal()].setEnabled(false);
        getToolBarButtons()[ToolbarName.REDO.ordinal()].setEnabled(false);
        getToolBarButtons()[ToolbarName.HINT.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.CHECK.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.SUBMIT.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.DIRECTIONS.ordinal()].setEnabled(true);

        pack();
    }

    private void repaintAll() {
        boardView.repaint();
        treePanel.repaint();
    }

    public void showStatus(String status, boolean error) {
        showStatus(status, error, 1);
    }

    public void errorEncountered(String error) {
        JOptionPane.showMessageDialog(null, error);
    }

    public void reloadGui() {
        repaintTree();
    }

    public void promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noquit("Opening a new puzzle?")) // !noquit or noquit?
            {
                return;
            }
        }

        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setTitle("Select Puzzle");
        fileDialog.setVisible(true);
        String fileName = null;
        File puzzleFile = null;
        if (fileDialog.getDirectory() != null && fileDialog.getFile() != null) {
            fileName = fileDialog.getDirectory() + File.separator + fileDialog.getFile();
            puzzleFile = new File(fileName);
        }

        if (puzzleFile != null && puzzleFile.exists()) {
            try {
                GameBoardFacade.getInstance().loadPuzzle(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                setTitle(puzzleName + " - " + puzzleFile.getName());
            } catch (InvalidFileFormatException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public void showStatus(String status, boolean error, int timer) {

    }

    public void checkUpdates() {
        String updateStr = null;
        File jarPath = null;
        try {
            jarPath = new File(LegupUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (Exception e) {
            updateStr = "An error occurred while attempting to update Legup...";
            JOptionPane.showMessageDialog(this, updateStr, "Update Legup", JOptionPane.ERROR_MESSAGE);
        }
        Update update = new Update(Update.Stream.CLIENT, jarPath);

        boolean isUpdateAvailable = update.checkUpdate();
        int ans = 0;
        if (isUpdateAvailable) {
            updateStr = "There is update available. Do you want to update?";
            ans = JOptionPane.showConfirmDialog(this, updateStr, "Update Legup", JOptionPane.OK_CANCEL_OPTION);
        } else {
            updateStr = "There is no update available at this time. Check again later!";
            JOptionPane.showMessageDialog(this, updateStr, "Update Legup", JOptionPane.INFORMATION_MESSAGE);
        }

        if (ans == JOptionPane.OK_OPTION && isUpdateAvailable) {
            LOGGER.info("Updating Legup....");

            new Thread(() -> {
                JDialog updateDialog = new JDialog(this, "Updating Legup...", true);
                JProgressBar dpb = new JProgressBar(0, 500);
                updateDialog.add(BorderLayout.CENTER, dpb);
                updateDialog.add(BorderLayout.NORTH, new JLabel("Progress..."));
                updateDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                updateDialog.setSize(300, 75);
                updateDialog.setResizable(false);
                updateDialog.setLocationRelativeTo(this);
//                updateDialog.setVisible(true);
                update.setUpdateProgress(new UpdateProgress() {
                    double total = 0;

                    @Override
                    public void setTotalDownloads(double total) {
                        this.total = total;
                        dpb.setString("0 - " + total);
                    }

                    @Override
                    public void setCurrentDownload(double current) {
                        dpb.setValue((int) (current / total * 100));
                        dpb.setString(current + " - " + total);
                    }

                    @Override
                    public void setDescription(String description) {

                    }
                });
                update.update();
            }).run();
        } else {
//            if (SystemUtils.IS_OS_WINDOWS) {
//                File java = new File(SystemUtils.JAVA_HOME);
//                java = new File(java, "bin");
//                java = new File(java, "javaw.exe");
//                String javaPath = java.getPath();
//                String legupPath = LegupUI.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//                try {
//                    LOGGER.severe("starting new legup");
//                    Process process = Runtime.getRuntime().exec(new String[]{javaPath, "-jar", legupPath});
//                    process.waitFor();
//                    System.out.println(new String(process.getInputStream().readAllBytes()));
//                    System.err.println(new String(process.getErrorStream().readAllBytes()));
//
//                } catch (InterruptedException | IOException e) {
//                    LOGGER.severe("interrupted or io");
//                }
//            }
        }
    }

    //ask to edu.rpi.legup.save current proof
    public boolean noquit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            return false;
        }
        return true;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        if (GameBoardFacade.getInstance().getHistory().getIndex() > -1) {
            if (noquit("Exiting LEGUP?")) {
                this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            } else {
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        } else {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

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
     * Sets the toolbar buttons
     *
     * @param toolBarButtons toolbar buttons
     */
    public void setToolBarButtons(JButton[] toolBarButtons) {
        this.toolBarButtons = toolBarButtons;
    }

    public void setPuzzleView(Puzzle puzzle) {
        this.boardView = puzzle.getBoardView();

        dynamicBoardView = new DynamicView(boardView);
        this.topHalfPanel.setRightComponent(dynamicBoardView);
        this.topHalfPanel.setVisible(true);

        TitledBorder titleBoard = BorderFactory.createTitledBorder(boardView.getClass().getSimpleName());
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        this.treePanel.getTreeView().resetView();
        this.treePanel.getTreeView().setTree(puzzle.getTree());

        puzzle.addTreeListener(treePanel.getTreeView());
        puzzle.addBoardListener(puzzle.getBoardView());

        ruleFrame.getBasicRulePanel().setRules(puzzle.getBasicRules());
        ruleFrame.getCasePanel().setRules(puzzle.getCaseRules());
        ruleFrame.getContradictionPanel().setRules(puzzle.getContradictionRules());

        toolBarButtons[ToolbarName.CHECK.ordinal()].setEnabled(true);
        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(true);

        reloadGui();
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
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(true);
        redo.setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);

        String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
        setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
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
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(!isBottom);
        redo.setEnabled(!isTop);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(!isTop);
        if (isBottom) {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            setTitle(puzzleName + " - " + puzzleFile.getName());
        } else {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
        }
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
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(!isBottom);
        redo.setEnabled(!isTop);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(!isTop);
        if (isBottom) {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            setTitle(puzzleName + " - " + puzzleFile.getName());
        } else {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
            setTitle(puzzleName + " - " + puzzleFile.getName() + " *");
        }
    }

    /**
     * Called when the history is cleared
     */
    @Override
    public void onClearHistory() {
        undo.setEnabled(false);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        redo.setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
    }
}
