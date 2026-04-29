package edu.rpi.legup.ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatPropertiesLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

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
     * @return operating system, either "mac" or "win"
     */
    @NotNull public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            os = "mac";
        } else {
            os = "win";
        }
        return os;
    }

    /** Registers the necessary LAF and font files. */
    private static void registerAssets() {

        FlatLaf.registerCustomDefaultsSource("edu/rpi/legup/themes");

        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .registerFont(
                            Font.createFont(
                                    Font.TRUETYPE_FONT,
                                    Objects.requireNonNull(
                                            LegupUI.class
                                                    .getClassLoader()
                                                    .getResourceAsStream(
                                                            "edu/rpi/legup/fonts/Roboto/Roboto-Regular.ttf"))));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void updateColorTheme() {

        boolean useCustomTheme = LegupPreferences.useCustomColorTheme();
        if (useCustomTheme) {

            // Create a LAF from a user-provided .properties file
            try {
                FlatLaf.setup(
                        new FlatPropertiesLaf(
                                "Custom Theme", new File(LegupPreferences.colorThemeFile())));
            } catch (IOException exception) {
                System.err.printf(
                        "Invalid color theme file '%s', using default theme.\n",
                        LegupPreferences.colorThemeFile());
                useCustomTheme = false;
            }
        }
        if (!useCustomTheme) {

            // Create a LAF from default files
            try {
                if (!LegupPreferences.darkMode()) {
                    if (!LegupPreferences.colorBlind()) {
                        FlatLaf.setup(
                                new FlatPropertiesLaf(
                                        "Light Theme",
                                        Objects.requireNonNull(
                                                LegupUI.class
                                                        .getClassLoader()
                                                        .getResourceAsStream(
                                                                "edu/rpi/legup/themes/light-theme.properties"))));
                    } else {
                        FlatLaf.setup(
                                new FlatPropertiesLaf(
                                        "Deuteranomaly Light Theme",
                                        Objects.requireNonNull(
                                                LegupUI.class
                                                        .getClassLoader()
                                                        .getResourceAsStream(
                                                                "edu/rpi/legup/themes/light-color-blind-theme.properties"))));
                    }
                } else {
                    if (!LegupPreferences.colorBlind()) {
                        FlatLaf.setup(
                                new FlatPropertiesLaf(
                                        "Dark Theme",
                                        Objects.requireNonNull(
                                                LegupUI.class
                                                        .getClassLoader()
                                                        .getResourceAsStream(
                                                                "edu/rpi/legup/themes/dark-theme.properties"))));
                    } else {
                        FlatLaf.setup(
                                new FlatPropertiesLaf(
                                        "Deuteranomaly Dark Theme",
                                        Objects.requireNonNull(
                                                LegupUI.class
                                                        .getClassLoader()
                                                        .getResourceAsStream(
                                                                "edu/rpi/legup/themes/dark-color-blind-theme.properties"))));
                    }
                }
            } catch (IOException | NullPointerException exception) {
                System.err.println("Provided color theme properties not found");
                System.err.println("\tDark mode: " + LegupPreferences.darkMode());
                System.err.println("\tColor blind: " + LegupPreferences.colorBlind());
                exception.printStackTrace();

                // Try to fall back on FlatLightLaf which will hopefully have the additional keys
                FlatLightLaf.setup();
            }

            // Update UI with new LAF
            updateIconColors();
            FlatLaf.updateUI();
        }
    }

    /**
     * Update the global color filter applied to all {@code FlatSVGIcon}s to convert puzzle-related
     * colors to the values defined in the LAF. If a puzzle is currently set, values for that puzzle
     * type will be retrieved, otherwise only universally-used values will be set.
     */
    public static void updateIconColors() {

        ColorFilter globalFilter = ColorFilter.getInstance();
        UIDefaults defs = UIManager.getDefaults();
        String puzzleName = null;

        if (GameBoardFacade.getInstance().getPuzzleModule() != null) {
            puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
        }

        // Make maps to apply universal colors before puzzle-specific colors
        HashMap<Color, Color> baseMap = new HashMap<>();
        HashMap<Color, Color> puzzleMap = new HashMap<>();

        for (String key :
                defs.keySet().stream().map(Object::toString).collect(Collectors.toSet())) {
            if (key.startsWith("Expected.")) {

                int offset = ("Expected.").length();
                Color color = defs.getColor(key);

                if (key.startsWith("SvgIcon.", offset)) {
                    baseMap.put(color, defs.getColor(key.substring(offset)));
                } else if (key.startsWith(puzzleName + ".", offset)) {
                    puzzleMap.put(color, defs.getColor(key.substring(offset)));
                }
            }
        }

        globalFilter.addAll(baseMap);
        globalFilter.addAll(puzzleMap);
    }

    /** LegupUI Constructor - creates a new LegupUI to set up the menu and toolbar */
    public LegupUI() {
        setTitle("LEGUP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        // Set the application's LAF in accordance with user preferences
        registerAssets();
        updateColorTheme();
        if (UIManager.getLookAndFeel() == null) {
            FlatLightLaf.setup();
        }

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
                    public void keyTyped(@NotNull KeyEvent e) {
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
    @NotNull public ProofEditorPanel getProofEditor() {
        return (ProofEditorPanel) panels[1];
    }

    /**
     * Gets the PuzzleEditorPanel instance
     *
     * @return the PuzzleEditorPanel
     */
    @NotNull public PuzzleEditorPanel getPuzzleEditor() {
        return (PuzzleEditorPanel) panels[2];
    }

    /** Repaints the tree view in the proof editor. */
    public void repaintTree() {
        getProofEditor().repaintTree();
    }

    public void showStatus(@NotNull String status, boolean error) {
        showStatus(status, error, 1);
    }

    public void errorEncountered(@NotNull String error) {
        JOptionPane.showMessageDialog(null, error);
    }

    public void showStatus(@NotNull String status, boolean error, int timer) {
        // TODO: implement
    }

    /**
     * Prompts the user to confirm if they want to exit LEGUP
     *
     * @param instr the prompt message
     * @return true if the user chooses not to quit, false otherwise
     */
    public boolean exit(@NotNull String instr) {
        int n = JOptionPane.showConfirmDialog(null, instr, "Confirm", JOptionPane.YES_NO_OPTION);
        return n != JOptionPane.YES_OPTION;
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    public void windowClosing(WindowEvent e) {
        if (GameBoardFacade.getInstance().getHistory().getIndex() > -1) {
            if (exit("Exiting LEGUP?")) {
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

    /**
     * Gets the BoardView instance from the proof editor
     *
     * @return the BoardView
     */
    @NotNull public BoardView getBoardView() {
        return getProofEditor().getBoardView();
    }

    /**
     * Gets the BoardView instance from the puzzle editor
     *
     * @return the BoardView
     */
    @NotNull public BoardView getEditorBoardView() {
        return getPuzzleEditor().getBoardView();
    }

    /**
     * Gets the DynamicView instance from the proof editor
     *
     * @return the DynamicView
     */
    @NotNull public DynamicView getDynamicBoardView() {
        return getProofEditor().getDynamicBoardView();
    }

    /**
     * Gets the DynamicView instance from the puzzle editor.
     *
     * @return the DynamicView
     */
    @NotNull public DynamicView getEditorDynamicBoardView() {
        return getPuzzleEditor().getDynamicBoardView();
    }

    /**
     * Gets the TreePanel instance from the proof editor
     *
     * @return the TreePanel
     */
    @NotNull public TreePanel getTreePanel() {
        return getProofEditor().getTreePanel();
    }
}
