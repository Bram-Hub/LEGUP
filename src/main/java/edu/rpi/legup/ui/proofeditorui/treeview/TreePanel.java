package edu.rpi.legup.ui.proofeditorui.treeview;

import static edu.rpi.legup.ui.zoompane.ZoomViewport.ZoomPeer.NOTIFICATION;

import com.formdev.flatlaf.FlatClientProperties;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.history.AddTreeElementCommand;
import edu.rpi.legup.history.DeleteTreeElementCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.MergeCommand;
import edu.rpi.legup.ui.zoompane.ZoomPane;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * {@code TreePanel} is a JPanel that manages and displays a tree view with associated toolbar and
 * status information. It provides methods to interact with the tree view, such as adding, deleting,
 * and merging tree elements, and updating the status based on actions performed.
 */
public class TreePanel extends JPanel {

    private final ZoomPane treeViewPane;
    private final TreeView treeView;
    private final TreeToolbarPanel toolbar;

    private final JLabel infoLabel;
    private final JLabel errorLabel;

    /** Constructs a {@code TreePanel} and initializes the UI components. */
    public TreePanel() {

        setLayout(new BorderLayout());
        TitledBorder title = BorderFactory.createTitledBorder("Proof Tree");
        title.setTitleJustification(TitledBorder.CENTER);
        setBorder(title);

        treeView = new TreeView(new TreeController());

        toolbar = new TreeToolbarPanel(this);
        treeViewPane = new ZoomPane(treeView);

        add(treeViewPane, BorderLayout.CENTER);
        add(toolbar, BorderLayout.WEST);

        errorLabel = new JLabel();
        errorLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        errorLabel.setVisible(false);
        treeViewPane.getZoomViewport().getPeer().add(errorLabel, NOTIFICATION);

        infoLabel = new JLabel();
        infoLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "info");
        infoLabel.setVisible(false);
        treeViewPane.getZoomViewport().getPeer().add(infoLabel, NOTIFICATION);

        updateUI();
    }

    @Override
    public void updateUI() {
        super.updateUI();

        if (treeViewPane != null) {
            treeViewPane.putClientProperty("ZoomPane.viewPadding", null);
            Insets treeViewPadding = UIManager.getInsets("Tree.viewPadding");
            if (treeViewPadding != null) {
                treeViewPane.putClientProperty("ZoomPane.viewPadding", treeViewPadding);
            }
        }
    }

    /**
     * Updates the info display with a status message.
     *
     * @param status the status message to display
     */
    public void updateStatus(String status) {
        if (status.isEmpty()) { infoLabel.setVisible(false); }
        else {
            infoLabel.setText(status);
            infoLabel.setVisible(true);
        }
    }

    /**
     * Updates the error display with an error message.
     *
     * @param error the error message to display
     */
    public void updateError(@NotNull String error) {
        if (error.isEmpty()) { errorLabel.setVisible(false); }
        else {
            errorLabel.setText(error);
            errorLabel.setVisible(true);
        }
    }

    /**
     * Gets the {@code TreeView} instance associated with this panel.
     *
     * @return the {@code TreeView} instance
     */
    public TreeView getTreeView() { return treeView; }

    /**
     * Adds a new tree element by executing an {@link AddTreeElementCommand}. If the command cannot
     * be executed, it updates the error message.
     */
    public void add() {
        TreeViewSelection selection = treeView.getSelection();

        String error = "";
        AddTreeElementCommand add = new AddTreeElementCommand(selection);

        if (add.canExecute()) {
            add.execute();
            GameBoardFacade.getInstance().getHistory().pushChange(add);
        }
        else { error = add.getError(); }

        updateError(error);
    }

    /**
     * Deletes the selected tree element by executing a {@link DeleteTreeElementCommand}. If the
     * command cannot be executed, it updates the error message.
     */
    public void delete() {
        TreeViewSelection selection = treeView.getSelection();

        String error = "";
        DeleteTreeElementCommand del = new DeleteTreeElementCommand(selection);

        if (del.canExecute()) {
            del.execute();
            GameBoardFacade.getInstance().getHistory().pushChange(del);
        }
        else { error = del.getError(); }

        updateError(error);
    }

    /**
     * Merges selected tree elements by executing a {@link MergeCommand}. If the command cannot be
     * executed, it updates the error message.
     */
    public void merge() {
        TreeViewSelection selection = treeView.getSelection();

        String error = "";
        ICommand merge = new MergeCommand(selection);

        if (merge.canExecute()) {
            merge.execute();
            GameBoardFacade.getInstance().getHistory().pushChange(merge);
        }
        else { error = merge.getError(); }

        updateError(error);
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
