package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.ui.lookandfeel.components.MaterialTabbedPaneUI;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ElementFrame extends JPanel {
    private static final String checkBox = "<font style=\"color:#00CD00\"> \u2714 </font>";
    private static final String xBox = "<font style=\"color:#FF0000\"> \u2718 </font>";
    private static final String htmlHead = "<html>";
    private static final String htmlTail = "</html>";

    private PlaceableElementPanel placeableElementPanel;
    //private JTabbedPane tabbedPane;
    private ButtonGroup buttonGroup;

    private EditorElementController controller;

    public ElementFrame(EditorElementController controller) {

        this.controller = controller;

        JLabel status = new JLabel("", SwingConstants.CENTER);
        this.buttonGroup = new ButtonGroup();

        // Parent panel to hold all elements
        JPanel elementPanel = new JPanel();
        elementPanel.setLayout(new BoxLayout(elementPanel, BoxLayout.Y_AXIS));

        placeableElementPanel = new PlaceableElementPanel(this);
        placeableElementPanel.setMinimumSize(new Dimension(100, 200));
        elementPanel.add(new JScrollPane(placeableElementPanel));

        // Set layout and dimensions for the main panel
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 256));
        setPreferredSize(new Dimension(330, 256));

        // Add components to the main panel
        add(elementPanel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        // Center-align the titled border
        TitledBorder title = BorderFactory.createTitledBorder("Elements");
        title.setTitleJustification(TitledBorder.CENTER);
        setBorder(title);
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

//    public void resetSize() {
//        int buttonWidth =
//                ((ElementPanel) tabbedPane.getSelectedComponent())
//                        .getElementButtons()[0].getWidth();
//        this.setMinimumSize(new Dimension(2 * buttonWidth + 64, this.getHeight()));
//    }

    public void setElements(Puzzle puzzle) {
        if (puzzle != null) {
            placeableElementPanel.setElements(puzzle.getPlaceableElements());
        }
    }

    public EditorElementController getController() {
        return controller;
    }

//    public JTabbedPane getTabbedPane() {
//        return tabbedPane;
//    }

    public PlaceableElementPanel getPlaceableElementPanel() {
        return placeableElementPanel;
    }
}
