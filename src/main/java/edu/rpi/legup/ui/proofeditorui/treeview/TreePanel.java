package edu.rpi.legup.ui.proofeditorui.treeview;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.history.AddTreeElementCommand;
import edu.rpi.legup.history.DeleteTreeElementCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.MergeCommand;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.ui.DynamicView;
import edu.rpi.legup.ui.DynamicViewType;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * {@code TreePanel} is a JPanel that manages and displays a tree view with associated toolbar and
 * status information. It provides methods to interact with the tree view, such as adding, deleting,
 * and merging tree elements, and updating the status based on actions performed.
 */
public class TreePanel extends JPanel {
    public boolean modifiedSinceSave = false;
    public boolean modifiedSinceUndoPush = false;
    public int updateStatusTimer = 0;

    private JPanel main;
    private TreeView treeView;
    private TreeToolbarPanel toolbar;
    // private LegupUI legupUI;

    private JLabel status;

    /** Constructs a {@code TreePanel} and initializes the UI components. */
    public TreePanel(/*LegupUI legupUI*/ ) {
        // this.legupUI = legupUI;

        main = new JPanel();

        main.setLayout(new BorderLayout());

        TreeController treeController = new TreeController();
        treeView = new TreeView(treeController);
        treeController.setViewer(treeView);

        toolbar = new TreeToolbarPanel(this);

        DynamicView dynamicTreeView = new DynamicView(treeView, DynamicViewType.PROOF_TREE);
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

    /**
     * Repaints the tree view with the provided {@link Tree} object
     *
     * @param tree the {@link Tree} object to update the view with
     */
    public void repaintTreeView(Tree tree) {
        treeView.updateTreeView(tree);
    }

    /**
     * Updates the status of the panel based on changes to the {@link Board}
     *
     * @param board the {@link Board} object representing the current board state
     */
    public void boardDataChanged(Board board) {
        modifiedSinceSave = true;
        modifiedSinceUndoPush = true;
        updateStatus();
        // colorTransitions();
    }

    /**
     * Updates the status display based on the status timer. If the timer is greater than 0, the
     * status will not be updated. Otherwise, it clears the status text.
     */
    public void updateStatus() {
        updateStatusTimer = ((updateStatusTimer - 1) > 0) ? (updateStatusTimer - 1) : 0;
        if (updateStatusTimer > 0) {
            return;
        }
        this.status.setText("");
    }

    /**
     * Updates the status display with the given status string
     *
     * @param statusString the status string to display
     */
    public void updateStatus(String statusString) {
        status.setForeground(Color.BLACK);
        status.setFont(MaterialFonts.REGULAR);
        status.setText(statusString);
    }

    /**
     * Updates the status display as an error with an error message
     *
     * @param error the error message to display
     */
    public void updateError(String error) {
        status.setForeground(Color.RED);
        status.setFont(MaterialFonts.ITALIC);
        status.setText(error);
    }

    /**
     * Gets the {@link TreeView} instance associated with this panel
     *
     * @return the {@link TreeView} instance
     */
    public TreeView getTreeView() {
        return treeView;
    }

    /**
     * Adds a new tree element by executing an {@link AddTreeElementCommand}. If the command cannot
     * be executed, it updates the status display with an error and error message.
     */
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

    /**
     * Deletes the selected tree element by executing a {@link DeleteTreeElementCommand}. If the
     * command cannot be executed, it updates the status display with an error and an error message.
     */
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

    /**
     * Merges selected tree elements by executing a {@link MergeCommand}. If the command cannot be
     * executed, it updates the status display with an error and an error message.
     */
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

    /**
     * Toggles the collapsed state of the selected tree elements. If an element is collapsed, it
     * will be expanded, and vice versa.
     */
    public void collapse() {
        TreeViewSelection selection = treeView.getSelection();
        for (TreeElementView view : selection.getSelectedViews()) {
            view.setCollapsed(!view.isCollapsed());
        }
    }
}
