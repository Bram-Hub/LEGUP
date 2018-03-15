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

        addChild = new JButton(new ImageIcon("images/Legup/AddChild.png"));
        delChild = new JButton(new ImageIcon("images/Legup/DelChild.png"));
        merge = new JButton(new ImageIcon("images/Legup/Merge.png"));
        collapse = new JButton(new ImageIcon("images/Legup/Collapse.png"));

        zoomIn = new JButton(new ImageIcon("images/Legup/Zoom In.png"));
        zoomOut = new JButton(new ImageIcon("images/Legup/Zoom Out.png"));
        zoomReset = new JButton(new ImageIcon("images/Legup/Normal Zoom.png"));
        zoomFit = new JButton(new ImageIcon("images/Legup/Best Fit.png"));

        add(addChild);
        addChild.addActionListener(this);
        addChild.setEnabled(false);
        addChild.setToolTipText("Finalize CaseRule");
        addChild.setBackground(Color.LIGHT_GRAY);

        add(delChild);
        delChild.addActionListener(this);
        delChild.setToolTipText("Remove currently selected node");
        delChild.setBackground(Color.LIGHT_GRAY);

        add(merge);
        merge.addActionListener(this);
        merge.setToolTipText("Merge nodes");
        merge.setBackground(Color.LIGHT_GRAY);

        add(collapse);
        collapse.addActionListener(this);
        collapse.setToolTipText("Collapse nodes");
        collapse.setBackground(Color.LIGHT_GRAY);

        add(zoomIn);
        zoomIn.addActionListener(this);
        zoomIn.setToolTipText("Zoom In");
        zoomIn.setBackground(Color.LIGHT_GRAY);

        add(zoomOut);
        zoomOut.addActionListener(this);
        zoomOut.setToolTipText("Zoom Out");
        zoomOut.setBackground(Color.LIGHT_GRAY);

        add(zoomReset);
        zoomReset.addActionListener(this);
        zoomReset.setToolTipText("Reset Zoom");
        zoomReset.setBackground(Color.LIGHT_GRAY);

        add(zoomFit);
        zoomFit.addActionListener(this);
        zoomFit.setToolTipText("Best Fit");
        zoomFit.setBackground(Color.LIGHT_GRAY);
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
            //treePanel.delChildAtCurrentState();
        }
        else if(e.getSource() == merge)
        {
            //treePanel.mergeStates();
        }
        else if(e.getSource() == collapse)
        {
            //treePanel.collapseStates();
        }
        else if(e.getSource() == zoomIn)
        {
            treePanel.getTreeView().zoomIn();
        }
        else if(e.getSource() == zoomOut)
        {
            treePanel.getTreeView().zoomOut();
        }
        else if(e.getSource() == zoomReset)
        {
            treePanel.getTreeView().zoomReset();
        }
        else if(e.getSource() == zoomFit)
        {
            treePanel.getTreeView().zoomFit();
        }
    }
}
