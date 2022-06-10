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
import edu.rpi.legup.ui.puzzleeditorui.rulesview.RuleFrame;
import edu.rpi.legup.ui.puzzleeditorui.treeview.TreePanel;
import edu.rpi.legup.ui.puzzleeditorui.treeview.TreeViewSelection;
import edu.rpi.legup.user.Submission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class PuzzleSolverPanel extends LegupPanel implements IHistoryListener {
    private final static Logger LOGGER = LogManager.getLogger(PuzzleSolverPanel.class.getName());
    private JMenuBar mBar;
    private TreePanel treePanel;
    private FileDialog fileDialog;
    private JFrame frame;
    private RuleFrame ruleFrame;
    private DynamicView dynamicBoardView;
    private JSplitPane topHalfPanel, mainPanel;
    private TitledBorder boardBorder;
    private JButton[] toolBarButtons;
    private JToolBar toolBar;
    private BoardView boardView;
    private JFileChooser folderBrowser;
    private JMenuItem undo, redo;

    final static int[] TOOLBAR_SEPARATOR_BEFORE = {2, 4, 8};

    public PuzzleSolverPanel(FileDialog fileDialog, JFrame frame) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        setLayout(new BorderLayout());
    }

    @Override
    public void makeVisible() {
        setupToolBar();
        setupContent();
        frame.setJMenuBar(getMenuBar());
    }

    public JMenuBar getMenuBar() {
        mBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem newPuzzle = new JMenuItem("Open");
        JMenuItem resetPuzzle = new JMenuItem("Reset Puzzle");
//        genPuzzle = new JMenuItem("Puzzle Generators");
        JMenuItem saveProof = new JMenuItem("Save Proof");
        JMenuItem preferences = new JMenuItem("Preferences");
        JMenuItem exit = new JMenuItem("Exit");

        JMenu edit = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        JMenu view = new JMenu("View");

        JMenu proof = new JMenu("Proof");

        String os = LegupUI.getOS();

        JMenuItem add = new JMenuItem("Add");
        add.addActionListener(a -> treePanel.add());
        if(os.equals("mac")) add.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else add.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        proof.add(add);

        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(a -> treePanel.delete());
        if(os.equals("mac")) delete.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else delete.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        proof.add(delete);

        JMenuItem merge = new JMenuItem("Merge");
        merge.addActionListener(a -> treePanel.merge());
        if(os.equals("mac")) merge.setAccelerator(KeyStroke.getKeyStroke('M', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else merge.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
        proof.add(merge);

        JMenuItem collapse = new JMenuItem("Collapse");
        collapse.addActionListener(a -> treePanel.collapse());
        if(os.equals("mac")) collapse.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else collapse.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        collapse.setEnabled(false);
        proof.add(collapse);

        JCheckBoxMenuItem allowDefault = new JCheckBoxMenuItem("Allow Default Rule Applications",
                LegupPreferences.getInstance().getUserPref(LegupPreferences.ALLOW_DEFAULT_RULES).equalsIgnoreCase(Boolean.toString(true)));
        allowDefault.addChangeListener(e -> {
            LegupPreferences.getInstance().setUserPref(LegupPreferences.ALLOW_DEFAULT_RULES, Boolean.toString(allowDefault.isSelected()));
        });
        proof.add(allowDefault);

        JCheckBoxMenuItem caseRuleGen = new JCheckBoxMenuItem("Automatically generate cases for CaseRule",
                LegupPreferences.getInstance().getUserPref(LegupPreferences.AUTO_GENERATE_CASES).equalsIgnoreCase(Boolean.toString(true)));
        caseRuleGen.addChangeListener(e -> {
            LegupPreferences.getInstance().setUserPref(LegupPreferences.AUTO_GENERATE_CASES, Boolean.toString(caseRuleGen.isSelected()));
        });
        proof.add(caseRuleGen);

        JCheckBoxMenuItem imdFeedback = new JCheckBoxMenuItem("Provide immediate feedback",
                LegupPreferences.getInstance().getUserPref(LegupPreferences.IMMEDIATE_FEEDBACK).equalsIgnoreCase(Boolean.toString(true)));
        imdFeedback.addChangeListener(e -> {
            LegupPreferences.getInstance().setUserPref(LegupPreferences.IMMEDIATE_FEEDBACK, Boolean.toString(imdFeedback.isSelected()));
        });
        proof.add(imdFeedback);

        JMenu about = new JMenu("About");
        JMenuItem checkUpdates = new JMenuItem("Check for Updates...");
        JMenuItem helpLegup = new JMenuItem("Help Legup");
        JMenuItem aboutLegup = new JMenuItem("About Legup");

        // unused
        // help = new JMenu("Help");

        mBar.add(file);
        file.add(newPuzzle);
        newPuzzle.addActionListener((ActionEvent) -> promptPuzzle());
        if(os.equals("mac")) newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));

//        file.add(genPuzzle);
////        genPuzzle.addActionListener((ActionEvent) ->
////        {
////            pickGameDialog = new PickGameDialog(this, true);
////            pickGameDialog.setVisible(true);
////        });
        file.add(resetPuzzle);
        resetPuzzle.addActionListener(a -> {
            Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
            if (puzzle != null) {
                Tree tree = GameBoardFacade.getInstance().getTree();
                TreeNode rootNode = tree.getRootNode();
                if (rootNode != null) {
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
        if(os.equals("mac")) resetPuzzle.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else resetPuzzle.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
        file.addSeparator();

        file.add(saveProof);
        saveProof.addActionListener((ActionEvent) -> saveProof());
        if(os.equals("mac")) saveProof.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else saveProof.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));

        file.add(preferences);
//        preferences.addActionListener(a -> {
//            PreferencesDialog preferencesDialog = new PreferencesDialog(this);
//        });
        file.addSeparator();

        file.add(exit);
        exit.addActionListener((ActionEvent) -> System.exit(0));
        if(os.equals("mac")) exit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else exit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        mBar.add(edit);


        edit.add(undo);
        undo.addActionListener((ActionEvent) ->
        {
            GameBoardFacade.getInstance().getHistory().undo();
        });
        if(os.equals("mac")) undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));

        edit.add(redo);
        redo.addActionListener((ActionEvent) ->
        {
            GameBoardFacade.getInstance().getHistory().redo();
        });
        if(os.equals("mac")) redo.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else redo.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));

        mBar.add(proof);

        about.add(checkUpdates);
        checkUpdates.addActionListener(l -> {
            //checkUpdates();
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

        return mBar;
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
                frame.setTitle(puzzleName + " - " + puzzleFile.getName());
            } catch (InvalidFileFormatException e) {
                LOGGER.error(e.getMessage());
            }
        }
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

    public boolean noquit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            return false;
        }
        return true;
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
        this.add(treeBox);
