package edu.rpi.legup.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.RuleController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
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
import edu.rpi.legup.user.Submission;
import edu.rpi.legupupdate.Update;

import javax.swing.border.TitledBorder;

public class LegupUI extends JFrame implements WindowListener, IHistoryListener
{
    private final static Logger LOGGER = Logger.getLogger(LegupUI.class.getName());

    public static final int ALLOW_HINTS = 1;
    public static final int ALLOW_DEFAPP = 2;
    public static final int ALLOW_FULLAI = 4;
    public static final int ALLOW_JUST = 8;
    public static final int REQ_STEP_JUST = 16;
    public static final int IMD_FEEDBACK = 32;
    public static final int INTERN_RO = 64;
    public static final int AUTO_JUST = 128;

    final static int[] TOOLBAR_SEPARATOR_BEFORE = {3, 5, 9};
    private static final String[] PROFILES = {"No Assistance", "Rigorous Proof", "Casual Proof", "Assisted Proof", "Guided Proof", "Training-Wheels Proof", "No Restrictions"};
    private static final int[] PROF_FLAGS = {0, ALLOW_JUST | REQ_STEP_JUST, ALLOW_JUST, ALLOW_HINTS | ALLOW_JUST | AUTO_JUST, ALLOW_HINTS | ALLOW_JUST | REQ_STEP_JUST, ALLOW_HINTS | ALLOW_DEFAPP | ALLOW_JUST | IMD_FEEDBACK | INTERN_RO, ALLOW_HINTS | ALLOW_DEFAPP | ALLOW_FULLAI | ALLOW_JUST};

    private static int CONFIG_INDEX = 0;

    protected JPanel contentPane;
    protected FileDialog fileChooser;
    protected PickGameDialog pickGameDialog;
    protected JButton[] toolBarButtons;

    protected JMenuBar mBar;

    protected JMenu file;
    protected JMenuItem newPuzzle, genPuzzle, openProof, saveProof, instructorCheck, preferences, exit;

    protected JMenu edit;
    protected JMenuItem undo, redo;

    protected JMenu view;

    protected JMenu proof;
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
    private RuleFrame ruleFrame;
    private TreePanel treePanel;

    protected TitledBorder boardBorder;

    protected JSplitPane topHalfPanel, mainPanel;

    /**
     * LegupUI Constructor - creates a new LegupUI to setup the menu and toolbar
     */
    public LegupUI()
    {
        setTitle("LEGUP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new LegupLookAndFeel());
        } catch(UnsupportedLookAndFeelException e) {
            System.err.println("Not supported ui look and fel");
        }

        this.contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        setIconImage(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/Basic Rules.gif")).getImage());

        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null)
        {
            Graphics2D g = splash.createGraphics();
            if (g != null)
            {
                g.setComposite(AlphaComposite.Clear);
                g.setPaintMode();
                g.setColor(Color.BLACK);
                g.setFont(new Font(g.getFont().getName(), Font.BOLD, 24));
                g.drawString("Loading ...", 120, 350);
                splash.update();
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException e)
                {

                }
                splash.close();
            }
        }

        setupMenu();
        setupToolBar();
        setupContent();
        if(LegupPreferences.getInstance().getUserPref(LegupPreferences.START_FULL_SCREEN).equals(Boolean.toString(true))) {
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

    public static boolean profFlag(int flag)
    {
        return !((PROF_FLAGS[CONFIG_INDEX] & flag) == 0);
    }

    public void repaintTree()
    {
        treePanel.repaintTreeView(GameBoardFacade.getInstance().getTree());
    }

    /**
     * Sets up the menu bar
     */
    private void setupMenu()
    {
        mBar = new JMenuBar();
        fileChooser = new FileDialog(this);

        file = new JMenu("File");
        newPuzzle = new JMenuItem("New");
        genPuzzle = new JMenuItem("Puzzle Generators");
        openProof = new JMenuItem("Open");
        saveProof = new JMenuItem("Save Proof");
        instructorCheck = new JMenuItem("Instructor Check");
        preferences = new JMenuItem("Preferences");
        exit = new JMenuItem("Exit");

        edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        view = new JMenu("View");

        proof = new JMenu("Proof");
        allowDefault = new JCheckBoxMenuItem("Allow Default Rule Applications", false);
        caseRuleGen = new JCheckBoxMenuItem("Automatically generate cases for CaseRule", false);
        imdFeedback = new JCheckBoxMenuItem("Provide immediate feedback", false);

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

        file.add(genPuzzle);
        genPuzzle.addActionListener((ActionEvent) ->
        {
            pickGameDialog = new PickGameDialog(this, true);
            pickGameDialog.setVisible(true);
        });
        file.addSeparator();

        file.add(openProof);
        openProof.addActionListener((ActionEvent) -> openProof());
        openProof.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));

