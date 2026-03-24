package edu.rpi.legup.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.lookandfeel.LegupCustomColorScheme;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Objects;

/**
 * The main user interface class for the LEGUP application. This class extends {@link JFrame} and
 * implements {@link WindowListener} to manage the overall window and provide functionality for
 * displaying various panels.
 */
public class LegupUI extends JFrame implements WindowListener {
    private static final Logger LOGGER = LogManager.getLogger(LegupUI.class.getName());

    protected JFileChooser fileChooser;
    protected JPanel window;
    protected LegupPanel[] panels;

    /**
     * Identifies operating system
     *
     * @return operating system, either mac or win
     */
    public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            os = "mac";
        }
        else {
            os = "win";
        }
        return os;
    }

    public static void updateColorTheme() {
        try {
            final String colorFileName = LegupPreferences.colorThemeFile();
            final boolean isTxt = colorFileName.endsWith(".txt");
            boolean useCustomColorTheme = LegupPreferences.useCustomColorTheme();
            if (!isTxt && useCustomColorTheme) {
                System.err.printf("Invalid color theme file '%s', using default theme.\n", colorFileName);
                useCustomColorTheme = false;
            }
            if (isTxt && useCustomColorTheme) {
                LegupCustomColorScheme.setupCustomColorScheme(colorFileName);
            } else {
                FlatLaf.setGlobalExtraDefaults(Collections.emptyMap());
            }

            if (LegupPreferences.darkMode()) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }

            com.formdev.flatlaf.FlatLaf.updateUI();
        }
        catch (UnsupportedLookAndFeelException exception) {
            throw new RuntimeException("Not supported ui look and feel", exception);
        }
    }

    /**
     * LegupUI Constructor - creates a new LegupUI to set up the menu and toolbar
     */
    public LegupUI() {
        setTitle("LEGUP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        updateColorTheme();

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(LegupPreferences.workDirectory()));

        initPanels();
        displayPanel(0);

        setIconImage(
                new ImageIcon(
                        Objects.requireNonNull(
                                ClassLoader.getSystemClassLoader()
                                        .getResource(
                                                "edu/rpi/legup/images/Legup/Direct Rules.gif")))
                        .getImage());

        if (LegupPreferences.startFullScreen()) {
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }

        this.addWindowListener(this);
        addKeyListener(
                new KeyAdapter() {
                    /**
                     * Invoked when a key has been typed. This event occurs when a key press is
                     * followed by a key release.
                     *
                     * @param e
                     */
                    @Override
                    public void keyTyped(KeyEvent e) {
                        System.err.println(e.getKeyChar());
                        super.keyTyped(e);
                    }
                });
        setMinimumSize(getPreferredSize());
        setVisible(true);
    }

    /** Initializes the panels used in the UI. Sets up the layout and adds panels to the window. */
    private void initPanels() {
        window = new JPanel();
        window.setLayout(new BorderLayout());
        add(window);
        panels = new LegupPanel[3];

        panels[0] = new HomePanel(this, this);
        panels[1] = new ProofEditorPanel(fileChooser, this, this);
        panels[2] = new PuzzleEditorPanel(fileChooser, this, this);
    }

    /**
     * Displays the specified panel
     *
     * @param option the index of the panel to display
     * @throws InvalidParameterException if the option is out of range
     */
    protected void displayPanel(int option) {
        if (option > panels.length || option < 0) {
            throw new InvalidParameterException("Invalid option");
        }
        this.window.removeAll();
        panels[option].makeVisible();
        this.window.add(panels[option]);
        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    /**
     * Gets the ProofEditorPanel instance
     *
     * @return the ProofEditorPanel
     */
    public ProofEditorPanel getProofEditor() {
        return (ProofEditorPanel) panels[1];
    }

    /**
     * Gets the PuzzleEditorPanel instance
     *
     * @return the PuzzleEditorPanel
     */
    public PuzzleEditorPanel getPuzzleEditor() {
        return (PuzzleEditorPanel) panels[2];
    }

    /** Repaints the tree view in the proof editor. */
    public void repaintTree() {
        getProofEditor().repaintTree();
    }

    public void showStatus(String status, boolean error) {
        showStatus(status, error, 1);
    }

    public void errorEncountered(String error) {
        JOptionPane.showMessageDialog(null, error);
    }

    public void showStatus(String status, boolean error, int timer) {
        // TODO: implement
    }

    /**
     * Prompts the user to confirm if they want to exit LEGUP
     *
     * @param instr the prompt message
     * @return true if the user chooses not to quit, false otherwise
     */
    public boolean exit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    public void windowClosing(WindowEvent e) {
        if (GameBoardFacade.getInstance().getHistory().getIndex() > -1) {
            if (exit("Exiting LEGUP?")) {
                this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }
            else {
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
        else {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}

    /**
     * Gets the BoardView instance from the proof editor
     *
     * @return the BoardView
     */
    public BoardView getBoardView() {
        return getProofEditor().getBoardView();
    }

    /**
     * Gets the BoardView instance from the puzzle editor
     *
     * @return the BoardView
     */
    public BoardView getEditorBoardView() {
        return getPuzzleEditor().getBoardView();
    }

    /**
     * Gets the DynamicView instance from the proof editor
     *
     * @return the DynamicView
     */
    public DynamicView getDynamicBoardView() {
        return getProofEditor().getDynamicBoardView();
    }

    /**
     * Gets the DynamicView instance from the puzzle editor.
     *
     * @return the DynamicView
     */
    public DynamicView getEditorDynamicBoardView() {
        return getPuzzleEditor().getDynamicBoardView();
    }

    /**
     * Gets the TreePanel instance from the proof editor
     *
     * @return the TreePanel
     */
    public TreePanel getTreePanel() {
        return getProofEditor().getTreePanel();
    }
}
