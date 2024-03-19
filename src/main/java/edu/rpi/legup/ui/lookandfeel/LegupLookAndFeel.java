package edu.rpi.legup.ui.lookandfeel;

import edu.rpi.legup.ui.lookandfeel.components.*;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialBorders;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialImages;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import edu.rpi.legup.ui.color.ColorPreferences.UIColor;

public class LegupLookAndFeel extends BasicLookAndFeel {
    /**
     * Return a short string that identifies this look and feel, e.g. "CDE/Motif". This string
     * should be appropriate for a menu item. Distinct look and feels should have different names,
     * e.g. a subclass of MotifLookAndFeel that changes the way a few components are rendered should
     * be called "CDE/Motif My Way"; something that would be useful to a user trying to select a
     * L&amp;F from a list of names.
     *
     * @return short identifier for the look and feel
     */
    @Override
    public String getName() {
        return "Legup";
    }

    /**
     * Return a string that identifies this look and feel. This string will be used by
     * applications/services that want to recognize well known look and feel implementations.
     * Presently the well known names are "Motif", "Windows", "Mac", "Metal". Note that a
     * LookAndFeel derived from a well known superclass that doesn't make any fundamental changes to
     * the look or feel shouldn't override this method.
     *
     * @return identifier for the look and feel
     */
    @Override
    public String getID() {
        return "Legup Look and Feel";
    }

    /**
     * Return a one line description of this look and feel implementation, e.g. "The CDE/Motif Look
     * and Feel". This string is intended for the user, e.g. in the title of a window or in a
     * ToolTip message.
     *
     * @return short description for the look and feel
     */
    @Override
    public String getDescription() {
        return "Material design look and feel for Legup";
    }

    /**
     * If the underlying platform has a "native" look and feel, and this is an implementation of it,
     * return {@code true}. For example, when the underlying platform is Solaris running CDE a
     * CDE/Motif look and feel implementation would return {@code true}.
     *
     * @return {@code true} if this look and feel represents the underlying platform look and feel
     */
    @Override
    public boolean isNativeLookAndFeel() {
        return true;
    }

    /**
     * Return {@code true} if the underlying platform supports and or permits this look and feel.
     * This method returns {@code false} if the look and feel depends on special resources or legal
     * agreements that aren't defined for the current platform.
     *
     * @return {@code true} if this is a supported look and feel
     * @see UIManager#setLookAndFeel
     */
    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    /**
     * Initializes the look and feel. While this method is public, it should only be invoked by the
     * {@code UIManager} when a look and feel is installed as the current look and feel. This method
     * is invoked before the {@code UIManager} invokes {@code getDefaults}. This method is intended
     * to perform any initialization for the look and feel. Subclasses should do any one-time setup
     * they need here, rather than in a static initializer, because look and feel class objects may
     * be loaded just to discover that {@code isSupportedLookAndFeel()} returns {@code false}.
     *
     * @see #uninitialize
     * @see UIManager#setLookAndFeel
     */
    @Override
    public void initialize() {
        super.initialize();
    }

