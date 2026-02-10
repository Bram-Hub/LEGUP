package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.model.Puzzle;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ElementFrame extends JPanel {

    private PlaceableElementPanel placeableElementPanel;
    private JTabbedPane tabbedPane;
    private ButtonGroup buttonGroup;

    private EditorElementController controller;

    public ElementFrame(EditorElementController controller) {

        this.controller = controller;

        JLabel status = new JLabel("", SwingConstants.CENTER);
        this.buttonGroup = new ButtonGroup();

        placeableElementPanel = new PlaceableElementPanel(this);
        placeableElementPanel.setMinimumSize(new Dimension(100, 200));

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Placeable", new JScrollPane(placeableElementPanel));
        tabbedPane.addTab("Goal Conditions", createGoalConditionsPanel());
        tabbedPane.addChangeListener(
                e -> {
                    int selectedIndex = tabbedPane.getSelectedIndex();
                    if (selectedIndex == 1) {
                        controller.setSelectionMode(
                                EditorElementController.SelectionMode.GOAL_CONDITIONS);
                    } else {
                        controller.setSelectionMode(EditorElementController.SelectionMode.PLACEABLE);
                    }
                });

        // Set layout and dimensions for the main panel
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 256));
        setPreferredSize(new Dimension(330, 256));

        // Add components to the main panel
        add(tabbedPane, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        // Center-align the titled border
        TitledBorder title = BorderFactory.createTitledBorder("Elements");
        title.setTitleJustification(TitledBorder.CENTER);
        setBorder(title);
    }

    private JComponent createGoalConditionsPanel() {
        JPanel goalPanel = new JPanel(new BorderLayout());
        goalPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel helpText =
                new JLabel(
                        "<html>Select this tab, then click cells on the board to toggle goal condition markers.</html>");
        helpText.setVerticalAlignment(SwingConstants.TOP);
        goalPanel.add(helpText, BorderLayout.NORTH);

        return goalPanel;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setElements(Puzzle puzzle) {
        if (puzzle != null) {
            placeableElementPanel.setElements(puzzle.getPlaceableElements());
        }
    }

    public EditorElementController getController() {
        return controller;
    }

    public PlaceableElementPanel getPlaceableElementPanel() {
        return placeableElementPanel;
    }
}
