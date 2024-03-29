package edu.rpi.legup.ui.lookandfeel.components;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class MaterialTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent component =
                (JComponent)
                        super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

        // hides yellow selection highlight
        component.setBorder(BorderFactory.createEmptyBorder());

        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        return component;
    }
}