    /**
     * Populates {@code table} with mappings from {@code uiClassID} to the fully qualified name of
     * the ui class. The value for a particular {@code uiClassID} is {@code
     * "javax.swing.plaf.basic.Basic + uiClassID"}. For example, the value for the {@code uiClassID}
     * {@code TreeUI} is {@code "javax.swing.plaf.basic.BasicTreeUI"}.
     *
     * @param table the {@code UIDefaults} instance the entries are added to
     * @throws NullPointerException if {@code table} is {@code null}
     * @see LookAndFeel
     * @see #getDefaults
     */
    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);

        table.put("ButtonUI", MaterialButtonUI.class.getCanonicalName());
        table.put("TextFieldUI", MaterialTextFieldUI.class.getCanonicalName());
        table.put("PasswordFieldUI", MaterialPasswordFieldUI.class.getCanonicalName());
        table.put("TableUI", MaterialTableUI.class.getCanonicalName());
        table.put("TableHeaderUI", MaterialTableHeaderUI.class.getCanonicalName());
        table.put("TreeUI", MaterialTreeUI.class.getCanonicalName());
        table.put("SpinnerUI", MaterialSpinnerUI.class.getCanonicalName());
        table.put("PanelUI", MaterialPanelUI.class.getCanonicalName());
        table.put("LabelUI", MaterialLabelUI.class.getCanonicalName());
        table.put("MenuItemUI", MaterialMenuItemUI.class.getCanonicalName());
        //        table.put ("MenuBarUI", .class.getCanonicalName());
        table.put("MenuUI", MaterialMenuUI.class.getCanonicalName());
        table.put("CheckBoxUI", MaterialCheckBoxUI.class.getCanonicalName());
        table.put("RadioButtonUI", MaterialRadioButtonUI.class.getCanonicalName());
        table.put("TabbedPaneUI", MaterialTabbedPaneUI.class.getCanonicalName());
        table.put("ToggleButtonUI", MaterialToggleButtonUI.class.getCanonicalName());
        table.put("ScrollBarUI", MaterialScrollBarUI.class.getCanonicalName());
        table.put("ComboBoxUI", MaterialComboBoxUI.class.getCanonicalName());
        table.put("PopupMenuUI", MaterialPopupMenuUI.class.getCanonicalName());
        table.put("ToolBarUI", MaterialToolBarUI.class.getCanonicalName());
        table.put("SliderUI", MaterialSliderUI.class.getCanonicalName());
        table.put("ProgressBarUI", MaterialProgressBarUI.class.getCanonicalName());
        table.put("RadioButtonMenuItemUI", MaterialRadioButtonMenuItemUI.class.getCanonicalName());
        table.put("CheckBoxMenuItemUI", MaterialCheckBoxMenuItemUI.class.getCanonicalName());
        table.put("TextPaneUI", MaterialTextPaneUI.class.getCanonicalName());
        table.put("EditorPaneUI", MaterialEditorPaneUI.class.getCanonicalName());
        table.put("SeparatorUI", MaterialSeparatorUI.class.getCanonicalName());
        table.put("FileChooserUI", MaterialFileChooserUI.class.getCanonicalName());
        table.put("ToolTipUI", MaterialToolTipUI.class.getCanonicalName());
        table.put("SplitPaneUI", MaterialSplitPaneUI.class.getCanonicalName());
        //        table.put ("ColorChooserUI", );
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);

//        table.put("Button.highlight", MaterialColors.GRAY_300);
        table.put("Button.highlight", UIColor.BUTTON_HIGHLIGHT_DEFAULT.get());
        table.put("Button.opaque", false);
        table.put("Button.border", BorderFactory.createEmptyBorder(7, 17, 7, 17));
//        table.put("Button.background", MaterialColors.GRAY_200);
        table.put("Button.background", UIColor.BUTTON_BACKGROUND_DEFAULT.get());
//        table.put("Button.foreground", Color.BLACK);
        table.put("Button.foreground", UIColor.BUTTON_FOREGROUND_DEFAULT.get());
        table.put("Button.font", MaterialFonts.MEDIUM);

        table.put("CheckBox.font", MaterialFonts.REGULAR);
//        table.put("CheckBox.background", Color.WHITE);
        table.put("CheckBox.background", UIColor.CHECKBOX_BACKGROUND_DEFAULT.get());
//        table.put("CheckBox.foreground", Color.BLACK);
        table.put("CheckBox.foreground", UIColor.CHECKBOX_FOREGROUND_DEFAULT.get());
        table.put("CheckBox.icon", new ImageIcon(MaterialImages.UNCHECKED_BOX));
        table.put("CheckBox.selectedIcon", new ImageIcon(MaterialImages.PAINTED_CHECKED_BOX));

        table.put("ComboBox.font", MaterialFonts.REGULAR);
//        table.put("ComboBox.background", Color.WHITE);
        table.put("ComboBox.background", UIColor.COMBOBOX_BACKGROUND_DEFAULT.get());
