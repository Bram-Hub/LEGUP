package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.ui.color.ColorPreferences;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import edu.rpi.legup.ui.color.ColorPreferences.*;

public class DataSelectionView extends JPopupMenu {

    public DataSelectionView(ElementController controller) {
        setBackground(UIColor.DATA_SELECTION_BACKGROUND.get());
        setBorder(new BevelBorder(BevelBorder.RAISED));
    }
}