        file.add(saveProof);
        saveProof.addActionListener((ActionEvent) -> saveProof());
        saveProof.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));

        file.add(instructorCheck);
        instructorCheck.addActionListener((ActionEvent) -> instructorCheck());

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
        proof.add(allowDefault);
        allowDefault.addActionListener((ActionEvent) ->
        {

        });

        proof.add(caseRuleGen);
        caseRuleGen.addActionListener((ActionEvent) ->
        {

        });
        caseRuleGen.setState(true);

        proof.add(imdFeedback);
        imdFeedback.addActionListener((ActionEvent) ->
        {

        });
        imdFeedback.setState(true);

        about.add(checkUpdates);
        checkUpdates.addActionListener( l -> {
            checkUpdates();
        });

        about.add(aboutLegup);
        aboutLegup.addActionListener(l-> {
            JOptionPane.showMessageDialog(null, "Version: 2.0.0");
        });

        about.add(helpLegup);
        helpLegup.addActionListener(l-> {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/jpoegs/Legup2.0"));
            } catch(IOException e) {
                LOGGER.log(Level.SEVERE, "Can't open web page");
            }
        });

        mBar.add(about);

        setJMenuBar(mBar);
    }

    // contains all the code to setup the toolbar
    private void setupToolBar()
    {
        setToolBarButtons(new JButton[ToolbarName.values().length]);
        for(int i = 0; i < ToolbarName.values().length; i++)
        {
            String toolBarName = ToolbarName.values()[i].toString();
            URL resourceLocation = ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/Legup/" + toolBarName + ".png");
            JButton button = new JButton(toolBarName, new ImageIcon(resourceLocation));
            button.setFocusPainted(false);
            getToolBarButtons()[i] = button;
        }

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        for(int i = 0; i < getToolBarButtons().length; i++)
        {
            for(int s = 0; s < TOOLBAR_SEPARATOR_BEFORE.length; s++)
            {
                if(i == TOOLBAR_SEPARATOR_BEFORE[s])
                {
                    toolBar.addSeparator();
                }
            }
            String toolBarName = ToolbarName.values()[i].toString();

            toolBar.add(getToolBarButtons()[i]);
            getToolBarButtons()[i].setToolTipText(toolBarName);

            getToolBarButtons()[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            getToolBarButtons()[i].setHorizontalTextPosition(SwingConstants.CENTER);
        }

        toolBarButtons[ToolbarName.OPEN_PUZZLE.ordinal()].addActionListener((ActionEvent e)  -> promptPuzzle());
        toolBarButtons[ToolbarName.OPEN_PROOF.ordinal()].addActionListener((ActionEvent e)  -> openProof());
        toolBarButtons[ToolbarName.SAVE.ordinal()].addActionListener((ActionEvent e)  -> saveProof());
        toolBarButtons[ToolbarName.UNDO.ordinal()].addActionListener((ActionEvent e)  -> GameBoardFacade.getInstance().getHistory().undo());
        toolBarButtons[ToolbarName.REDO.ordinal()].addActionListener((ActionEvent e)  -> GameBoardFacade.getInstance().getHistory().redo());
        toolBarButtons[ToolbarName.HINT.ordinal()].addActionListener((ActionEvent e)  -> {});
        toolBarButtons[ToolbarName.CHECK.ordinal()].addActionListener((ActionEvent e)  -> checkProof());
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].addActionListener((ActionEvent e)  -> {});
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].addActionListener((ActionEvent e)  -> {});
        toolBarButtons[ToolbarName.ANNOTATIONS.ordinal()].addActionListener((ActionEvent e)  -> {});

        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.HINT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.CHECK.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].setEnabled(false);
        getToolBarButtons()[ToolbarName.ANNOTATIONS.ordinal()].setEnabled(false);

        contentPane.add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Sets the main content for the edu.rpi.legup.user interface
     */
    protected void setupContent()
    {
//        JPanel consoleBox = new JPanel(new BorderLayout());
        JPanel treeBox = new JPanel(new BorderLayout());
        JPanel ruleBox = new JPanel(new BorderLayout());

        RuleController ruleController = new RuleController();
        ruleFrame = new RuleFrame(ruleController);
        ruleBox.add(ruleFrame, BorderLayout.WEST );

        treePanel = new TreePanel(this);

        DynamicView emptyBoard = new DynamicView(new ScrollView(new BoardController()));
        TitledBorder titleBoard = BorderFactory.createTitledBorder("Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        emptyBoard.setBorder(titleBoard);

        JPanel boardPanel = new JPanel(new BorderLayout());
        topHalfPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, ruleFrame, emptyBoard);
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
     * Opens the file chooser to open a proof file
     */
    private void openProof()
    {
        if(GameBoardFacade.getInstance().getBoard() != null)
        {
            if(noquit("Opening a new proof?"))
            {
                return;
            }
        }
        fileChooser.setMode(FileDialog.LOAD);
        fileChooser.setTitle("Select Proof");
        fileChooser.setVisible(true);

        String filename = fileChooser.getFile();

        if(filename != null)
        {
            filename = fileChooser.getDirectory() + filename;
            if(!filename.toLowerCase().endsWith(".proof"))
            {
                JOptionPane.showMessageDialog(null, "File selected does not have the suffix \".proof\".");
                return;
            }
            GameBoardFacade.getInstance().loadProofFile(filename);
        }
    }

    /**
     * Saves a proof
     */
    private void saveProof()
    {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if(puzzle == null)
        {
            return;
        }

        fileChooser.setMode(FileDialog.SAVE);
        fileChooser.setTitle("Select Proof");
        fileChooser.setVisible(true);

        String filename = fileChooser.getFile();

        if(filename != null)
        {
            filename = fileChooser.getDirectory() + filename;

            //facade.setWindowTitle(filename.substring(0, filename.length() - 6), facade.getPuzzleModule().getName());
            try
            {
                PuzzleExporter exporter = puzzle.getExporter();
                if(exporter == null)
                {
                    throw new ExportFileException("Puzzle exporter null");
                }
                exporter.exportPuzzle(filename);
            }
            catch(ExportFileException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks the proof for correctness
     */
    private void checkProof()
    {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Board board = facade.getBoard();
        Board finalBoard = null;
        boolean delayStatus = true; //board.evalDelayStatus();

        repaintAll();

        Puzzle puzzle = facade.getPuzzleModule();

        if(puzzle.isPuzzleComplete())
        {
            int confirm = JOptionPane.showConfirmDialog(null, "Congratulations! Your proof is correct. Would you like to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION)
            {
                Submission submission = new Submission(board);
                submission.submit();
            }
            showStatus("Your proof is correct.", false);
        }
        else
        {
            String message = "\nThe game board is not solved.";
            JOptionPane.showMessageDialog(null, message, "Invalid proof.", JOptionPane.ERROR_MESSAGE);

            showStatus(message, true);
        }
    }

    private boolean basicCheckProof(int[][] origCells)
    {
        return false;
    }

    /**
     * Instructor checks
     */
    private void instructorCheck()
    {
        promptPuzzle();
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Board board = facade.getInstance().getBoard();

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose directory with submissions");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setVisible(true);

        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File dir = new File(chooser.getSelectedFile().toString());
            File[] directoryListing = dir.listFiles();
            if(directoryListing != null)
            {
                String results = "";
                for(File child : directoryListing)
                {
                    if(!child.toString().toLowerCase().endsWith(".proof"))
                    {
                        JOptionPane.showMessageDialog(null, "File selected does not have the suffix \".proof\".");
                        return;
                    }
                    facade.loadProofFile(child.toString());
                    if(basicCheckProof(null))
                    {
                        results += child.toString() + ": Correct!\n";
                    }
                    else
                    {
                        results += child.toString() + ": Incorrect\n";
                    }
                }

                fileChooser.setMode(FileDialog.SAVE);
                fileChooser.setTitle("Select Proof");
                fileChooser.setVisible(true);
                String filename = fileChooser.getFile();
                if(filename != null) // edu.rpi.legup.user didn't pressed cancel
                {
                    String savePath = fileChooser.getDirectory() + filename;
                    try(PrintStream ps = new PrintStream(savePath))
                    {
                        ps.println(results);
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println("Can't find file");
                    }
                }
            }
        }
        else
        {
            System.out.println("No Selection");
        }
    }

    /**
     * Submits the proof file
     */
    private void submit()
    {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        Board board = facade.getBoard();
        boolean delayStatus = true; //board.evalDelayStatus();
        repaintAll();

        Puzzle pm = facade.getPuzzleModule();
        if(pm.isPuzzleComplete() && delayStatus)
        {
            // 0 means yes, 1 means no (Java's fault...)
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you wish to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            if(confirm == 0)
            {
                Submission submission = new Submission(board);
                submission.submit();
            }
        }
        else
        {
            JOptionPane.showConfirmDialog(null, "Your proof is incorrect! Are you sure you wish to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            Submission submit = new Submission(board);
        }
    }

    private void directions()
    {
        JOptionPane.showMessageDialog(null, "For ever move you make, you must provide a rules for it (located in the Rules panel).\n" + "While working on the edu.rpi.legup.puzzle, you may click on the \"Check\" button to test your proof for correctness.", "Directions", JOptionPane.PLAIN_MESSAGE);
    }

    private void showAll()
    {
        getToolBarButtons()[ToolbarName.SAVE.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.UNDO.ordinal()].setEnabled(false);
        getToolBarButtons()[ToolbarName.REDO.ordinal()].setEnabled(false);
        getToolBarButtons()[ToolbarName.HINT.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.CHECK.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.SUBMIT.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.DIRECTIONS.ordinal()].setEnabled(true);
        getToolBarButtons()[ToolbarName.ANNOTATIONS.ordinal()].setEnabled(true);

        pack();
    }

    private void repaintAll()
    {
        boardView.repaint();
        treePanel.repaint();
    }

    public void showStatus(String status, boolean error)
    {
        showStatus(status, error, 1);
    }

    public void errorEncountered(String error)
    {
        JOptionPane.showMessageDialog(null, error);
    }

    public void reloadGui()
    {
        repaintTree();
    }

    public void promptPuzzle()
    {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if(facade.getBoard() != null)
        {
            if(noquit("Opening a new puzzle?")) // !noquit or noquit?
            {
                return;
            }
        }

        fileChooser.setMode(FileDialog.LOAD);
        fileChooser.setTitle("Select Puzzle");
        fileChooser.setVisible(true);
        String filename = fileChooser.getFile();

        if (filename != null)
        {
            filename = fileChooser.getDirectory() + filename;
            try
            {
                GameBoardFacade.getInstance().loadBoardFile(filename);
            }
            catch(InvalidFileFormatException e)
            {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            //GameBoardFacade.getInstance().setPuzzle(new Sudoku());
        }


        /*JFileChooser newPuzzle = new JFileChooser("puzzlefiles");
        FileNameExtensionFilter fileType = new FileNameExtensionFilter("LEGUP Puzzles", "xml");
        newPuzzle.setFileFilter(fileType);
        if(newPuzzle.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            facade.loadBoardFile(newPuzzle.getSelectedFile().getAbsolutePath());
            Puzzle edu.rpi.legup.puzzle = facade.getPuzzleModule();
            if(edu.rpi.legup.puzzle != null)
            {
                //getJustificationFrame().setJustifications(edu.rpi.legup.puzzle);
                // AI setup
                //myAI.setBoard(edu.rpi.legup.puzzle);
            }
            // show them all
            showAll();
        }
        else
        {
            //System.out.println("Cancel Pressed");
        }
        */
    }

    public void showStatus(String status, boolean error, int timer)
    {

    }

    public void checkUpdates()
    {
        String updateStr = null;
        File jarPath = null;
        try {
            jarPath = new File(LegupUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch(Exception e)
        {
            updateStr = "An error occurred while attempting to update Legup...";
            JOptionPane.showMessageDialog(this, updateStr, "Update Legup", JOptionPane.ERROR_MESSAGE);
        }
        Update update = new Update(Update.Stream.CLIENT, jarPath);

        boolean isUpdateAvailable = update.checkUpdate();
        int ans = 0;
        if(isUpdateAvailable)
        {
            updateStr = "There is update available. Do you want to update?";
            ans = JOptionPane.showConfirmDialog(this, updateStr, "Update Legup", JOptionPane.OK_CANCEL_OPTION);
        }
        else
        {
            updateStr = "There is no update available at this time. Check again later!";
            JOptionPane.showMessageDialog(this, updateStr, "Update Legup", JOptionPane.INFORMATION_MESSAGE);
        }

        if(ans == JOptionPane.OK_OPTION && isUpdateAvailable)
        {
            LOGGER.log(Level.INFO, "Updating Legup....");
            update.update();
        }
    }

    //ask to edu.rpi.legup.save current proof
    public boolean noquit(String instr)
    {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        if(n == JOptionPane.YES_OPTION)
        {
            return false;
        }
        return true;
    }

    public void resetUndoRedo()
    {

    }

    @Override
    public void windowOpened(WindowEvent e)
    {

    }

    public void windowClosing(WindowEvent e)
    {
        if(GameBoardFacade.getInstance().getHistory().getIndex() > -1)
        {
            if(noquit("Exiting LEGUP?"))
            {
                this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }
            else
            {
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
        else
        {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    public void windowClosed(WindowEvent e)
    {
        System.exit(0);
    }

    public void windowIconified(WindowEvent e)
    {

    }

    public void windowDeiconified(WindowEvent e)
    {

    }

    public void windowActivated(WindowEvent e)
    {

    }

    public void windowDeactivated(WindowEvent e)
    {

    }

    /**
     * Gets the toolbar buttons
     *
     * @return toolbar buttons
     */
    public JButton[] getToolBarButtons()
    {
        return toolBarButtons;
    }

    /**
     * Sets the toolbar buttons
     *
     * @param toolBarButtons toolbar buttons
     */
    public void setToolBarButtons(JButton[] toolBarButtons)
    {
        this.toolBarButtons = toolBarButtons;
    }

    public void setPuzzleView(Puzzle puzzle)
    {
        this.boardView = puzzle.getBoardView();

        DynamicView dynamicView = new DynamicView(boardView);
        this.topHalfPanel.setRightComponent(dynamicView);
        this.topHalfPanel.setVisible(true);

        TitledBorder titleBoard = BorderFactory.createTitledBorder(boardView.getClass().getSimpleName());
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicView.setBorder(titleBoard);

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

    public BoardView getBoardView()
    {
        return boardView;
    }

    public TreePanel getTreePanel()
    {
        return treePanel;
    }

    public RuleFrame getRuleFrame()
    {
        return ruleFrame;
    }

    public JMenuBar getmBar()
    {
        return mBar;
    }

    public JToolBar getToolBar()
    {
        return toolBar;
    }

    public JMenuItem getUndo()
    {
        return undo;
    }

    public JMenuItem getRedo()
    {
        return redo;
    }

    public JButton getUndoButton()
    {
        return toolBarButtons[ToolbarName.UNDO.ordinal()];
    }

    public JButton getRedoButton()
    {
        return toolBarButtons[ToolbarName.REDO.ordinal()];
    }

    /**
     * Called when a action is pushed onto the edu.rpi.legup.history stack
     *
     * @param command action to push onto the stack
     */
    @Override
    public void onPushChange(ICommand command)
    {
        LOGGER.log(Level.SEVERE, "onPushChange");
        undo.setEnabled(true);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(true);
        redo.setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
    }

    /**
     * Called when an action is undone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop true if there are no more changes to redo, false otherwise
     */
    @Override
    public void onUndo(boolean isBottom, boolean isTop)
    {
        undo.setEnabled(!isBottom);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(!isBottom);
        redo.setEnabled(!isTop);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(!isTop);
    }

    /**
     * Called when an action is redone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop true if there are no more changes to redo, false otherwise
     */
    @Override
    public void onRedo(boolean isBottom, boolean isTop)
    {
        undo.setEnabled(!isBottom);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(!isBottom);
        redo.setEnabled(!isTop);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(!isTop);
    }

    /**
     * Called when the edu.rpi.legup.history is cleared
     */
    @Override
    public void onClearHistory()
    {
        undo.setEnabled(false);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        redo.setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
    }
}
