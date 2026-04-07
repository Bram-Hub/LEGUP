package edu.rpi.legup.ui;

import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.proofeditorui.rulesview.RuleFrame;
import org.intellij.lang.annotations.MagicConstant;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A dialog for managing user preferences in the LEGUP application. This dialog allows users to
 * configure various settings such as screen mode, update preferences, work directory path, and
 * specific features related to board and tree views. Users can access this dialog from the home
 * screen or proof editor.
 */
public class PreferencesDialog extends JDialog {

    private RuleFrame rulesFrame;

    private static final Logger LOGGER = Logger.getLogger(PreferencesDialog.class.getName());

    private JCheckBox fullScreen,
            autoUpdate,
            darkMode,
            customColorTheme,
            showMistakes,
            showAnnotations,
            allowDefault,
            generateCases,
            immFeedback,
            colorBlind;

    private FileChooserComponents workDirectory;
    private FileChooserComponents colorThemeFile;

    private static Image folderIcon;

    static {
        try {
            folderIcon =
                    ImageIO.read(
                            PreferencesDialog.class.getResource("/edu/rpi/legup/imgs/folder.png"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to locate icons");
        }
    }

    /**
     * Creates a new instance of PreferencesDialog for the proof editor
     *
     * @param frame the parent frame
     * @param rules the RuleFrame associated with the proof editor
     * @return a new instance of PreferencesDialog
     */
    public static PreferencesDialog CreateDialogForProofEditor(Frame frame, RuleFrame rules) {
        PreferencesDialog p = new PreferencesDialog(frame);
        p.rulesFrame = rules;
        return p;
    }

    /**
     * Constructs a PreferencesDialog
     *
     * @param frame the parent frame
     */
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

    private void updateColorTheme(LegupPreferences prefs) {
        LegupUI.updateColorTheme();
    }

    private JCheckBox addDefaultCheckBox(
            String title,
            boolean checked,
            String description,
            JPanel contentPane
    ) {
        return addCheckBox(
                title,
                checked,
                description,
                BorderLayout.WEST,
                row -> row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height)),
                contentPane,
                Box.createRigidArea(new Dimension(0, 10))

        );
    }

    private JCheckBox addCheckBox(
            String label,
            boolean checked,
            String hoverText,
            @MagicConstant(stringValues = {
                    BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST,
                    BorderLayout.CENTER, BorderLayout.BEFORE_FIRST_LINE, BorderLayout.AFTER_LAST_LINE,
                    BorderLayout.BEFORE_LINE_BEGINS, BorderLayout.AFTER_LINE_ENDS, BorderLayout.PAGE_START,
                    BorderLayout.PAGE_END, BorderLayout.LINE_START, BorderLayout.LINE_END
            }) String borderLayout,
            Consumer<JPanel> rowConsumer,
            JPanel contentPane,
            Component area) {
        final JCheckBox box =
                new JCheckBox(label, checked);
        box.setToolTipText(hoverText);
        JPanel row = new JPanel();
        row.setLayout(new BorderLayout());
        row.add(box, borderLayout);
        rowConsumer.accept(row);
        contentPane.add(row);
        contentPane.add(area);
        return box;
    }

    private void addRowLabel(JPanel contentPane, String title) {
        contentPane.add(createLeftLabel(title));
        contentPane.add(createLineSeparator());
    }

    private record FileChooserComponents(JTextField file, JButton openFile, JLabel label) {

        private void setEnabled(boolean enabled) {
            file.setVisible(enabled);
            openFile.setVisible(enabled);
            label.setVisible(enabled);
        }

        private void disable() {
            setEnabled(false);
        }

        private void enable() {
            setEnabled(true);
        }

    }

