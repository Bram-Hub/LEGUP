package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.elements.PlaceableElement;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ElementFrame extends JPanel {

    private PlaceableElementPanel placeableElementPanel;
    private JTabbedPane tabbedPane;
    private JComboBox<GoalType> goalTypeComboBox;
    private JComboBox<PlaceableElement> goalDataTypecomboBox;
    private JCheckBox assumeSolutionCheckBox;
    private JScrollPane elements;
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
                        "<html>Choose a goal type, then click cells on the board to toggle goal-condition markers.</html>");
        helpText.setVerticalAlignment(SwingConstants.TOP);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.add(helpText, BorderLayout.NORTH);

        goalTypeComboBox = new JComboBox<>(GoalType.values());
        goalTypeComboBox.addActionListener(
                e -> controller.setGoalType((GoalType) goalTypeComboBox.getSelectedItem()));

        JPanel selectorPanel = new JPanel(new BorderLayout(6, 0));
        selectorPanel.add(new JLabel("Goal Type:"), BorderLayout.WEST);
        selectorPanel.add(goalTypeComboBox, BorderLayout.CENTER);
        topPanel.add(selectorPanel, BorderLayout.NORTH);

        goalDataTypecomboBox = new JComboBox<>();
        goalDataTypecomboBox.addActionListener(
                e -> {
                    Object selected = goalDataTypecomboBox.getSelectedItem();
                    if (selected instanceof PlaceableElement) {
                        controller.setGoalDataType((PlaceableElement) selected);
                    }
                });

        JPanel selectorPanel2 = new JPanel(new BorderLayout(6, 0));
        selectorPanel2.add(new JLabel("Goal Data:"), BorderLayout.WEST);
        selectorPanel2.add(goalDataTypecomboBox, BorderLayout.CENTER);
        topPanel.add(selectorPanel2, BorderLayout.SOUTH);

        assumeSolutionCheckBox = new JCheckBox("Assume solution");
        assumeSolutionCheckBox.addActionListener(
                e -> controller.setAssumeSolution(assumeSolutionCheckBox.isSelected()));
        topPanel.add(assumeSolutionCheckBox, BorderLayout.CENTER);

        goalPanel.add(topPanel, BorderLayout.NORTH);

        return goalPanel;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setElements(Puzzle puzzle) {
        if (puzzle != null) {
            placeableElementPanel.setElements(puzzle.getPlaceableElements());
            goalTypeComboBox.setSelectedItem(puzzle.getGoal().getType());

            // Populate goal data type combo box with placeable elements
            goalDataTypecomboBox.removeAllItems();
            for (PlaceableElement element : puzzle.getPlaceableElements()) {
                goalDataTypecomboBox.addItem(element);
            }
        }
    }

    public EditorElementController getController() {
        return controller;
    }

    public PlaceableElementPanel getPlaceableElementPanel() {
        return placeableElementPanel;
    }
}
