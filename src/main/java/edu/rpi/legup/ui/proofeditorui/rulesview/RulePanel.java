package edu.rpi.legup.ui.proofeditorui.rulesview;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.WrapLayout;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Abstract base class for panels displaying rules. Each subclass will represent a specific type of
 * rule panel (e.g., DirectRulePanel, CaseRulePanel).
 */
public abstract class RulePanel extends JPanel {
    protected ImageIcon icon;
    protected String name;
    protected String toolTip;

    protected RuleButton[] ruleButtons;
    protected JPanel searchBarPanel;
    JTextField textField;
    protected RuleFrame ruleFrame;
    protected List<? extends Rule> rules;

    /**
     * RulePanel Constructor creates a RulePanel
     *
     * @param ruleFrame rule frame that this RulePanel is contained in
     */
    public RulePanel(RuleFrame ruleFrame) {
        this.ruleFrame = ruleFrame;
        this.rules = new ArrayList<>();
        setLayout(new WrapLayout());
    }

    /**
     * Gets the array of rule buttons
     *
     * @return rule ruleButtons
     */
    public RuleButton[] getRuleButtons() {
        return ruleButtons;
    }

    /**
     * Sets the rules for this rule panel
     *
     * @param rules list of the rules
     */
    public void setRules(List<? extends Rule> rules) {
        this.rules = rules;
        clearButtons();

        ruleButtons = new RuleButton[rules.size()];

        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);

            ruleButtons[i] = new RuleButton(rule);
            ruleButtons[i].setPreferredSize(
                    new Dimension(150, 150)); // adjust the size of each RuleButton