//        table.put("ComboBox.foreground", Color.BLACK);
        table.put("ComboBox.foreground", UIColor.COMBOBOX_FOREGROUND_DEFAULT.get());
        table.put(
                "ComboBox.border",
                BorderFactory.createCompoundBorder(
                        MaterialBorders.LIGHT_LINE_BORDER,
                        BorderFactory.createEmptyBorder(0, 5, 0, 0)));
//        table.put("ComboBox.buttonBackground", MaterialColors.GRAY_300);
        table.put("ComboBox.buttonBackground", UIColor.COMBOBOX_BUTTON_BACKGROUND_DEFAULT.get());
//        table.put("ComboBox.selectionBackground", Color.WHITE);
        table.put("ComboBox.selectionBackground", UIColor.COMBOBOX_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("ComboBox.selectionForeground", Color.BLACK);
        table.put("ComboBox.selectionForeground", UIColor.COMBOBOX_SELECTION_FOREGROUND_DEFAULT.get());
//        table.put("ComboBox.selectedInDropDownBackground", MaterialColors.GRAY_200);
        table.put("ComboBox.selectedInDropDownBackground", UIColor.COMBOBOX_SELECTED_IN_DROP_DOWN_BACKGROUND_DEFAULT.get());

        table.put("Label.font", MaterialFonts.REGULAR);
//        table.put("Label.background", Color.WHITE);
        table.put("Label.background", UIColor.LABEL_BACKGROUND_DEFAULT.get());
//        table.put("Label.foreground", Color.BLACK);
        table.put("Label.foreground", UIColor.LABEL_FOREGROUND_DEFAULT.get());
        table.put("Label.border", BorderFactory.createEmptyBorder());

        table.put("Menu.font", MaterialFonts.BOLD);
        table.put("Menu.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        table.put("Menu.background", Color.WHITE);
        table.put("Menu.background", UIColor.MENU_BACKGROUND_DEFAULT.get());
//        table.put("Menu.foreground", Color.BLACK);
        table.put("Menu.foreground", UIColor.MENU_FOREGROUND_DEFAULT.get());
        table.put("Menu.opaque", true);
//        table.put("Menu.selectionBackground", MaterialColors.GRAY_200);
        table.put("Menu.selectionBackground", UIColor.MENU_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("Menu.selectionForeground", Color.BLACK);
        table.put("Menu.selectionForeground", UIColor.MENU_SELECTION_FOREGROUND_DEFAULT.get());
//        table.put("Menu.disabledForeground", new Color(0, 0, 0, 100));
        table.put("Menu.disabledForeground", UIColor.MENU_DISABLED_FOREGROUND_DEFAULT.get());
        table.put("Menu.menuPopupOffsetY", 3);

        table.put("MenuBar.font", MaterialFonts.BOLD);
//        table.put("MenuBar.background", Color.WHITE);
        table.put("MenuBar.background", UIColor.MENU_BAR_BACKGROUND_DEFAULT.get());
        table.put("MenuBar.border", MaterialBorders.LIGHT_SHADOW_BORDER);
//        table.put("MenuBar.foreground", Color.BLACK);
        table.put("MenuBar.foreground", UIColor.MENU_BAR_FOREGROUND_DEFAULT.get());

//        table.put("MenuItem.disabledForeground", new Color(0, 0, 0, 100));
        table.put("MenuItem.disabledForeground", UIColor.MENU_ITEM_DISABLED_FOREGROUND_DEFAULT.get());
//        table.put("MenuItem.selectionBackground", MaterialColors.GRAY_200);
        table.put("MenuItem.selectionBackground", UIColor.MENU_ITEM_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("MenuItem.selectionForeground", Color.BLACK);
        table.put("MenuItem.selectionForeground", UIColor.MENU_ITEM_SELECTION_FOREGROUND_DEFAULT.get());
        table.put("MenuItem.font", MaterialFonts.MEDIUM);
//        table.put("MenuItem.background", Color.WHITE);
        table.put("MenuItem.background", UIColor.MENU_ITEM_BACKGROUND_DEFAULT.get());
//        table.put("MenuItem.foreground", Color.BLACK);
        table.put("MenuItem.foreground", UIColor.MENU_ITEM_FOREGROUND_DEFAULT.get());
        table.put("MenuItem.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));

//        table.put("OptionPane.background", Color.WHITE);
        table.put("OptionPane.background", UIColor.OPTION_PANE_BACKGROUND_DEFAULT.get());
        table.put("OptionPane.border", MaterialBorders.DEFAULT_SHADOW_BORDER);
        table.put("OptionPane.font", MaterialFonts.REGULAR);

        table.put("Panel.font", MaterialFonts.REGULAR);
//        table.put("Panel.background", Color.WHITE);
        table.put("Panel.background", UIColor.PANEL_BACKGROUND_COLOR_DEFAULT.get());
        table.put("Panel.border", BorderFactory.createEmptyBorder());

        table.put("PopupMenu.border", MaterialBorders.LIGHT_LINE_BORDER);
//        table.put("PopupMenu.background", Color.WHITE);
        table.put("PopupMenu.background", UIColor.POPUP_MENU_BACKGROUND_DEFAULT.get());
//        table.put("PopupMenu.foreground", Color.BLACK);
        table.put("PopupMenu.foreground", UIColor.POPUP_MENU_FOREGROUND_DEFAULT.get());

        table.put("RadioButton.font", MaterialFonts.REGULAR);
//        table.put("RadioButton.background", Color.WHITE);
        table.put("RadioButton.background", UIColor.RADIO_BUTTON_BACKGROUND_DEFAULT.get());
//        table.put("RadioButton.foreground", Color.BLACK);
        table.put("RadioButton.foreground", UIColor.RADIO_BUTTON_FOREGROUND_DEFAULT.get());
        table.put("RadioButton.icon", new ImageIcon(MaterialImages.RADIO_BUTTON_OFF));
        table.put("RadioButton.selectedIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_ON));

        table.put("Spinner.font", MaterialFonts.REGULAR);
//        table.put("Spinner.background", Color.WHITE);
        table.put("Spinner.background", UIColor.SPINNER_BACKGROUND_DEFAULT.get());
//        table.put("Spinner.foreground", Color.BLACK);
        table.put("Spinner.foreground", UIColor.SPINNER_FOREGROUND_DEFAULT.get());
        table.put("Spinner.border", MaterialBorders.LIGHT_LINE_BORDER);
//        table.put("Spinner.arrowButtonBackground", MaterialColors.GRAY_200);
        table.put("Spinner.arrowButtonBackground", UIColor.SPINNER_ARROW_BUTTON_BACKGROUND_DEFAULT.get());
        table.put("Spinner.arrowButtonBorder", BorderFactory.createEmptyBorder());

        table.put("ScrollBar.font", MaterialFonts.REGULAR);
//        table.put("ScrollBar.track", MaterialColors.GRAY_200);
        table.put("ScrollBar.track", UIColor.SCROLL_BAR_TRACK_DEFAULT.get());
//        table.put("ScrollBar.thumb", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumb", UIColor.SCROLL_BAR_THUMB_DEFAULT.get());
//        table.put("ScrollBar.thumbDarkShadow", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumbDarkShadow", UIColor.SCROLL_BAR_THUMB_DARK_SHADOW_DEFAULT.get());
//        table.put("ScrollBar.thumbHighlight", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumbHighlight", UIColor.SCROLL_BAR_THUMB_HIGHLIGHT_DEFAULT.get());
//        table.put("ScrollBar.thumbShadow", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumbShadow", UIColor.SCROLL_BAR_THUMB_SHADOW_DEFAULT.get());
//        table.put("ScrollBar.arrowButtonBackground", MaterialColors.GRAY_300);
        table.put("ScrollBar.arrowButtonBackground", UIColor.SCROLL_BAR_ARROW_BUTTON_BACKGROUND_DEFAULT.get());
        table.put("ScrollBar.arrowButtonBorder", BorderFactory.createEmptyBorder());

//        table.put("ScrollPane.background", Color.WHITE);
        table.put("ScrollPane.background", UIColor.SCROLL_PANE_BACKGROUND_DEFAULT.get());
        table.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        table.put("ScrollPane.font", MaterialFonts.REGULAR);

        table.put("Slider.font", MaterialFonts.REGULAR);
//        table.put("Slider.background", Color.WHITE);
        table.put("Slider.background", UIColor.SLIDER_BACKGROUND_DEFAULT.get());
//        table.put("Slider.foreground", MaterialColors.GRAY_700);
        table.put("Slider.foreground", UIColor.SLIDER_FOREGROUND_DEFAULT.get());
//        table.put("Slider.trackColor", Color.BLACK);
        table.put("Slider.trackColor", UIColor.SLIDER_TRACK_COLOR_DEFAULT.get());
        //        table.put ("Slider.border",
        // BorderFactory.createCompoundBorder(MaterialBorders.LIGHT_LINE_BORDER,
        // BorderFactory.createEmptyBorder (5, 5, 5, 5)));

        table.put("SplitPane.border", MaterialBorders.LIGHT_LINE_BORDER);
//        table.put("SplitPane.background", Color.WHITE);
        table.put("SplitPane.background", UIColor.SPLIT_PANE_BACKGROUND_DEFAULT.get());
        table.put("SplitPane.dividerSize", 10);
        table.put("SplitPaneDivider.border", MaterialBorders.LIGHT_SHADOW_BORDER);

        table.put("TabbedPane.font", MaterialFonts.REGULAR);
//        table.put("TabbedPane.background", Color.WHITE);
        table.put("TabbedPane.background", UIColor.TABBED_PANE_BACKGROUND_DEFAULT.get());
//        table.put("TabbedPane.foreground", Color.BLACK);
        table.put("TabbedPane.foreground", UIColor.TABBED_PANE_FOREGROUND_DEFAULT.get());
        table.put("TabbedPane.border", BorderFactory.createEmptyBorder());
        table.put("TabbedPane.shadow", null);
        table.put("TabbedPane.darkShadow", null);
//        table.put("TabbedPane.highlight", MaterialColors.GRAY_200);
        table.put("TabbedPane.highlight", UIColor.TABBED_PANE_HIGHLIGHT_DEFAULT.get());
//        table.put("TabbedPane.borderHighlightColor", MaterialColors.GRAY_300);
        table.put("TabbedPane.borderHighlightColor", UIColor.TABBED_PANE_BORDER_HIGHLIGHT_DEFAULT.get());

//        table.put("Table.selectionBackground", MaterialColors.GRAY_100);
        table.put("Table.selectionBackground", UIColor.TABLE_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("Table.selectionForeground", Color.BLACK);
        table.put("Table.selectionForeground", UIColor.TABLE_SELECTION_FOREGROUND_DEFAULT.get());
//        table.put("Table.background", Color.WHITE);
        table.put("Table.background", UIColor.TABLE_BACKGROUND_DEFAULT.get());
        table.put("Table.font", MaterialFonts.REGULAR);
        table.put("Table.border", MaterialBorders.LIGHT_LINE_BORDER);
//        table.put("Table.gridColor", MaterialColors.GRAY_200);
        table.put("Table.gridColor", UIColor.TABLE_GRID_COLOR_DEFAULT.get());
//        table.put("TableHeader.background", MaterialColors.GRAY_200);
        table.put("TableHeader.background", UIColor.TABLE_HEADER_BACKGROUND_DEFAULT.get());
        table.put("TableHeader.font", MaterialFonts.BOLD);
        table.put(
                "TableHeader.cellBorder",
                BorderFactory.createCompoundBorder(
                        MaterialBorders.LIGHT_LINE_BORDER,
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

//        table.put("TextArea.background", MaterialColors.GRAY_200);
        table.put("TextArea.background", UIColor.TEXT_AREA_BACKGROUND_DEFAULT.get());
        table.put("TextArea.border", BorderFactory.createEmptyBorder());
//        table.put("TextArea.foreground", Color.BLACK);
        table.put("TextArea.foreground", UIColor.TEXT_AREA_FOREGROUND_DEFAULT.get());

        table.put("ToggleButton.border", BorderFactory.createEmptyBorder());
        table.put("ToggleButton.font", MaterialFonts.REGULAR);
//        table.put("ToggleButton.background", Color.WHITE);
        table.put("ToggleButton.background", UIColor.TOGGLE_BUTTON_BACKGROUND_DEFAULT.get());
//        table.put("ToggleButton.foreground", Color.BLACK);
        table.put("ToggleButton.foreground", UIColor.TOGGLE_BUTTON_FOREGROUND_DEFAULT.get());
        table.put("ToggleButton.icon", new ImageIcon(MaterialImages.TOGGLE_BUTTON_OFF));
        table.put("ToggleButton.selectedIcon", new ImageIcon(MaterialImages.TOGGLE_BUTTON_ON));

        table.put("ToolBar.font", MaterialFonts.REGULAR);
//        table.put("ToolBar.background", Color.WHITE);
        table.put("ToolBar.background", UIColor.TOOL_BAR_BACKGROUND_DEFAULT.get());
//        table.put("ToolBar.foreground", Color.BLACK);
        table.put("ToolBar.foreground", UIColor.TOOL_BAR_FOREGROUND_DEFAULT.get());
        table.put("ToolBar.border", MaterialBorders.LIGHT_SHADOW_BORDER);
//        table.put("ToolBar.dockingBackground", MaterialColors.LIGHT_GREEN_A100);
        table.put("ToolBar.dockingBackground", UIColor.TOOL_BAR_DOCKING_BACKGROUND_DEFAULT.get());
//        table.put("ToolBar.floatingBackground", MaterialColors.GRAY_200);
        table.put("ToolBar.floatingBackground", UIColor.TOOL_BAR_FLOATING_BACKGROUND_DEFAULT.get());

        table.put("Tree.font", MaterialFonts.REGULAR);
//        table.put("Tree.selectionForeground", Color.BLACK);
        table.put("Tree.selectionForeground", UIColor.TREE_SELECTION_FOREGROUND_DEFAULT.get());
//        table.put("Tree.foreground", Color.BLACK);
        table.put("Tree.foreground", UIColor.TREE_FOREGROUND_DEFAULT.get());
//        table.put("Tree.selectionBackground", MaterialColors.GRAY_200);
        table.put("Tree.selectionBackground", UIColor.TREE_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("Tree.background", Color.WHITE);
        table.put("Tree.background", UIColor.TREE_BACKGROUND_DEFAULT.get());
        table.put("Tree.closedIcon", new ImageIcon(MaterialImages.RIGHT_ARROW));
        table.put("Tree.openIcon", new ImageIcon(MaterialImages.DOWN_ARROW));
        table.put("Tree.selectionBorderColor", null);

//        table.put("RadioButtonMenuItem.foreground", Color.BLACK);
        table.put("RadioButtonMenuItem.foreground", UIColor.RADIO_BUTTON_FOREGROUND_DEFAULT.get());
//        table.put("RadioButtonMenuItem.selectionForeground", Color.BLACK);
        table.put("RadioButtonMenuItem.selectionForeground", UIColor.RADIO_BUTTON_MENU_ITEM_SELECTION_FOREGROUND_DEFAULT.get());
        // If it changes the background of the menuitem it must change this too, irrespective of its
        // setting
        table.put("RadioButtonMenuItem.background", UIManager.getColor("MenuItem.background"));
//        table.put("RadioButtonMenuItem.selectionBackground", MaterialColors.GRAY_200);
        table.put("RadioButtonMenuItem.selectionBackground", UIColor.RADIO_BUTTON_MENU_ITEM_SELECTION_BACKGROUND_DEFAULT.get());
        table.put("RadioButtonMenuItem.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        table.put("RadioButtonMenuItem.checkIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_OFF));
        table.put(
                "RadioButtonMenuItem.selectedCheckIcon",
                new ImageIcon(MaterialImages.RADIO_BUTTON_ON));

        // If it changes the background of the menuitem it must change this too, irrespective of its
        // setting
        table.put("CheckBoxMenuItem.background", UIManager.getColor("MenuItem.background"));
//        table.put("CheckBoxMenuItem.selectionBackground", MaterialColors.GRAY_200);
        table.put("CheckBoxMenuItem.selectionBackground", UIColor.CHECKBOX_MENU_ITEM_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("CheckBoxMenuItem.foreground", Color.BLACK);
        table.put("CheckBoxMenuItem.foreground", UIColor.CHECKBOX_MENU_ITEM_FOREGROUND_DEFAULT.get());
//        table.put("CheckBoxMenuItem.selectionForeground", Color.BLACK);
        table.put("CheckBoxMenuItem.selectionForeground", UIColor.CHECKBOX_MENU_ITEM_SELECTION_FOREGROUND_DEFAULT.get());
        table.put("CheckBoxMenuItem.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        table.put("CheckBoxMenuItem.checkIcon", new ImageIcon(MaterialImages.UNCHECKED_BOX));
        table.put(
                "CheckBoxMenuItem.selectedCheckIcon",
                new ImageIcon(MaterialImages.PAINTED_CHECKED_BOX));

        table.put("TextPane.border", MaterialBorders.LIGHT_LINE_BORDER);
//        table.put("TextPane.background", MaterialColors.GRAY_50);
        table.put("TextPane.background", UIColor.TEXT_PANE_BACKGROUND_DEFAULT.get());
//        table.put("TextPane.selectionBackground", MaterialColors.LIGHT_BLUE_200);
        table.put("TextPane.selectionBackground", UIColor.TEXT_PANE_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("TextPane.inactiveForeground", MaterialColors.GRAY_500);
        table.put("TextPane.inactiveForeground", UIColor.TEXT_PANE_INACTIVE_FOREGROUND_DEFAULT.get());
        table.put("TextPane.font", MaterialFonts.REGULAR);

        table.put("EditorPane.border", MaterialBorders.LIGHT_LINE_BORDER);
//        table.put("EditorPane.background", MaterialColors.GRAY_50);
        table.put("EditorPane.background", UIColor.EDITOR_PANE_BACKGROUND_DEFAULT.get());
//        table.put("EditorPane.selectionBackground", MaterialColors.LIGHT_BLUE_200);
        table.put("EditorPane.selectionBackground", UIColor.EDITOR_PANE_SELECTION_BACKGROUND_DEFAULT.get());
//        table.put("EditorPane.inactiveForeground", MaterialColors.GRAY_500);
        table.put("EditorPane.inactiveForeground", UIColor.EDITOR_PANE_INACTIVE_FOREGROUND_DEFAULT.get());
        table.put("EditorPane.font", MaterialFonts.REGULAR);

//        table.put("Separator.background", MaterialColors.GRAY_300);
        table.put("Separator.background", UIColor.SEPARATOR_BACKGROUND_DEFAULT.get());
//        table.put("Separator.foreground", MaterialColors.GRAY_300);
        table.put("Separator.foreground", UIColor.SEPARATOR_FOREGROUND_DEFAULT.get());

//        table.put("ToolTip.background", MaterialColors.GRAY_500);
        table.put("ToolTip.background", UIColor.TOOL_TIP_BACKGROUND_DEFAULT.get());
//        table.put("ToolTip.foreground", MaterialColors.GRAY_50);
        table.put("ToolTip.foreground", UIColor.TOOL_TIP_FOREGROUND_DEFAULT.get());
        table.put("ToolTip.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));

//        table.put("ColorChooser.background", MaterialColors.WHITE);
        table.put("ColorChooser.background", UIColor.COLOR_CHOOSER_BACKGROUND_DEFAULT.get());
//        table.put("ColorChooser.foreground", MaterialColors.BLACK);
        table.put("ColorChooser.foreground", UIColor.COLOR_CHOOSER_FOREGROUND_DEFAULT.get());
    }
}
