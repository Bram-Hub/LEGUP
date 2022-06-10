package edu.rpi.legup.ui.treeview;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.history.AddTreeElementCommand;
import edu.rpi.legup.history.DeleteTreeElementCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.MergeCommand;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.ui.DynamicView;
import edu.rpi.legup.ui.LegupUI;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class TreePanel extends JPanel {
    public boolean modifiedSinceSave = false;
    public boolean modifiedSinceUndoPush = false;
    public int updateStatusTimer = 0;

    private JPanel main;
    private TreeView treeView;
    private TreeToolbarPanel toolbar;
    private LegupUI legupUI;

    private JLabel status;

    public TreePanel(LegupUI legupUI) {
        this.legupUI = legupUI;

        main = new JPanel();

        main.setLayout(new BorderLayout());

        TreeController treeController = new TreeController();
        treeView = new TreeView(treeController);
        treeController.setViewer(treeView);

        toolbar = new TreeToolbarPanel(this);

        DynamicView dynamicTreeView = new DynamicView(treeView);
        main.add(dynamicTreeView, BorderLayout.CENTER);
        dynamicTreeView.add(toolbar, BorderLayout.WEST);

        status = new JLabel();
        status.setPreferredSize(new Dimension(150, 15));
        dynamicTreeView.getZoomWrapper().add(status, BorderLayout.CENTER);

        TitledBorder title = BorderFactory.createTitledBorder("Proof Tree");
        title.setTitleJustification(TitledBorder.CENTER);
        main.setBorder(title);

        setLayout(new BorderLayout());
        add(main);

        updateStatusTimer = 0;
    }

    public void repaintTreeView(Tree tree) {
        treeView.updateTreeView(tree);
    }

    public void boardDataChanged(Board board) {
        modifiedSinceSave = true;
        modifiedSinceUndoPush = true;
        updateStatus();
        //colorTransitions();
    }

    public void updateStatus() {
        updateStatusTimer = ((updateStatusTimer - 1) > 0) ? (updateStatusTimer - 1) : 0;
        if (updateStatusTimer > 0) {
            return;
        }
        this.status.setText("");
    }

    public void updateStatus(String statusString) {
        status.setForeground(Color.BLACK);
        status.setFont(MaterialFonts.REGULAR);
        status.setText(statusString);
    }

    public void updateError(String error) {
        status.setForeground(Color.RED);
        status.setFont(MaterialFonts.ITALIC);
        status.setText(error);
    }

    public TreeView getTreeView() {
        return treeView;
    }

    public void add() {
        TreeViewSelection selection = treeView.getSelection();

        AddTreeElementCommand add = new AddTreeElementCommand(selection);
        if (add.canExecute()) {
            add.execute();
            GameBoardFacade.getInstance().getHistory().pushChange(add);
        } else {
            updateError(add.getError());
        }
    }

    public void delete() {
        TreeViewSelection selection = treeView.getSelection();

        DeleteTreeElementCommand del = new DeleteTreeElementCommand(selection);
        if (del.canExecute()) {
            del.execute();
            GameBoardFacade.getInstance().getHistory().pushChange(del);
        } else {
            updateError(del.getError());
        }
    }

    public void merge() {
        TreeViewSelection selection = treeView.getSelection();

        ICommand merge = new MergeCommand(selection);
        if (merge.canExecute()) {
            merge.execute();
            GameBoardFacade.getInstance().getHistory().pushChange(merge);
        } else {
            updateError(merge.getError());
        }
    }

    public void collapse() {
        TreeViewSelection selection = treeView.getSelection();
        for (TreeElementView view : selection.getSelectedViews()) {
            view.setCollapsed(!view.isCollapsed());
        }
    }
}
