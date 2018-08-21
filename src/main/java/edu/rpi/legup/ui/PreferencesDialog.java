package edu.rpi.legup.ui;

import edu.rpi.legup.app.Config;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialBorders;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreferencesDialog extends JDialog {

    private final static Logger LOGGER = Logger.getLogger(PreferencesDialog.class.getName());

    public PreferencesDialog(LegupUI legupUI) {
        super(legupUI);

        setTitle("Preferences");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        JScrollPane generalTab = createGeneralTab();

        tabbedPane.addTab("General", generalTab);

        Config config = GameBoardFacade.getInstance().getConfig();
        try {

            for (String puzzleName : config.getPuzzleNames()) {
                String qualifiedClassName = config.getPuzzleClassForName(puzzleName);

                Class<?> c = Class.forName(qualifiedClassName);
                Constructor<?> cons = c.getConstructor();
                Puzzle puzzle = (Puzzle) cons.newInstance();
                tabbedPane.addTab(puzzleName, createPuzzleTab(puzzle));
            }
        } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Cannot create puzzle preferences");
        }
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(null);
        bottomPanel.setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        toolbar.setBorder(null);
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(l -> {
            this.setVisible(false);
        });
        toolbar.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(l -> {
            this.setVisible(false);
        });
        toolbar.add(cancelButton);
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(l -> {

        });
        toolbar.add(applyButton);
        bottomPanel.add(toolbar, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        setSize(600, 400);
        setLocationRelativeTo(legupUI);
        setVisible(true);
    }

    private JScrollPane createGeneralTab() {
        JScrollPane scrollPane = new JScrollPane();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        contentPane.add(new JLabel("Legup Preferences"));
        contentPane.add(createLineSeparator());

        JCheckBox workDir = new JCheckBox("Work Directory", false);
        workDir.setToolTipText("This is where the open and save dialogs will open to.");
        contentPane.add(workDir);
        JCheckBox fullScreen = new JCheckBox("Full Screen", false);
        fullScreen.setToolTipText("If checked this starts Legup in full screen.");
        contentPane.add(fullScreen);
        JCheckBox autoUpdate = new JCheckBox("Automatically Check for Updates", true);
        autoUpdate.setToolTipText("If checked this automatically checks for updates on startup of Legup");
        contentPane.add(autoUpdate);
        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        contentPane.add(new JLabel("Board View Preferences"));
        contentPane.add(createLineSeparator());
        JCheckBox showMistakes = new JCheckBox("Show Mistakes", true);
        showMistakes.setToolTipText("If checked this show incorrectly applied rule applications in red on the board");
        contentPane.add(showMistakes);
        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        contentPane.add(new JLabel("Tree View Preferences"));
        contentPane.add(createLineSeparator());

        JCheckBox allowDefault = new JCheckBox("Allow Default Rule Applications", false);
        allowDefault.setToolTipText("If checked this automatically applies a rule where it can on the board");
        allowDefault.setEnabled(false);
        contentPane.add(allowDefault);

        JCheckBox generateCases = new JCheckBox("Automatically Generate Cases", true);
        generateCases.setToolTipText("If checked this automatically generates all cases for a case rule");
        contentPane.add(generateCases);

        JCheckBox immFeedback = new JCheckBox("Provide Immediate Feedback", true);
        immFeedback.setToolTipText("If checked this will update the colors of the tree view elements immediately");
        contentPane.add(immFeedback);

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

        contentPane.add(createLeftLabel("Basic Rules"));
        contentPane.add(createLineSeparator());
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        for(Rule rule : puzzle.getBasicRules()) {
            JPanel ruleRow = createRuleRow(rule);
            contentPane.add(ruleRow);
            contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        contentPane.add(createLeftLabel("Case Rules"));
        contentPane.add(createLineSeparator());
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        for(Rule rule : puzzle.getCaseRules()) {
            JPanel ruleRow = createRuleRow(rule);
            contentPane.add(ruleRow);
            contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        contentPane.add(createLeftLabel("Contradiction Rules"));
        contentPane.add(createLineSeparator());
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        for(Rule rule : puzzle.getContradictionRules()) {
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
        ruleAcc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ruleAcc.requestFocusInWindow();
            }
        });

        ruleAcc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                String combo = "";
                if(e.isControlDown()) {
                    combo += "Ctrl + ";
                } else if(e.isShiftDown()) {
                    combo += "Shift + ";
                } else if(e.isAltDown()) {
                    combo += "Alt + ";
                }
                if(keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT ) {
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

        labelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, labelRow.getPreferredSize().height));
        return labelRow;
    }

    private JSeparator createLineSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        return separator;
    }
}