//        consoleBox.add(treeBox);
//
//        getContentPane().add(consoleBox);

//        JPopupPanel popupPanel = new JPopupPanel();
//        setGlassPane(popupPanel);
//        popupPanel.setVisible(true);

        mainPanel.setDividerLocation(mainPanel.getMaximumDividerLocation() + 100);
        //frame.pack();
        revalidate();
    }

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
        //toolBarButtons[ToolbarName.SAVE.ordinal()].addActionListener((ActionEvent e) -> saveProof());
        toolBarButtons[ToolbarName.UNDO.ordinal()].addActionListener((ActionEvent e) -> GameBoardFacade.getInstance().getHistory().undo());
        toolBarButtons[ToolbarName.REDO.ordinal()].addActionListener((ActionEvent e) -> GameBoardFacade.getInstance().getHistory().redo());
        toolBarButtons[ToolbarName.HINT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.CHECK.ordinal()].addActionListener((ActionEvent e) -> checkProof());
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].addActionListener((ActionEvent e) -> {
        });

        toolBarButtons[ToolbarName.CHECK_ALL.ordinal()].addActionListener((ActionEvent e) -> checkProofAll());

        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
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
        } else {
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

        /**
         * Select dir to grade; recursively grade sub-dirs using traverseDir()
         * Selected dir must have sub-dirs for each student:
         * GradeThis
         *    |
         *    | -> Student 1
         *    |       |
         *    |       | -> Proofs
         **/
        folderBrowser = new JFileChooser();
        folderBrowser.setCurrentDirectory(new java.io.File("."));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);
        folderBrowser.showOpenDialog(this);
        File folder = folderBrowser.getSelectedFile();

        // Write csv file (Path,File-Name,Score,Solved?)
        File resultFile = new File(folder.getAbsolutePath() + File.separator + "result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name,File Name,Score,Solved?\n");

            // Go through student folders
            for (final File folderEntry : folder.listFiles(File::isDirectory)) {
                // Write path
                String path = folderEntry.getName();
                traverseDir(folderEntry, writer, path);
            }
        }catch (IOException ex){
            //LOGGER.error(ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Batch grading complete.");
    }

    private void traverseDir(File folder, BufferedWriter writer, String path) throws IOException {
        // Recursively traverse directory
        GameBoardFacade facade = GameBoardFacade.getInstance();

        // Folder is empty
        if(folder.listFiles().length == 0) {
            writer.append(path + ",Empty folder,0,Ungradeable\n");
            return;
        }

        // Travese directory, recurse if sub-directory found
        for(final File f : folder.listFiles()) {
            // Recurse
            if(f.isDirectory()) {
                traverseDir(f, writer, path + "/" + f.getName());
                continue;
            }

            // Set path name
            writer.append(path + ",");

            // Load puzzle, run checker
            // If wrong file type, ungradeable
            String fName = f.getName();
            String fPath = f.getAbsolutePath();
            File puzzleFile = new File(fPath);
            if(puzzleFile != null && puzzleFile.exists()) {
                // Try to load file. If invalid, note in csv
                try {
                    // Load puzzle, run checker
                    GameBoardFacade.getInstance().loadPuzzle(fPath);
                    String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                    frame.setTitle(puzzleName + " - " + puzzleFile.getName());
                    facade = GameBoardFacade.getInstance();
                    Puzzle puzzle = facade.getPuzzleModule();

                    // Write data
                    writer.append(fName + ",");
                    if(puzzle.isPuzzleComplete()) writer.append("1,Solved\n");
                    else writer.append("0,Unsolved\n");
                } catch (InvalidFileFormatException e) {
                    writer.append(fName + " - invalid type,0,Ungradeable\n");
                }
            } else {
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
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(true);
        redo.setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);

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
        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
        redo.setEnabled(false);
        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
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
            frame.setTitle(puzzleName + " - " + puzzleFile.getName());
        } else {
            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
            File puzzleFile = new File(GameBoardFacade.getInstance().getCurFileName());
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
        } else {
            JOptionPane.showConfirmDialog(null, "Your proof is incorrect! Are you sure you wish to submit?", "Proof Submission", JOptionPane.YES_NO_OPTION);
            Submission submit = new Submission(board);
        }
    }
}