    private FileChooserComponents addFileChooser(
            JPanel contentPane,
            String label,
            String hoverText,
            String currentFile,
            ImageIcon imageIcon,
            String chooserLabel,
            @MagicConstant(intValues = {
                    JFileChooser.FILES_ONLY,
                    JFileChooser.DIRECTORIES_ONLY,
                    JFileChooser.FILES_AND_DIRECTORIES,
            }) int fileSelectionMode
    ) {
        final JPanel row = new JPanel();
        row.setLayout(new BorderLayout());
        final JLabel fileLabel = new JLabel(label);
        fileLabel.setToolTipText(hoverText);
        row.add(fileLabel, BorderLayout.WEST);
        final JTextField file = new JTextField(currentFile);
        row.add(file, BorderLayout.CENTER);
        final JButton openFile = new JButton(imageIcon);
        openFile.addActionListener(
                a -> {
                    final JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File(file.getText()));
                    chooser.setDialogTitle(chooserLabel);
                    chooser.setFileSelectionMode(fileSelectionMode);
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setVisible(true);

                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        final File newFile = chooser.getSelectedFile();
                        file.setText(newFile.toString());
                    }
                });
        row.add(openFile, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
        contentPane.add(row);
        return new FileChooserComponents(file, openFile, fileLabel);
    }

    private JScrollPane createGeneralTab() {
        LegupPreferences prefs = LegupPreferences.getInstance();
        JScrollPane scrollPane = new JScrollPane();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        addRowLabel(contentPane, "General Preferences");

        workDirectory = addFileChooser(
                contentPane,
                "Work Directory",
                "This is where the open and save dialogs will open to.",
                LegupPreferences.workDirectory(),
                new ImageIcon(folderIcon),
                "Choose work directory",
                JFileChooser.DIRECTORIES_ONLY
        );

        fullScreen = addDefaultCheckBox(
                "Full Screen",
                LegupPreferences.startFullScreen(),
                "If checked this starts LEGUP in full screen.",
                contentPane
        );
        autoUpdate = addDefaultCheckBox(
                "Automatically Check for Updates",
                LegupPreferences.autoUpdate(),
                "If checked this automatically checks for updates on startup of Legup",
                contentPane
        );
        addRowLabel(contentPane, "Board View Preferences");

        showMistakes = addDefaultCheckBox(
                "Show Mistakes",
                LegupPreferences.showMistakes(),
                "If checked this show incorrectly applied rule applications in red on the board",
                contentPane
        );
        showAnnotations = addDefaultCheckBox(
                "Show Annotations",
                LegupPreferences.showAnnotations(),
                "If checked this show incorrectly applied rule applications in red on the board",
                contentPane
        );

        addRowLabel(contentPane, "Tree View Preferences");

        allowDefault = addDefaultCheckBox(
                "Allow Default Rule Applications",
                LegupPreferences.allowDefaultRules(),
                "If checked this automatically applies a rule where it can on the board",
                contentPane
        );
        generateCases = addDefaultCheckBox(
                "Automatically Generate Cases",
                LegupPreferences.autoGenerateCases(),
                "If checked this automatically generates all cases for a case rule",
                contentPane
        );
        immFeedback = addDefaultCheckBox(
                "Provide Immediate Feedback",
                LegupPreferences.immediateFeedback(),
                "If checked this will update the colors of the tree view elements immediately",
                contentPane
        );

        addRowLabel(contentPane, "Instructor Preferences");

        immFeedback = addDefaultCheckBox(
                "Instructor Mode",
                LegupPreferences.immediateFeedback(),
                "Currently unimplemented, this does nothing right now",
                contentPane
        );

        addRowLabel(contentPane, "Color Preferences");

        colorBlind = addDefaultCheckBox(
                "Deuteranomaly (Red/Green Colorblindness)",
                LegupPreferences.colorBlind(),
                "This turns colorblind mode on and off",
                contentPane
        );
        darkMode = addDefaultCheckBox(
                "Dark Mode",
                LegupPreferences.darkMode(),
                "This turns dark mode on and off",
                contentPane
        );
        customColorTheme = addDefaultCheckBox(
                "Custom Color Theme",
                LegupPreferences.useCustomColorTheme(),
                "This turns custom color theme on and off",
                contentPane
        );
        colorThemeFile = addFileChooser(
                contentPane,
                "Color Theme File",
                "This is the color theme LEGUP will use.",
                LegupPreferences.colorThemeFile(),
                new ImageIcon(folderIcon),
                "Choose color theme file",
                JFileChooser.FILES_ONLY
        );

        // Colorblind / dark mode / file chooser exclusion due to custom color theme
        customColorTheme.addActionListener(event -> {
            colorBlind.setEnabled(!customColorTheme.isSelected());
            darkMode.setEnabled(!customColorTheme.isSelected());
            colorThemeFile.setEnabled(customColorTheme.isSelected());
        });
        colorBlind.setEnabled(!customColorTheme.isSelected());
        darkMode.setEnabled(!customColorTheme.isSelected());
        colorThemeFile.setEnabled(customColorTheme.isSelected());

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

    /**
     * Creates a JPanel that represents a single row for a rule in the rule list. Each row displays
     * the rule's name and an area for showing keyboard shortcuts associated with the rule. The
     * keyboard shortcuts are dynamically updated based on user input.
     *
     * @param rule the rule object to be displayed
     * @return a JPanel representing the row for the rule
     */
    private JPanel createRuleRow(Rule rule) {
        JPanel ruleRow = new JPanel();
        ruleRow.setLayout(new BorderLayout());

        JLabel ruleLabel = new JLabel(rule.getRuleName());
        ruleRow.add(ruleLabel, BorderLayout.WEST);

        JLabel ruleAcc = new JLabel();
        ruleAcc.setHorizontalAlignment(JLabel.CENTER);
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

    /**
     * Creates a JPanel containing a left-aligned label with the specified text. This label is
     * typically used for section headings or descriptive text in the preferences dialog.
     *
     * @param text the text to be displayed on the label
     * @return a JPanel containing the left-aligned label
     */
    private JPanel createLeftLabel(String text) {
        JPanel labelRow = new JPanel();
        labelRow.setLayout(new BorderLayout());
        JLabel label = new JLabel(text);
        label.setFont(UIManager.getFont("Legup.prefsHeadingFont"));
        label.setHorizontalAlignment(JLabel.LEFT);
        labelRow.add(label, BorderLayout.WEST);

        labelRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, labelRow.getPreferredSize().height));
        return labelRow;
    }

    /**
     * Creates a JSeparator with a maximum height of 5 pixels. This separator is used to visually
     * divide sections in the preferences dialog.
     *
     * @return a JSeparator with a fixed height
     */
    private JSeparator createLineSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        return separator;
    }

    /**
     * Applies the current user preferences and updates the associated components. This method
     * retrieves user preferences from the dialog's components and stores them in the {@link
     * LegupPreferences} instance. It also updates the rule panels in the rules frame if it is not
     * null.
     */
    public void applyPreferences() {
        LegupPreferences prefs = LegupPreferences.getInstance();
        prefs.setUserPref(LegupPreferences.LegupPreference.WORK_DIRECTORY, workDirectory.file.getText());
        prefs.setUserPref(LegupPreferences.LegupPreference.START_FULL_SCREEN, fullScreen.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.AUTO_UPDATE, autoUpdate.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.DARK_MODE, darkMode.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.USE_CUSTOM_COLOR_THEME, customColorTheme.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.SHOW_MISTAKES, showMistakes.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.SHOW_ANNOTATIONS, showAnnotations.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.ALLOW_DEFAULT_RULES, allowDefault.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.AUTO_GENERATE_CASES, generateCases.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.IMMEDIATE_FEEDBACK, immFeedback.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.COLOR_BLIND, colorBlind.isSelected());
        prefs.setUserPref(LegupPreferences.LegupPreference.COLOR_THEME_FILE, colorThemeFile.file.getText());

        if (rulesFrame != null) {
            rulesFrame.getCasePanel().updateRules();
            rulesFrame.getDirectRulePanel().updateRules();
            rulesFrame.getContradictionPanel().updateRules();
        }

        updateColorTheme(prefs);
    }
}
