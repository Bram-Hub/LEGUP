package edu.rpi.legup.ui.rulesview;

import edu.rpi.legup.controller.RuleController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.Rule;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class RuleFrame extends JPanel {
    private static final String checkBox = "<font style=\"color:#00CD00\"> \u2714 </font>";
    private static final String xBox = "<font style=\"color:#FF0000\"> \u2718 </font>";
    private static final String htmlHead = "<html>";
    private static final String htmlTail = "</html>";

    private BasicRulePanel basicRulePanel;
    private ContradictionRulePanel contradictionPanel;
    private CaseRulePanel casePanel;

    private JTabbedPane tabbedPane;
    private JLabel status;
    private ButtonGroup buttonGroup;

    private RuleController controller;

    public RuleFrame(RuleController controller) {
        this.controller = controller;

        this.tabbedPane = new JTabbedPane();
        this.status = new JLabel();
        this.buttonGroup = new ButtonGroup();

        basicRulePanel = new BasicRulePanel(this);

        tabbedPane.addTab(basicRulePanel.getName(), basicRulePanel.getIcon(), new JScrollPane(basicRulePanel), basicRulePanel.getToolTip());

        casePanel = new CaseRulePanel(this);
        tabbedPane.addTab(casePanel.name, casePanel.icon, new JScrollPane(casePanel), casePanel.toolTip);

        contradictionPanel = new ContradictionRulePanel(this);
        tabbedPane.addTab(contradictionPanel.name, contradictionPanel.icon, new JScrollPane(contradictionPanel), contradictionPanel.toolTip);

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 256));
        setPreferredSize(new Dimension(330, 256));

        add(tabbedPane);
        add(status, BorderLayout.SOUTH);

        TitledBorder title = BorderFactory.createTitledBorder("Rules");
        title.setTitleJustification(TitledBorder.CENTER);
        setBorder(title);
    }

    /**
     * Gets the button group for the rule frame
     *
     * @return button group
     */
    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setSelectionByRule(Rule rule) {
        basicRulePanel.setSelectionByRule(rule);
        casePanel.setSelectionByRule(rule);
        contradictionPanel.setSelectionByRule(rule);
    }

    /**
     * Reset the rules button and status string
     */
    public void resetRuleButtons() {
        resetStatus();
    }

    /**
     * Reset the status label to the empty string
     */
    public void resetStatus() {
        //((GridUI)GameBoardFacade.getInstance().getLegupUI()).getTreePanel().updateStatus();
    }

    /**
     * Resets the dimension of the rule frame
     */
    public void resetSize() {
        int buttonWidth = ((RulePanel) tabbedPane.getSelectedComponent()).getRuleButtons()[0].getWidth();
        this.setMinimumSize(new Dimension(2 * buttonWidth + 64, this.getHeight()));
    }

    /**
     * Set the status label to a value. Use resetStatus to clear it.
     *
     * @param check true iff we want a check box, if false we'll have a red x box
     * @param text  the text we're setting the label to display
     */
    public void setStatus(boolean check, String text) {
        String box = (check ? checkBox : xBox);
        //status.setText(htmlHead + box + text + htmlTail);
        //((GridUI)GameBoardFacade.getInstance().getLegupUI()).getTreePanel().getStatus().setText(htmlHead + box + text + htmlTail);
    }

    /**
     * Sets all the rules for the rule frame
     *
     * @param puzzle edu.rpi.legup.puzzle game
     */
    public void setRules(Puzzle puzzle) {
        basicRulePanel.setRules(puzzle.getBasicRules());
        contradictionPanel.setRules(puzzle.getContradictionRules());
        casePanel.setRules(puzzle.getCaseRules());
    }

    /**
     * Board puzzleElement has changed
     *
     * @param board board state
     */
    public void boardDataChanged(Board board) {
        this.resetStatus();
    }

    /**
     * Gets the RuleController for this RuleFrame
     *
     * @return rule controller
     */
    public RuleController getController() {
        return controller;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public BasicRulePanel getBasicRulePanel() {
        return basicRulePanel;
    }

    public CaseRulePanel getCasePanel() {
        return casePanel;
    }

    public ContradictionRulePanel getContradictionPanel() {
        return contradictionPanel;
    }
}
