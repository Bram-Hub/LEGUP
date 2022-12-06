package edu.rpi.legup.ui.proofeditorui.rulesview;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
     * Gets the rule rule buttons
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
            ruleButtons[i].setPreferredSize(new Dimension(150,150));
            ruleButtons[i].setHorizontalTextPosition(JButton.CENTER);
            ruleButtons[i].setVerticalTextPosition(JButton.BOTTOM);
            ruleFrame.getButtonGroup().add(ruleButtons[i]);
            ruleButtons[i].setToolTipText(rule.getRuleName() + ": " + rule.getDescription());
            ruleButtons[i].addActionListener(ruleFrame.getController());
            add(ruleButtons[i]);
            //add(new JButton(rule.getRuleName())); // add name under relative rule

        }
        revalidate();
    }


    /**
     * Search a certain rule in all the puzzles and set it for the searchBarPanel
     *
     * @param puzzle, ruleName
     */
    public void searchForRule(Puzzle puzzle, String ruleName) {

        List<List<? extends Rule>> allrules = new ArrayList<List<? extends Rule>>(3);
        allrules.add(0, puzzle.getBasicRules());
        allrules.add(1, puzzle.getCaseRules());
        allrules.add(2, puzzle.getContradictionRules());

        ruleButtons = new RuleButton[100];
        int similarfound = 0;


        for(int i = 0; i < allrules.size(); i++) {
            for (int j = 0; j < allrules.get(i).size(); j++) {
                Rule rule = allrules.get(i).get(j);
                if ((ruleName).equals(rule.getRuleName().toUpperCase())) {

                    ruleButtons[0] = new RuleButton(rule);
                    ruleFrame.getButtonGroup().add(ruleButtons[0]);

                    ruleButtons[0].setToolTipText(rule.getRuleName() + ": " + rule.getDescription());
                    ruleButtons[0].addActionListener(ruleFrame.getController());
                    add(ruleButtons[0]);
                    revalidate();
                    return;

                }
                else if(similarityCheck(ruleName, rule.getRuleName().toUpperCase())>0.2){
                    ruleButtons[similarfound] = new RuleButton(rule);
                    ruleFrame.getButtonGroup().add(ruleButtons[similarfound]);

                    ruleButtons[similarfound].setToolTipText(rule.getRuleName() + ": " + rule.getDescription());
                    ruleButtons[similarfound].addActionListener(ruleFrame.getController());
                    add(ruleButtons[similarfound]);
                    similarfound+=1;
                    revalidate();
                }
                else if((ruleName.charAt(0)) == (rule.getRuleName().toUpperCase()).charAt(0)){
                    ruleButtons[similarfound] = new RuleButton(rule);
                    ruleFrame.getButtonGroup().add(ruleButtons[similarfound]);

                    ruleButtons[similarfound].setToolTipText(rule.getRuleName() + ": " + rule.getDescription());
                    ruleButtons[similarfound].addActionListener(ruleFrame.getController());
                    add(ruleButtons[similarfound]);
                    similarfound+=1;
                    revalidate();
                }
            }
        }

        if(ruleButtons[0] == null) {
            JOptionPane.showMessageDialog(null, "Please input the correct rule name", "Confirm", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarityCheck(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }
    /**
     * Help function for similarityCheck();
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    /**
     * UnFinished
     *
     * Sets the search bar for SearchBarPanel
     *
     */
    public void setSearchBar(Puzzle allPuzzle){

        searchBarPanel = new JPanel(new FlowLayout(SwingConstants.LEADING, 6, 6));
        add(searchBarPanel);
        JLabel findLabel = new JLabel("Search:");
        searchBarPanel.add(findLabel);
        searchBarPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        textField = new JTextField(8);
        searchBarPanel.add(textField);
        searchBarPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        JButton findButton = new JButton("Go");
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (ruleButtons != null) {
                    for(int i = 0; i != ruleButtons.length; i++) {
                        if(ruleButtons[i]==null){
                            continue;
                        }
                        ruleButtons[i].removeActionListener(ruleFrame.getController());
                    }
                }
                String inputRule = textField.getText().toUpperCase().trim();

                if (!inputRule.isEmpty()) {
                    if (ruleButtons != null) {

                        for (int x = 0; x < ruleButtons.length; ++x) {
                            if(ruleButtons[x]==null){
                                continue;
                            }
                            remove(ruleButtons[x]);
                        }
                    }
                    searchForRule(allPuzzle, inputRule);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please give a name","Confirm",JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
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

    /**
     * Clears the rule buttons off this panel
     */
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

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
}