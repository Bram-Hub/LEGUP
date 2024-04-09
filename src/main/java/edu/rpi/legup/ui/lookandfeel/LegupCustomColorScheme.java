package edu.rpi.legup.ui.lookandfeel;

import com.formdev.flatlaf.FlatLaf;
import edu.rpi.legup.ui.color.ColorPreferences;
import edu.rpi.legup.ui.color.ColorPreferences.UIColor;

import java.util.HashMap;
import java.util.Map;

public class LegupCustomColorScheme {

    
    private static void addDefaultColor(Map<String, String> defaults, String key, UIColor color) {
        color.getAsHex().ifPresent(hex -> defaults.put(key, hex));
    }

    public static void setupCustomColorScheme(String colorThemeFileName) {
        ColorPreferences.loadColorScheme(colorThemeFileName);

        final var extraGlobalDefaults = new HashMap<String, String>();
        addDefaultColor(extraGlobalDefaults, "Button.highlight", UIColor.BUTTON_HIGHLIGHT);
        addDefaultColor(extraGlobalDefaults, "Button.background", UIColor.BUTTON_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Button.foreground", UIColor.BUTTON_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "CheckBox.background", UIColor.CHECKBOX_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "CheckBox.foreground", UIColor.CHECKBOX_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "ComboBox.background", UIColor.COMBOBOX_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ComboBox.foreground", UIColor.COMBOBOX_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "ComboBox.buttonBackground", UIColor.COMBOBOX_BUTTON_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ComboBox.selectionBackground", UIColor.COMBOBOX_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ComboBox.selectionForeground", UIColor.COMBOBOX_SELECTION_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "ComboBox.selectedInDropDownBackground", UIColor.COMBOBOX_SELECTED_IN_DROP_DOWN_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "Label.background", UIColor.LABEL_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Label.foreground", UIColor.LABEL_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "Menu.background", UIColor.MENU_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Menu.foreground", UIColor.MENU_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Menu.selectionBackground", UIColor.MENU_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Menu.selectionForeground", UIColor.MENU_SELECTION_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Menu.disabledForeground", UIColor.MENU_DISABLED_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "MenuBar.background", UIColor.MENU_BAR_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "MenuBar.foreground", UIColor.MENU_BAR_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "MenuItem.disabledForeground", UIColor.MENU_ITEM_DISABLED_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "MenuItem.selectionBackground", UIColor.MENU_ITEM_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "MenuItem.selectionForeground", UIColor.MENU_ITEM_SELECTION_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "MenuItem.background", UIColor.MENU_ITEM_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "MenuItem.foreground", UIColor.MENU_ITEM_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "OptionPane.background", UIColor.OPTION_PANE_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "Panel.background", UIColor.PANEL_BACKGROUND_COLOR);

        addDefaultColor(extraGlobalDefaults, "PopupMenu.background", UIColor.POPUP_MENU_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "PopupMenu.foreground", UIColor.POPUP_MENU_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "RadioButton.background", UIColor.RADIO_BUTTON_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "RadioButton.foreground", UIColor.RADIO_BUTTON_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "Spinner.background", UIColor.SPINNER_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Spinner.foreground", UIColor.SPINNER_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Spinner.arrowButtonBackground", UIColor.SPINNER_ARROW_BUTTON_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "ScrollBar.track", UIColor.SCROLL_BAR_TRACK);
        addDefaultColor(extraGlobalDefaults, "ScrollBar.thumb", UIColor.SCROLL_BAR_THUMB);
        addDefaultColor(extraGlobalDefaults, "ScrollBar.thumbDarkShadow", UIColor.SCROLL_BAR_THUMB_DARK_SHADOW);
        addDefaultColor(extraGlobalDefaults, "ScrollBar.thumbHighlight", UIColor.SCROLL_BAR_THUMB_HIGHLIGHT);
        addDefaultColor(extraGlobalDefaults, "ScrollBar.thumbShadow", UIColor.SCROLL_BAR_THUMB_SHADOW);
        addDefaultColor(extraGlobalDefaults, "ScrollBar.arrowButtonBackground", UIColor.SCROLL_BAR_ARROW_BUTTON_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "ScrollPane.background", UIColor.SCROLL_PANE_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "Slider.background", UIColor.SLIDER_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Slider.foreground", UIColor.SLIDER_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Slider.trackColor", UIColor.SLIDER_TRACK_COLOR);

        addDefaultColor(extraGlobalDefaults, "SplitPane.background", UIColor.SPLIT_PANE_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "TabbedPane.background", UIColor.TABBED_PANE_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "TabbedPane.foreground", UIColor.TABBED_PANE_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "TabbedPane.highlight", UIColor.TABBED_PANE_HIGHLIGHT);
        addDefaultColor(extraGlobalDefaults, "TabbedPane.borderHighlightColor", UIColor.TABBED_PANE_BORDER_HIGHLIGHT);

        addDefaultColor(extraGlobalDefaults, "Table.selectionBackground", UIColor.TABLE_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Table.selectionForeground", UIColor.TABLE_SELECTION_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Table.background", UIColor.TABLE_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Table.gridColor", UIColor.TABLE_GRID_COLOR);
        addDefaultColor(extraGlobalDefaults, "TableHeader.background", UIColor.TABLE_HEADER_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "TextArea.background", UIColor.TEXT_AREA_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "TextArea.foreground", UIColor.TEXT_AREA_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "ToggleButton.background", UIColor.TOGGLE_BUTTON_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ToggleButton.foreground", UIColor.TOGGLE_BUTTON_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "ToolBar.background", UIColor.TOOL_BAR_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ToolBar.foreground", UIColor.TOOL_BAR_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "ToolBar.dockingBackground", UIColor.TOOL_BAR_DOCKING_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ToolBar.floatingBackground", UIColor.TOOL_BAR_FLOATING_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "Tree.selectionForeground", UIColor.TREE_SELECTION_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Tree.foreground", UIColor.TREE_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "Tree.selectionBackground", UIColor.TREE_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Tree.background", UIColor.TREE_BACKGROUND);

        addDefaultColor(extraGlobalDefaults, "RadioButtonMenuItem.foreground", UIColor.RADIO_BUTTON_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "RadioButtonMenuItem.selectionForeground", UIColor.RADIO_BUTTON_MENU_ITEM_SELECTION_FOREGROUND);
        // If it changes the background of the menuitem it must change this too, irrespective of its
        // setting
        addDefaultColor(extraGlobalDefaults, "RadioButtonMenuItem.selectionBackground", UIColor.RADIO_BUTTON_MENU_ITEM_SELECTION_BACKGROUND);

        // If it changes the background of the menuitem it must change this too, irrespective of its
        // setting
        addDefaultColor(extraGlobalDefaults, "CheckBoxMenuItem.selectionBackground", UIColor.CHECKBOX_MENU_ITEM_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "CheckBoxMenuItem.foreground", UIColor.CHECKBOX_MENU_ITEM_FOREGROUND);
        addDefaultColor(extraGlobalDefaults, "CheckBoxMenuItem.selectionForeground", UIColor.CHECKBOX_MENU_ITEM_SELECTION_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "TextPane.background", UIColor.TEXT_PANE_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "TextPane.selectionBackground", UIColor.TEXT_PANE_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "TextPane.inactiveForeground", UIColor.TEXT_PANE_INACTIVE_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "EditorPane.background", UIColor.EDITOR_PANE_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "EditorPane.selectionBackground", UIColor.EDITOR_PANE_SELECTION_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "EditorPane.inactiveForeground", UIColor.EDITOR_PANE_INACTIVE_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "Separator.background", UIColor.SEPARATOR_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "Separator.foreground", UIColor.SEPARATOR_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "ToolTip.background", UIColor.TOOL_TIP_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ToolTip.foreground", UIColor.TOOL_TIP_FOREGROUND);

        addDefaultColor(extraGlobalDefaults, "ColorChooser.background", UIColor.COLOR_CHOOSER_BACKGROUND);
        addDefaultColor(extraGlobalDefaults, "ColorChooser.foreground", UIColor.COLOR_CHOOSER_FOREGROUND);

        FlatLaf.setGlobalExtraDefaults(extraGlobalDefaults);
    }
}
