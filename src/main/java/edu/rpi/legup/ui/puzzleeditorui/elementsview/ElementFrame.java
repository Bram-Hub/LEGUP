package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.ui.lookandfeel.components.MaterialTabbedPaneUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ElementFrame extends JPanel {
    private static final String checkBox = "<font style=\"color:#00CD00\"> \u2714 </font>";
    private static final String xBox = "<font style=\"color:#FF0000\"> \u2718 </font>";
    private static final String htmlHead = "<html>";
    private static final String htmlTail = "</html>";

    private PlaceableElementPanel placeableElementPanel;
    private NonPlaceableElementPanel nonPlaceableElementPanel;
    private JTabbedPane tabbedPane;
    private ButtonGroup buttonGroup;

    private EditorElementController controller;

    public ElementFrame(EditorElementController controller) {
        this.controller = controller;
        MaterialTabbedPaneUI tabOverride = new MaterialTabbedPaneUI() {
            //this prevents the tabs from moving around when you select them
            @Override
            protected boolean shouldRotateTabRuns(int i) {
                return false;
            }
        };

        this.tabbedPane = new JTabbedPane();
        tabbedPane.setUI(tabOverride);
        JLabel status = new JLabel("", SwingConstants.CENTER);
        this.buttonGroup = new ButtonGroup();

        nonPlaceableElementPanel = new NonPlaceableElementPanel(this);
        //nonPlaceableElementPanel.setMinimumSize(new Dimension(100,200));
        tabbedPane.addTab(nonPlaceableElementPanel.getName(), nonPlaceableElementPanel.getIcon(), new JScrollPane(nonPlaceableElementPanel), nonPlaceableElementPanel.getToolTip());

        placeableElementPanel = new PlaceableElementPanel(this);
        //placeableElementPanel.setMinimuSize(new Dimension(100,200));
        tabbedPane.addTab(placeableElementPanel.getName(), placeableElementPanel.getIcon(), new JScrollPane(placeableElementPanel), placeableElementPanel.getToolTip());
        tabbedPane.setTabPlacement(JTabbedPane.TOP);


        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 256));
        setPreferredSize(new Dimension(330, 256));

        add(tabbedPane);
        add(status, BorderLayout.SOUTH);

        TitledBorder title = BorderFactory.createTitledBorder("Elements");
        title.setTitleJustification(TitledBorder.CENTER);
        setBorder(title);
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void resetSize() {
        int buttonWidth = ((ElementPanel) tabbedPane.getSelectedComponent()).getElementButtons()[0].getWidth();
        this.setMinimumSize(new Dimension(2 * buttonWidth + 64, this.getHeight()));
    }

    public void setElements(Puzzle puzzle) {
        nonPlaceableElementPanel.setElements(puzzle.getNonPlaceableElements());
        placeableElementPanel.setElements(puzzle.getPlaceableElements());

    }

    public EditorElementController getController() {
        return controller;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public NonPlaceableElementPanel getNonPlaceableElementPanel() {
        return nonPlaceableElementPanel;
    }

    public PlaceableElementPanel getPlaceableElementPanel() {
        return placeableElementPanel;
    }
}
