package edu.rpi.legup.ui.proofeditorui.rulesview;

import edu.rpi.legup.controller.RuleController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.ui.lookandfeel.components.MaterialTabbedPaneUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import javax.swing.border.TitledBorder;

public class RuleFrame extends JPanel {
    private static final String checkBox = "<font style=\"color:#00CD00\"> \u2714 </font>";
    private static final String xBox = "<font style=\"color:#FF0000\"> \u2718 </font>";
    private static final String htmlHead = "<html>";
    private static final String htmlTail = "</html>";

    private DirectRulePanel DirectRulePanel;
    private ContradictionRulePanel contradictionPanel;
    private CaseRulePanel casePanel;

    private SearchBarPanel searchPanel;

    private JTabbedPane tabbedPane;
    private JLabel status;
    private ButtonGroup buttonGroup;

    private RuleController controller;

    public RuleFrame(RuleController controller) {

        MaterialTabbedPaneUI tabOverride = new MaterialTabbedPaneUI() {
            //this prevents the tabs from moving around when you select them
            @Override
            protected boolean shouldRotateTabRuns(int i) {
                return false;
            }
        };

        this.controller = controller;

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setUI(tabOverride);
        this.status = new JLabel();
        this.buttonGroup = new ButtonGroup();

        DirectRulePanel = new DirectRulePanel(this);
        JScrollPane newbrp = new JScrollPane(DirectRulePanel);
        newbrp.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab(DirectRulePanel.getName(), DirectRulePanel.getIcon(), newbrp, DirectRulePanel.getToolTip());

        casePanel = new CaseRulePanel(this);
        JScrollPane newcp = new JScrollPane(casePanel);
        newcp.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab(casePanel.name, casePanel.icon, newcp, casePanel.toolTip);

        contradictionPanel = new ContradictionRulePanel(this);
        JScrollPane newp = new JScrollPane(contradictionPanel);
        newp.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab(contradictionPanel.name, contradictionPanel.icon, newp, contradictionPanel.toolTip);

        searchPanel = new SearchBarPanel(this);
        JScrollPane newsp = new JScrollPane(searchPanel);
        newsp.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab(searchPanel.name, searchPanel.icon, newsp, searchPanel.toolTip);

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
        DirectRulePanel.setSelectionByRule(rule);
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
        DirectRulePanel.setRules(puzzle.getDirectRules());
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

    public DirectRulePanel getDirectRulePanel() {
        return DirectRulePanel;
    }

    public CaseRulePanel getCasePanel() {
        return casePanel;
    }

    public ContradictionRulePanel getContradictionPanel() {
        return contradictionPanel;
    }

    public SearchBarPanel getSearchPanel() {
        return searchPanel;
    }
}
