package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.save.ExportFileException;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.puzzleeditorui.elementsview.ElementFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;

public class PuzzleEditorPanel extends LegupPanel implements IHistoryListener {

    private final static Logger LOGGER = LogManager.getLogger(PuzzleEditorPanel.class.getName());
    private JMenu[] menus;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JFrame frame;
    private JButton[] buttons;
    JSplitPane splitPanel;
    private JButton[] toolBarButtons;
    private JPanel elementPanel;
    private DynamicView dynamicBoardView;
    private BoardView boardView;
    private TitledBorder boardBorder;
    //private JSplitPane splitPanel, topHalfPanel;
    private FileDialog fileDialog;
    private JMenuItem undo, redo, fitBoardToScreen;
    private ElementFrame elementFrame;
    private JPanel treePanel;
    private LegupUI legupUI;
    private EditorElementController editorElementController;
    final static int[] TOOLBAR_SEPARATOR_BEFORE = {2, 4, 8};

    public PuzzleEditorPanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        this.legupUI = legupUI;
        setLayout(new BorderLayout());
    }

    protected void setupContent() {
        JSplitPane splitPanel;
        JPanel elementBox = new JPanel(new BorderLayout());

        editorElementController = new EditorElementController();
        elementFrame = new ElementFrame(editorElementController);
        elementBox.add(elementFrame, BorderLayout.WEST);

        dynamicBoardView = new DynamicView(new ScrollView(new BoardController()), DynamicViewType.BOARD);
        TitledBorder titleBoard = BorderFactory.createTitledBorder("Board");
        titleBoard.setTitleJustification(TitledBorder.CENTER);
        dynamicBoardView.setBorder(titleBoard);

        JPanel boardPanel = new JPanel(new BorderLayout());
        splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, elementFrame, dynamicBoardView);
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
        newPuzzle.addActionListener((ActionEvent) -> promptPuzzle());
        if (os.equals("mac")) {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        }
        // file>save
        JMenuItem savePuzzle = new JMenuItem("Save");
        savePuzzle.addActionListener((ActionEvent) -> savePuzzle());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((ActionEvent) -> this.legupUI.displayPanel(0));
        if (os.equals("mac")) {
            exit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            exit.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        }
        menus[0].add(newPuzzle);
        menus[0].add(savePuzzle);
        menus[0].add(exit);

        // EDIT
        menus[1] = new JMenu("Edit");
        // edit>undo
        undo = new JMenuItem("Undo");
        // edit>redo
        redo = new JMenuItem("Redo");
        fitBoardToScreen = new JMenuItem("Fit Board to Screen");

        menus[1].add(undo);
        undo.addActionListener((ActionEvent) ->
                GameBoardFacade.getInstance().getHistory().undo());
        if (os.equals("mac")) {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        }
        else {
            undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        }

        menus[1].add(redo);
        redo.addActionListener((ActionEvent) ->
                GameBoardFacade.getInstance().getHistory().redo());
        if (os.equals("mac")) {
            redo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_DOWN_MASK));
        }
        else {
            redo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        }

        menus[1].add(fitBoardToScreen);
        fitBoardToScreen.addActionListener((ActionEvent) -> dynamicBoardView.fitBoardViewToScreen());

        // HELP
        menus[2] = new JMenu("Help");

        // add menus to menubar
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
        frame.setJMenuBar(menuBar);
    }

    @Override
    public void makeVisible() {
        this.removeAll();

        setupToolBar();
        setupContent();
        setMenuBar();
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
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].addActionListener((ActionEvent e) -> {
        });
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].addActionListener((ActionEvent e) -> {
        });

//        toolBarButtons[ToolbarName.SAVE.ordinal()].setEnabled(false);
//        toolBarButtons[ToolbarName.UNDO.ordinal()].setEnabled(false);
//        toolBarButtons[ToolbarName.REDO.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.HINT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.SUBMIT.ordinal()].setEnabled(false);
        toolBarButtons[ToolbarName.DIRECTIONS.ordinal()].setEnabled(false);

        this.add(toolBar, BorderLayout.NORTH);
    }

    public void loadPuzzleFromHome(String game, int rows, int columns) throws IllegalArgumentException {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        try {
            facade.loadPuzzle(game, rows, columns);
        }
        catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public Object[] promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noQuit("Opening a new puzzle to edit?")) {
                return new Object[0];
            }
        }
        if (fileDialog == null) {
            fileDialog = new FileDialog(this.frame);
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
        else {
            return null;
        }

        return new Object[]{fileName, puzzleFile};
    }

    public void loadPuzzle() {
        Object[] items = promptPuzzle();
        String fileName = (String) items[0];
        File puzzleFile = (File) items[1];
        loadPuzzle(fileName, puzzleFile);
    }

    public void loadPuzzle(String fileName, File puzzleFile) {
        if (puzzleFile != null && puzzleFile.exists()) {
            try {
                GameBoardFacade.getInstance().loadPuzzleEditor(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                frame.setTitle(puzzleName + " - " + puzzleFile.getName());
            }
            catch (InvalidFileFormatException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public boolean noQuit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    @Override
    public void onPushChange(ICommand command) {

    }

    @Override
    public void onUndo(boolean isBottom, boolean isTop) {

    }

    @Override
    public void onRedo(boolean isBottom, boolean isTop) {

    }

    @Override
    public void onClearHistory() {

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

        TitledBorder titleBoard = BorderFactory.createTitledBorder(boardView.getClass().getSimpleName());
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

    /**
     * Saves a puzzle
     */
    private void savePuzzle() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
        }

        if (fileDialog == null) {
            fileDialog = new FileDialog(this.frame);
        }

        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setTitle("Save Proof");
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

    public DynamicView getDynamicBoardView() {
        return dynamicBoardView;
    }

}
