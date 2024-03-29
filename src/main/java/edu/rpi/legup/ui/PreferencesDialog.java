package edu.rpi.legup.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.color.ColorPreferences;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialBorders;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;
import edu.rpi.legup.ui.proofeditorui.rulesview.RuleFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreferencesDialog extends JDialog {

    private RuleFrame rulesFrame;

    private static final Logger LOGGER = Logger.getLogger(PreferencesDialog.class.getName());

    private JCheckBox fullScreen,
            autoUpdate,
            darkMode,
            showMistakes,
            showAnnotations,
            allowDefault,
            generateCases,
            immFeedback,
            colorBlind;

    private JTextField workDirectory;
    private JTextField colorThemeFile;

    private static Image folderIcon;

    static {
        try {
            folderIcon =
                    ImageIO.read(
                            PreferencesDialog.class.getResource("/edu/rpi/legup/imgs/folder.png"));
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to locate icons");
        }
    }

    public static PreferencesDialog CreateDialogForProofEditor(Frame frame, RuleFrame rules) {
        PreferencesDialog p = new PreferencesDialog(frame);
        p.rulesFrame = rules;
        return p;
    }

    public PreferencesDialog(Frame frame) {
        super(frame);

        setTitle("Preferences");

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createGeneralTab());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(null);
        bottomPanel.setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        toolbar.setBorder(null);
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(
                l -> {
                    applyPreferences();
                    this.setVisible(false);
                    this.dispose();
                });
        toolbar.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(
                l -> {
                    this.setVisible(false);
                    this.dispose();
                });
        toolbar.add(cancelButton);
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(
                l -> {
                    applyPreferences();
                });
        toolbar.add(applyButton);
        bottomPanel.add(toolbar, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        setSize(600, 400);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void toggleDarkMode(LegupPreferences prefs) {
        try {
            if (LegupPreferences.darkMode()) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            com.formdev.flatlaf.FlatLaf.updateUI();
        }
        catch (UnsupportedLookAndFeelException e) {
            System.err.println("Not supported ui look and feel");
        }
    }

    private JScrollPane createGeneralTab() {
        LegupPreferences prefs = LegupPreferences.getInstance();
        JScrollPane scrollPane = new JScrollPane();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        contentPane.add(createLeftLabel("General Preferences"));
        contentPane.add(createLineSeparator());

        JPanel workRow = new JPanel();
        workRow.setLayout(new BorderLayout());
        JLabel workDirLabel = new JLabel("Work Directory");
        workDirLabel.setToolTipText("This is where the open and save dialogs will open to.");
        workRow.add(workDirLabel, BorderLayout.WEST);
        workDirectory = new JTextField(LegupPreferences.workDirectory());
        workRow.add(workDirectory, BorderLayout.CENTER);
        JButton openDir = new JButton(new ImageIcon(folderIcon));
        openDir.addActionListener(
                a -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File(workDirectory.getText()));
                    chooser.setDialogTitle("Choose work directory");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setVisible(true);

                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File newFile = chooser.getSelectedFile();
                        workDirectory.setText(newFile.toString());
                    }
                });
        workRow.add(openDir, BorderLayout.EAST);
        workRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, workRow.getPreferredSize().height));
        contentPane.add(workRow);

        fullScreen =
                new JCheckBox(
                        "Full Screen",
                        LegupPreferences.startFullScreen());
        fullScreen.setToolTipText("If checked this starts Legup in full screen.");
        JPanel fullScreenRow = new JPanel();
        fullScreenRow.setLayout(new BorderLayout());
        fullScreenRow.add(fullScreen, BorderLayout.WEST);
        fullScreenRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, fullScreenRow.getPreferredSize().height));
        contentPane.add(fullScreenRow);

        autoUpdate =
                new JCheckBox(
                        "Automatically Check for Updates",
                        LegupPreferences.autoUpdate());
        autoUpdate.setToolTipText(
                "If checked this automatically checks for updates on startup of Legup");
        JPanel autoUpdateRow = new JPanel();
        autoUpdateRow.setLayout(new BorderLayout());
        autoUpdateRow.add(autoUpdate, BorderLayout.WEST);
        autoUpdateRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, autoUpdateRow.getPreferredSize().height));
        contentPane.add(autoUpdateRow);
        //        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        darkMode =
                new JCheckBox(
                        "Dark Mode",
                        LegupPreferences.darkMode());
        darkMode.setToolTipText("This turns dark mode on and off");
        JPanel darkModeRow = new JPanel();
        darkModeRow.setLayout(new BorderLayout());
        darkModeRow.add(darkMode, BorderLayout.WEST);
        darkModeRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, darkModeRow.getPreferredSize().height));
        contentPane.add(darkModeRow);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        contentPane.add(createLeftLabel("Board View Preferences"));
        contentPane.add(createLineSeparator());
        showMistakes =
                new JCheckBox(
                        "Show Mistakes",
                        LegupPreferences.showMistakes());
        showMistakes.setToolTipText(
                "If checked this show incorrectly applied rule applications in red on the board");
        JPanel showMistakesRow = new JPanel();
        showMistakesRow.setLayout(new BorderLayout());
        showMistakesRow.add(showMistakes, BorderLayout.WEST);
        showMistakesRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, showMistakesRow.getPreferredSize().height));
        contentPane.add(showMistakesRow);

        showAnnotations =
                new JCheckBox(
                        "Show Annotations",
                        LegupPreferences.showAnnotations());
        showAnnotations.setToolTipText(
                "If checked this show incorrectly applied rule applications in red on the board");
        JPanel showAnnotationsRow = new JPanel();
        showAnnotationsRow.setLayout(new BorderLayout());
        showAnnotationsRow.add(showAnnotations, BorderLayout.WEST);
        showAnnotationsRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, showAnnotationsRow.getPreferredSize().height));
        contentPane.add(showAnnotationsRow);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        contentPane.add(createLeftLabel("Tree View Preferences"));
        contentPane.add(createLineSeparator());

        allowDefault =
                new JCheckBox(
                        "Allow Default Rule Applications",
                        LegupPreferences.allowDefaultRules());
        allowDefault.setEnabled(false);
        allowDefault.setToolTipText(
                "If checked this automatically applies a rule where it can on the board");

        JPanel allowDefaultRow = new JPanel();
        allowDefaultRow.setLayout(new BorderLayout());
        allowDefaultRow.add(allowDefault, BorderLayout.WEST);
        allowDefaultRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, allowDefaultRow.getPreferredSize().height));
        contentPane.add(allowDefaultRow);

        generateCases =
                new JCheckBox(
                        "Automatically Generate Cases",
                        LegupPreferences.autoGenerateCases());
        generateCases.setToolTipText(
                "If checked this automatically generates all cases for a case rule");
        JPanel generateCasesRow = new JPanel();
        generateCasesRow.setLayout(new BorderLayout());
        generateCasesRow.add(generateCases, BorderLayout.WEST);
        generateCasesRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, generateCasesRow.getPreferredSize().height));
        contentPane.add(generateCasesRow);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        immFeedback =
                new JCheckBox(
                        "Provide Immediate Feedback",
                        LegupPreferences.immediateFeedback());
        immFeedback.setToolTipText(
                "If checked this will update the colors of the tree view elements immediately");
        JPanel immFeedbackRow = new JPanel();
        immFeedbackRow.setLayout(new BorderLayout());
        immFeedbackRow.add(immFeedback, BorderLayout.WEST);
        immFeedbackRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, immFeedbackRow.getPreferredSize().height));
        contentPane.add(immFeedbackRow);

        contentPane.add(createLeftLabel("Instructor Preferences"));
        contentPane.add(createLineSeparator());
        immFeedback =
                new JCheckBox(
                        "Instructor Mode",
                        LegupPreferences.immediateFeedback());
        immFeedback.setToolTipText("Currently unimplemented, this does nothing right now");
        immFeedbackRow.setLayout(new BorderLayout());
        immFeedbackRow.add(immFeedback, BorderLayout.WEST);
        immFeedbackRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, immFeedbackRow.getPreferredSize().height));
        contentPane.add(immFeedbackRow);

        contentPane.add(createLeftLabel("Color Preferences"));
        contentPane.add(createLineSeparator());
        colorBlind =
                new JCheckBox(
                        "Deuteranomaly(red/green colorblindness)",
                        LegupPreferences.colorBlind());

        JPanel colorBlindRow = new JPanel();
        colorBlindRow.setLayout(new BorderLayout());
        colorBlindRow.add(colorBlind, BorderLayout.WEST);
        colorBlindRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, showMistakesRow.getPreferredSize().height));
        contentPane.add(colorBlindRow);

        JPanel colorThemeRow = new JPanel();
        colorThemeRow.setLayout(new BorderLayout());
        JLabel colorThemeDirLabel = new JLabel("Color Theme File");
        colorThemeDirLabel.setToolTipText("This is the color theme LEGUP will use.");
        colorThemeRow.add(colorThemeDirLabel, BorderLayout.WEST);
        colorThemeFile = new JTextField(LegupPreferences.workDirectory() + "/" + ColorPreferences.LIGHT_COLOR_THEME_FILE_NAME);
        colorThemeRow.add(colorThemeFile, BorderLayout.CENTER);
        JButton openColorThemeFile = new JButton(new ImageIcon(folderIcon));
        openColorThemeFile.addActionListener(
                a -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File(colorThemeDirLabel.getText()));
                    chooser.setDialogTitle("Choose color theme file");
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setVisible(true);

                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File newFile = chooser.getSelectedFile();
                        colorThemeFile.setText(newFile.toString());
                    }
                });
        colorThemeRow.add(openColorThemeFile, BorderLayout.EAST);
        colorThemeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, colorThemeRow.getPreferredSize().height));
        contentPane.add(colorThemeRow);


        scrollPane.setViewportView(contentPane);
        return scrollPane;
    }

    private JScrollPane createPuzzleTab(Puzzle puzzle) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(createLeftLabel("Rules"));
        contentPane.add(createLineSeparator());

        contentPane.add(createLeftLabel("Direct Rules"));
        contentPane.add(createLineSeparator());
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        for (Rule rule : puzzle.getDirectRules()) {
            JPanel ruleRow = createRuleRow(rule);
            contentPane.add(ruleRow);
            contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        contentPane.add(createLeftLabel("Case Rules"));
        contentPane.add(createLineSeparator());
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        for (Rule rule : puzzle.getCaseRules()) {
            JPanel ruleRow = createRuleRow(rule);
            contentPane.add(ruleRow);
            contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        contentPane.add(createLeftLabel("Contradiction Rules"));
        contentPane.add(createLineSeparator());
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        for (Rule rule : puzzle.getContradictionRules()) {
            JPanel ruleRow = createRuleRow(rule);
            contentPane.add(ruleRow);
            contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        scrollPane.setViewportView(contentPane);
        return scrollPane;
    }

    private JPanel createRuleRow(Rule rule) {
        JPanel ruleRow = new JPanel();
        ruleRow.setLayout(new BorderLayout());

        JLabel ruleLabel = new JLabel(rule.getRuleName());
        ruleRow.add(ruleLabel, BorderLayout.WEST);

        JLabel ruleAcc = new JLabel();
        ruleAcc.setHorizontalAlignment(JLabel.CENTER);
        ruleAcc.setBorder(MaterialBorders.LIGHT_LINE_BORDER);
        ruleAcc.setPreferredSize(new Dimension(60, 20));
        ruleAcc.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ruleAcc.requestFocusInWindow();
                    }
                });

        ruleAcc.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int keyCode = e.getKeyCode();
                        String combo = "";
                        if (e.isControlDown()) {
                            combo += "Ctrl + ";
                        }
                        else {
                            if (e.isShiftDown()) {
                                combo += "Shift + ";
                            }
                            else {
                                if (e.isAltDown()) {
                                    combo += "Alt + ";
                                }
                            }
                        }
                        if (keyCode == KeyEvent.VK_CONTROL
                                || keyCode == KeyEvent.VK_SHIFT
                                || keyCode == KeyEvent.VK_ALT) {
                            return;
                        }
                        combo += KeyEvent.getKeyText(keyCode);
                        ruleAcc.setText(combo);
                    }
                });

        ruleRow.add(ruleAcc, BorderLayout.EAST);

        ruleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, ruleRow.getPreferredSize().height));
        return ruleRow;
    }

    private JPanel createLeftLabel(String text) {
        JPanel labelRow = new JPanel();
        labelRow.setLayout(new BorderLayout());
        JLabel label = new JLabel(text);
        label.setFont(MaterialFonts.BOLD);
        label.setHorizontalAlignment(JLabel.LEFT);
        labelRow.add(label, BorderLayout.WEST);

        labelRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, labelRow.getPreferredSize().height));
        return labelRow;
    }

    private JSeparator createLineSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        return separator;
    }

    public void applyPreferences() {
        LegupPreferences prefs = LegupPreferences.getInstance();
        prefs.setUserPref(LegupPreferences.LegupPreference.WORK_DIRECTORY, workDirectory.getText());
        prefs.setUserPref(
                LegupPreferences.LegupPreference.START_FULL_SCREEN, fullScreen.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.AUTO_UPDATE, autoUpdate.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.DARK_MODE, darkMode.isSelected());
        prefs.setUserPref(
                LegupPreferences.LegupPreference.SHOW_MISTAKES, showMistakes.isSelected());
        prefs.setUserPref(
                LegupPreferences.LegupPreference.SHOW_ANNOTATIONS, showAnnotations.isSelected());
        prefs.setUserPref(
                LegupPreferences.LegupPreference.ALLOW_DEFAULT_RULES, allowDefault.isSelected());
        prefs.setUserPref(
                LegupPreferences.LegupPreference.AUTO_GENERATE_CASES, generateCases.isSelected());
        prefs.setUserPref(
                LegupPreferences.LegupPreference.IMMEDIATE_FEEDBACK, immFeedback.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.COLOR_BLIND, colorBlind.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.COLOR_THEME_FILE, colorThemeFile.getText());

        if (rulesFrame != null) {
            rulesFrame.getCasePanel().updateRules();
            rulesFrame.getDirectRulePanel().updateRules();
            rulesFrame.getContradictionPanel().updateRules();
        }

        // toggle dark mode based on updated NIGHT_MODE variable
        toggleDarkMode(prefs);
    }
}