            if (rule.getRuleName().length() > 18) {
                ruleButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 11));
            }
            if (rule.getRuleName().length() > 20) {
                ruleButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 10));
            }
            System.out.println(ruleButtons[i].getFont().getName());

            ruleButtons[i].setHorizontalTextPosition(JButton.CENTER);
            ruleButtons[i].setVerticalTextPosition(JButton.BOTTOM);

            ruleFrame.getButtonGroup().add(ruleButtons[i]);
            ruleButtons[i].setToolTipText(
                    rule.getRuleName() + ": " + rule.getDescription()); // showing description
            ruleButtons[i].addActionListener(ruleFrame.getController());
            add(ruleButtons[i]);
        }
        revalidate();
    }

    /** Updates the rules displayed by reloading images and setting the rules again. */
    public void updateRules() {
        for (Rule rule : rules) {
            rule.loadImage();
        }
        setRules(rules);
    }

    /**
     * Search a certain rule in all the puzzles and set it for the searchBarPanel
     *
     * @param puzzle puzzle where the rule is being searched for
     * @param ruleName rule that is being compared to each puzzle
     *     <p>This function is the searching algorithm for "public void setSearchBar(Puzzle
     *     allPuzzle)" (below)
     *     <p>It takes two param Puzzle puzzle and String ruleName puzzle contains rules, this
     *     function will compare each rule of puzzle with ruleName, to find exact same, similar
     *     rules, or all the rules with same start letter (if input is a signal letter)
     */
    public void searchForRule(Puzzle puzzle, String ruleName) {

        List<List<? extends Rule>> allrules = new ArrayList<List<? extends Rule>>(3);
        allrules.add(0, puzzle.getDirectRules());
        allrules.add(1, puzzle.getCaseRules());
        allrules.add(2, puzzle.getContradictionRules());

        ruleButtons = new RuleButton[100];
        int similarfound = 0;

        for (int i = 0; i < allrules.size(); i++) {
            for (int j = 0; j < allrules.get(i).size(); j++) {
                Rule rule = allrules.get(i).get(j);
                if ((ruleName).equals(rule.getRuleName().toUpperCase())) {

                    ruleButtons[0] = new RuleButton(rule);
                    ruleFrame.getButtonGroup().add(ruleButtons[0]);

                    ruleButtons[0].setPreferredSize(
                            new Dimension(150, 150)); // adjust the size of each RuleButton
                    ruleButtons[0].setHorizontalTextPosition(JButton.CENTER);
                    ruleButtons[0].setVerticalTextPosition(JButton.BOTTOM);

                    ruleButtons[0].setToolTipText(
                            rule.getRuleName() + ": " + rule.getDescription());
                    ruleButtons[0].addActionListener(ruleFrame.getController());
                    add(ruleButtons[0]);
                    revalidate();
                    return;

                } else {
                    if (similarityCheck(ruleName, rule.getRuleName().toUpperCase()) > 0.2) {
                        ruleButtons[similarfound] = new RuleButton(rule);
                        ruleFrame.getButtonGroup().add(ruleButtons[similarfound]);

                        ruleButtons[similarfound].setPreferredSize(
                                new Dimension(150, 150)); // adjust the size of each RuleButton
                        ruleButtons[similarfound].setHorizontalTextPosition(JButton.CENTER);
                        ruleButtons[similarfound].setVerticalTextPosition(JButton.BOTTOM);

                        ruleButtons[similarfound].setToolTipText(
                                rule.getRuleName() + ": " + rule.getDescription());
                        ruleButtons[similarfound].addActionListener(ruleFrame.getController());
                        add(ruleButtons[similarfound]);
                        similarfound += 1;
                        revalidate();
                    } else {
                        if ((ruleName.charAt(0)) == (rule.getRuleName().toUpperCase()).charAt(0)) {
                            ruleButtons[similarfound] = new RuleButton(rule);
                            ruleFrame.getButtonGroup().add(ruleButtons[similarfound]);

                            ruleButtons[similarfound].setPreferredSize(
                                    new Dimension(150, 150)); // adjust the size of each RuleButton
                            ruleButtons[similarfound].setHorizontalTextPosition(JButton.CENTER);
                            ruleButtons[similarfound].setVerticalTextPosition(JButton.BOTTOM);

                            ruleButtons[similarfound].setToolTipText(
                                    rule.getRuleName() + ": " + rule.getDescription());
                            ruleButtons[similarfound].addActionListener(ruleFrame.getController());
                            add(ruleButtons[similarfound]);
                            similarfound += 1;
                            revalidate();
                        }
                    }
                }
            }
        }

        if (ruleButtons[0] == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please input the correct rule name",
                    "Confirm",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings. This function will
     * take two para String s1 and String s2, which s1 is the user's input and s2 is the compared
     * really rule name
     *
     * @param s1 user's input
     * @param s2 the compared really rule name
     * @return a similarity degree between 0 and 1 similarityCheck will use a helper function to
     *     calculate a similarity degree(from 0 to 1). closer to 0 means less similar, and closer to
     *     1 means more similar.
     */
    public static double similarityCheck(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Help function for similarityCheck();
     *
     * @param s1 user's input
     * @param s2 the compared really rule name
     * @return a similarity degree between 0 and 1
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    /**
     * Sets the search bar for SearchBarPanel search bar allows user to input a name to get relative
     * rules once a name is entered and click ok will load (a/several) rule icon, which has all the
     * functions just as other rule icons.
     *
     * @param allPuzzle name of rule input
     */
    public void setSearchBar(Puzzle allPuzzle) {

        searchBarPanel = new JPanel(new FlowLayout(SwingConstants.LEADING, 6, 6));

        textField = new JTextField();
        ruleFrame.addComponentListener(
                new ComponentAdapter() {
                    public void componentResized(ComponentEvent componentEvent) {
                        Component c = componentEvent.getComponent();
                        textField.setColumns((8 + (c.getWidth() - 250) / 10) - 1);
                    }
                });

        add(searchBarPanel);
        JLabel findLabel = new JLabel("Search:");
        searchBarPanel.add(findLabel);
        searchBarPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        searchBarPanel.add(textField);
        searchBarPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        JButton findButton = new JButton("Go");
        ActionListener action =
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (ruleButtons != null) {
                            for (int i = 0; i != ruleButtons.length; i++) {
                                if (ruleButtons[i] == null) {
                                    continue;
                                }
                                ruleButtons[i].removeActionListener(ruleFrame.getController());
                            }
                        }
                        String inputRule = textField.getText().toUpperCase().trim();

                        if (!inputRule.isEmpty()) {
                            if (ruleButtons != null) {

                                for (int x = 0; x < ruleButtons.length; ++x) {
                                    if (ruleButtons[x] == null) {
                                        continue;
                                    }
                                    remove(ruleButtons[x]);
                                }
                            }
                            searchForRule(allPuzzle, inputRule);
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Please give a name",
                                    "Confirm",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                };
        textField.addActionListener(action);
        findButton.addActionListener(action);
        searchBarPanel.add(findButton);
    }

    /**
     * Sets the selection by the specified rule
     *
     * @param rule rule to set the selection to
     */
    public void setSelectionByRule(Rule rule) {
        if (ruleButtons != null) {
            for (int i = 0; i < ruleButtons.length; i++) {
                if (rules.get(i).equals(rule)) {
                    ruleButtons[i].setSelected(true);
                    break;
                }
            }
        }
    }

    /** Clears the rule buttons off this panel */
    protected void clearButtons() {
        if (ruleButtons != null) {
            removeAll();
            for (int x = 0; x < ruleButtons.length; ++x) {
                ruleButtons[x].removeActionListener(ruleFrame.getController());
            }
        }
    }

    /**
     * Gets the rules associated with this panel
     *
     * @return the rules
     */
    public List<? extends Rule> getRules() {
        return rules;
    }

    /**
     * Gets the icon associated with this panel
     *
     * @return The ImageIcon associated with this panel
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Sets the ImageIcon associated with this panel
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * Gets the name of this panel
     *
     * @return the name of this panel in a String
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this panel
     *
     * @param name the name to set for this panel
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the tooltip text associated with this panel
     *
     * @return the tooltip text of this panel
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * Sets the tooltip text for this panel
     *
     * @param toolTip the tooltip text to set for this panel
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
}
