package ui.treeview;

import controller.TreeController;
import model.gameboard.Board;
import model.rules.Rule;
import model.tree.Tree;
import ui.LegupUI;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class TreePanel extends JPanel
{
    public boolean modifiedSinceSave = false;
    public boolean modifiedSinceUndoPush = false;
    public int updateStatusTimer = 0;

    private JPanel main;
    private TreeView treeView;
    private TreeToolbarPanel toolbar;
    private LegupUI legupUI;

    private JLabel status;
    private Rule curRuleApplied = null;

    public TreePanel(LegupUI legupUI)
    {
        this.legupUI = legupUI;

        main = new JPanel();

        main.setLayout(new BorderLayout());

        TreeController treeController = new TreeController();
        treeView = new TreeView(treeController);
        treeController.setViewer(treeView);

        toolbar = new TreeToolbarPanel(this);

        main.add(toolbar, BorderLayout.WEST);
        main.add(treeView, BorderLayout.CENTER);

        status = new JLabel();
        status.setPreferredSize(new Dimension(150,20));
        main.add(status, BorderLayout.SOUTH);

        TitledBorder title = BorderFactory.createTitledBorder("TreePanel");
        title.setTitleJustification(TitledBorder.CENTER);
        main.setBorder(title);

        setLayout(new BorderLayout());
        add(main);

        updateStatusTimer = 0;
    }

    public void repaintTreeView(Tree tree)
    {
        treeView.updateTreeView(tree);
    }

    /**
     * Merge the selected states
     */
    public void mergeStates()
    {
        treeView.mergeStates();
    }

    /**
     * Delete the child subtree starting at the current state
     */
    public void delChildAtCurrentState()
    {
        treeView.delChildAtCurrentState();
    }


    public void boardDataChanged(Board board)
    {
        modifiedSinceSave = true;
        modifiedSinceUndoPush = true;
        updateStatus();
        //colorTransitions();
    }

    public void updateStatus()
    {
        updateStatusTimer = ((updateStatusTimer - 1) > 0) ? (updateStatusTimer - 1) : 0;
        if(updateStatusTimer > 0)
        {
            return;
        }
        this.status.setText("");
    }

    public void updateStatus(String statusString)
    {
        status.setForeground(Color.BLACK);
        status.setText(statusString);
    }

    public void updateError(String error)
    {
        status.setForeground(Color.RED);
        status.setText(error);
    }

    public TreeView getTreeView()
    {
        return treeView;
    }
}
