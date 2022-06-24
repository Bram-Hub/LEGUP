package edu.rpi.legup.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.InvalidParameterException;

import javax.swing.*;


import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.ui.lookandfeel.LegupLookAndFeel;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import edu.rpi.legupupdate.Update;
import edu.rpi.legupupdate.UpdateProgress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LegupUI extends JFrame implements WindowListener {
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

    protected FileDialog fileDialog;
    protected JFileChooser folderBrowser;

    protected JMenu proofMode = new JMenu("Proof Mode");
    protected JCheckBoxMenuItem[] proofModeItems = new JCheckBoxMenuItem[PROF_FLAGS.length];

    protected JMenu ai = new JMenu("AI");
    protected JMenuItem runAI = new JMenuItem("Run AI to completion");
    protected JMenuItem setpAI = new JMenuItem("Run AI one Step");
    protected JMenuItem testAI = new JMenuItem("Test AI!");
    protected JMenuItem hintAI = new JMenuItem("Hint");

    protected JPanel window;

    protected LegupPanel[] panels;

    /**
     * Identifies operating system 
     */
    public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase(); 
        if(os.contains("mac")) os = "mac"; 
        else os = "win"; 
        return os;  
    }

    /**
     * LegupUI Constructor - creates a new LegupUI to setup the menu and toolbar
     */
    public LegupUI() {
        setTitle("LEGUP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new LegupLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Not supported ui look and feel");
        }

        fileDialog = new FileDialog(this);

        initPanels();
        displayPanel(0);

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
        revalidate();
        repaint();
    }

    public ProofEditorPanel getProofEditor() {
        return (ProofEditorPanel) panels[1];
    }

    public static boolean profFlag(int flag) {
        return !((PROF_FLAGS[CONFIG_INDEX] & flag) == 0);
    }

    public void repaintTree() {
        getProofEditor().repaintTree();
    }

    private void directions() {
        JOptionPane.showMessageDialog(null, "For every move you make, you must provide a rules for it (located in the Rules panel).\n" + "While working on the edu.rpi.legup.puzzle, you may click on the \"Check\" button to test your proof for correctness.", "Directions", JOptionPane.PLAIN_MESSAGE);
    }

    public void showStatus(String status, boolean error) {
        showStatus(status, error, 1);
    }

    public void errorEncountered(String error) {
        JOptionPane.showMessageDialog(null, error);
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

    public BoardView getBoardView() {
        return getProofEditor().getBoardView();
    }

    public DynamicView getDynamicBoardView() {
        return getProofEditor().getDynamicBoardView();
    }

    public TreePanel getTreePanel() {
        return getProofEditor().getTreePanel();
    }
}
