package edu.rpi.legup.ui.lookandfeel.components;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MaterialTableCellEditor extends DefaultCellEditor {

  public MaterialTableCellEditor() {
    super(init());
  }

  private static JTextField init() {
    JTextField textField = new JTextField();
    textField.setUI(new MaterialTextFieldUI(false));

    return textField;
  }

  @Override
  public Component getTableCellEditorComponent(
      JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
    JTextField textField =
        (JTextField)
            super.getTableCellEditorComponent(table, value, isSelected, rowIndex, vColIndex);
    textField.setText(value.toString());

    return textField;
  }
}
