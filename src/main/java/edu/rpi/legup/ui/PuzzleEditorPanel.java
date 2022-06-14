package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;

public class PuzzleEditorPanel extends LegupPanel {

    private final static Logger LOGGER = LogManager.getLogger(ProofEditorPanel.class.getName());
    private JMenu[] menus;
    private JMenuBar menuBar;
    private JFrame frame;
    private JButton[] buttons;

    private JPanel elementPanel;
    private JPanel mainPanel;
    private FileDialog fileDialog;
    private JMenuItem undo, redo;
    public PuzzleEditorPanel(FileDialog fileDialog, JFrame frame) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        setLayout(new GridLayout(2, 1));
        setup();
    }

    private void setup() {
        mainPanel = new JPanel();
        mainPanel.setSize(100,300);
        // menu bar


        // buttons
        buttons = new JButton[3];
        buttons[0] = new JButton("element 0");
        buttons[1] = new JButton("element 1");
        buttons[2] = new JButton("element 2");
        for (JButton button : buttons) {
            mainPanel.add(button);
        }
        setMenuBar();
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
        if(os.equals("mac")) newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        else newPuzzle.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        // file>save
        JMenuItem savePuzzle = new JMenuItem("Save");

        menus[0].add(newPuzzle);
        menus[0].add(savePuzzle);

        // EDIT
        menus[1] = new JMenu("Edit");
        // edit>undo
        undo = new JMenuItem("Undo");
        // edit>redo
        redo = new JMenuItem("Redo");

        menus[1].add(undo);
        menus[1].add(redo);

        // HELP
        menus[2] = new JMenu("Help");

        // add menus to menubar
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
    }

    @Override
    public void makeVisible() {
        render();
        frame.setJMenuBar(menuBar);
    }

    public void promptPuzzle() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        if (facade.getBoard() != null) {
            if (noQuit("Opening a new puzzle to edit?")) // !noquit or noquit?
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
    public boolean noQuit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        return n != JOptionPane.YES_OPTION;
    }
    private void render() {
        add(mainPanel);
        add(new JLabel("Welcome to the puzzle editor!"));
    }
}
