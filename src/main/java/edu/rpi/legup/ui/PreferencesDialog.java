package edu.rpi.legup.ui;

import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.color.ColorPreferences;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialBorders;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;
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

    private JTextField addFileChooser(
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
        return file;
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
                "Deuteranomaly(red/green colorblindness)",
                LegupPreferences.colorBlind(),
                "",
                contentPane
        );
        colorBlind =
                new JCheckBox(
                        "Deuteranomaly(red/green colorblindness)",
                        LegupPreferences.colorBlind());

        colorThemeFile = addFileChooser(
                contentPane,
                "Color Theme File",
                "This is the color theme LEGUP will use.",
                LegupPreferences.colorThemeFile(),
                new ImageIcon(folderIcon),
                "Choose color theme file",
                JFileChooser.FILES_ONLY
        );

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
        prefs.setUserPref(LegupPreferences.LegupPreference.USE_CUSTOM_COLOR_THEME, customColorTheme.isSelected());
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
        updateColorTheme(prefs);
    }
}
