package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.controller.ElementController;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * DataSelectionView is a popup menu used for selecting data elements.
 * It extends JPopupMenu and is styled with a gray background and a raised bevel border.
 */
public class DataSelectionView extends JPopupMenu {

    /**
     * Constructs a DataSelectionView with the given controller.
     *
     * @param controller The ElementController to handle UI events.
     */
    public DataSelectionView(ElementController controller) {
        setBackground(Color.GRAY);
        setBorder(new BevelBorder(BevelBorder.RAISED));
    }
}
