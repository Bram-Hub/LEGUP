package edu.rpi.legup.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import java.awt.*;
import java.awt.event.*;
import java.security.InvalidParameterException;
import java.util.Objects;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LegupUI extends JFrame implements WindowListener {
    private static final Logger LOGGER = LogManager.getLogger(LegupUI.class.getName());

    protected FileDialog fileDialog;
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
        } else {
            os = "win";
        }
        return os;
    }

    /** LegupUI Constructor - creates a new LegupUI to setup the menu and toolbar */
    public LegupUI() {
        setTitle("LEGUP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LegupPreferences prefs = LegupPreferences.getInstance();

        try {
            if (Boolean.valueOf(prefs.getUserPref(LegupPreferences.DARK_MODE))) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Not supported ui look and feel");
        }

        fileDialog = new FileDialog(this);

        initPanels();
        displayPanel(0);

        setIconImage(
                new ImageIcon(
                                Objects.requireNonNull(
                                        ClassLoader.getSystemClassLoader()
                                                .getResource(
                                                        "edu/rpi/legup/images/Legup/Direct"
                                                                + " Rules.gif")))
                        .getImage());

        if (LegupPreferences.getInstance()
                .getUserPref(LegupPreferences.START_FULL_SCREEN)
                .equals(Boolean.toString(true))) {
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

    private void initPanels() {
        window = new JPanel();
        window.setLayout(new BorderLayout());
        add(window);
        panels = new LegupPanel[3];

        panels[0] = new HomePanel(this.fileDialog, this, this);
        panels[1] = new ProofEditorPanel(this.fileDialog, this, this);
        panels[2] = new PuzzleEditorPanel(this.fileDialog, this, this);
    }

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

    public ProofEditorPanel getProofEditor() {
        return (ProofEditorPanel) panels[1];
    }

    public PuzzleEditorPanel getPuzzleEditor() {
        return (PuzzleEditorPanel) panels[2];
    }

    public void repaintTree() {
        getProofEditor().repaintTree();
    }

    private void directions() {
        JOptionPane.showMessageDialog(
                null,
                "For every move you make, you must provide a rules for it (located in the Rules"
                        + " panel).\n"
                        + "While working on the edu.rpi.legup.puzzle, you may click on the \"Check\""
                        + " button to test your proof for correctness.",
                "Directions",
                JOptionPane.PLAIN_MESSAGE);
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

    // ask to edu.rpi.legup.save current proof
    public boolean noquit(String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    @Override
    public void windowOpened(WindowEvent e) {}

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

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}

    public BoardView getBoardView() {
        return getProofEditor().getBoardView();
    }

    public BoardView getEditorBoardView() {
        return getPuzzleEditor().getBoardView();
    }

    public DynamicView getDynamicBoardView() {
        return getProofEditor().getDynamicBoardView();
    }

    public DynamicView getEditorDynamicBoardView() {
        return getPuzzleEditor().getDynamicBoardView();
    }

    public TreePanel getTreePanel() {
        return getProofEditor().getTreePanel();
    }
}
