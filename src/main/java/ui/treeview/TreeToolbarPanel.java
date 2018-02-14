package ui.treeview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeToolbarPanel extends JPanel implements ActionListener
{
    private TreePanel treePanel;
    private JButton addChild, delChild, merge, collapse, zoomIn, zoomOut, zoomReset, zoomFit;

    /**
     * TreeToolbarPanel Constructor - creates the tree tool mBar panel
     */
    public TreeToolbarPanel(TreePanel treePanel)
    {
        this.treePanel = treePanel;

        this.setLayout(new GridLayout(4, 2));

        addChild = new JButton(new ImageIcon("images/AddChild.png"));
        delChild = new JButton(new ImageIcon("images/DelChild.png"));
        merge = new JButton(new ImageIcon("images/Merge.png"));
        collapse = new JButton(new ImageIcon("images/Collapse.png"));

        zoomIn = new JButton(new ImageIcon("images/Zoom In.png"));
        zoomOut = new JButton(new ImageIcon("images/Zoom Out.png"));
        zoomReset = new JButton(new ImageIcon("images/Normal Zoom.png"));
        zoomFit = new JButton(new ImageIcon("images/Best Fit.png"));

        add(addChild);
        addChild.addActionListener(this);
        addChild.setEnabled(false);
        addChild.setToolTipText("Finalize CaseRule");

        add(delChild);
        delChild.addActionListener(this);
        delChild.setToolTipText("Remove currently selected node");

        add(merge);
        merge.addActionListener(this);
        merge.setToolTipText("Merge nodes");

        add(collapse);
        collapse.addActionListener(this);
        collapse.setToolTipText("Collapse nodes");

        add(zoomIn);
        zoomIn.addActionListener(this);
        zoomIn.setToolTipText("Zoom In");

        add(zoomOut);
        zoomOut.addActionListener(this);
        zoomOut.setToolTipText("Zoom Out");

        add(zoomReset);
        zoomReset.addActionListener(this);
        zoomReset.setToolTipText("Reset Zoom");

        add(zoomFit);
        zoomFit.addActionListener(this);
        zoomFit.setToolTipText("Best Fit");
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == addChild)
        {
//            BoardState cur = Legup.getCurrentState();
//            cur.setCaseRuleJustification(cur.getSingleParentState().getFirstChild().getCaseRuleJustification());
//            addChildAtCurrentState();
        }
        else if(e.getSource() == delChild)
        {
            treePanel.delChildAtCurrentState();
        }
        else if(e.getSource() == merge)
        {
            treePanel.mergeStates();
        }
        else if(e.getSource() == collapse)
        {
            //treePanel.collapseStates();
        }
        else if(e.getSource() == zoomIn)
        {
            //zoomIn();
        }
        else if(e.getSource() == zoomOut)
        {
            //treeView.zoomOut();
        }
        else if(e.getSource() == zoomReset)
        {
            //treeView.zoomReset();
        }
        else if(e.getSource() == zoomFit)
        {
            //treeView.zoomFit();
        }
    }
}
