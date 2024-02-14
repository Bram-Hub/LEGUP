package edu.rpi.legup.ui.lookandfeel;

import edu.rpi.legup.ui.lookandfeel.components.*;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialBorders;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialImages;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

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

        table.put("Button.highlight", MaterialColors.GRAY_300);
        table.put("Button.opaque", false);
        table.put("Button.border", BorderFactory.createEmptyBorder(7, 17, 7, 17));
        table.put("Button.background", MaterialColors.GRAY_200);
        table.put("Button.foreground", Color.BLACK);
        table.put("Button.font", MaterialFonts.MEDIUM);

        table.put("CheckBox.font", MaterialFonts.REGULAR);
        table.put("CheckBox.background", Color.WHITE);
        table.put("CheckBox.foreground", Color.BLACK);
        table.put("CheckBox.icon", new ImageIcon(MaterialImages.UNCHECKED_BOX));
        table.put("CheckBox.selectedIcon", new ImageIcon(MaterialImages.PAINTED_CHECKED_BOX));

        table.put("ComboBox.font", MaterialFonts.REGULAR);
        table.put("ComboBox.background", Color.WHITE);
        table.put("ComboBox.foreground", Color.BLACK);
        table.put(
                "ComboBox.border",
                BorderFactory.createCompoundBorder(
                        MaterialBorders.LIGHT_LINE_BORDER,
                        BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        table.put("ComboBox.buttonBackground", MaterialColors.GRAY_300);
        table.put("ComboBox.selectionBackground", Color.WHITE);
        table.put("ComboBox.selectionForeground", Color.BLACK);
        table.put("ComboBox.selectedInDropDownBackground", MaterialColors.GRAY_200);

        table.put("Label.font", MaterialFonts.REGULAR);
        table.put("Label.background", Color.WHITE);
        table.put("Label.foreground", Color.BLACK);
        table.put("Label.border", BorderFactory.createEmptyBorder());

        table.put("Menu.font", MaterialFonts.BOLD);
        table.put("Menu.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        table.put("Menu.background", Color.WHITE);
        table.put("Menu.foreground", Color.BLACK);
        table.put("Menu.opaque", true);
        table.put("Menu.selectionBackground", MaterialColors.GRAY_200);
        table.put("Menu.selectionForeground", Color.BLACK);
        table.put("Menu.disabledForeground", new Color(0, 0, 0, 100));
        table.put("Menu.menuPopupOffsetY", 3);

        table.put("MenuBar.font", MaterialFonts.BOLD);
        table.put("MenuBar.background", Color.WHITE);
        table.put("MenuBar.border", MaterialBorders.LIGHT_SHADOW_BORDER);
        table.put("MenuBar.foreground", Color.BLACK);

        table.put("MenuItem.disabledForeground", new Color(0, 0, 0, 100));
        table.put("MenuItem.selectionBackground", MaterialColors.GRAY_200);
        table.put("MenuItem.selectionForeground", Color.BLACK);
        table.put("MenuItem.font", MaterialFonts.MEDIUM);
        table.put("MenuItem.background", Color.WHITE);
        table.put("MenuItem.foreground", Color.BLACK);
        table.put("MenuItem.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));

        table.put("OptionPane.background", Color.WHITE);
        table.put("OptionPane.border", MaterialBorders.DEFAULT_SHADOW_BORDER);
        table.put("OptionPane.font", MaterialFonts.REGULAR);

        table.put("Panel.font", MaterialFonts.REGULAR);
        table.put("Panel.background", Color.WHITE);
        table.put("Panel.border", BorderFactory.createEmptyBorder());

        table.put("PopupMenu.border", MaterialBorders.LIGHT_LINE_BORDER);
        table.put("PopupMenu.background", Color.WHITE);
        table.put("PopupMenu.foreground", Color.BLACK);

        table.put("RadioButton.font", MaterialFonts.REGULAR);
        table.put("RadioButton.background", Color.WHITE);
        table.put("RadioButton.foreground", Color.BLACK);
        table.put("RadioButton.icon", new ImageIcon(MaterialImages.RADIO_BUTTON_OFF));
        table.put("RadioButton.selectedIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_ON));

        table.put("Spinner.font", MaterialFonts.REGULAR);
        table.put("Spinner.background", Color.WHITE);
        table.put("Spinner.foreground", Color.BLACK);
        table.put("Spinner.border", MaterialBorders.LIGHT_LINE_BORDER);
        table.put("Spinner.arrowButtonBackground", MaterialColors.GRAY_200);
        table.put("Spinner.arrowButtonBorder", BorderFactory.createEmptyBorder());

        table.put("ScrollBar.font", MaterialFonts.REGULAR);
        table.put("ScrollBar.track", MaterialColors.GRAY_200);
        table.put("ScrollBar.thumb", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumbDarkShadow", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumbHighlight", MaterialColors.GRAY_300);
        table.put("ScrollBar.thumbShadow", MaterialColors.GRAY_300);
        table.put("ScrollBar.arrowButtonBackground", MaterialColors.GRAY_300);
        table.put("ScrollBar.arrowButtonBorder", BorderFactory.createEmptyBorder());

        table.put("ScrollPane.background", Color.WHITE);
        table.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        table.put("ScrollPane.font", MaterialFonts.REGULAR);

        table.put("Slider.font", MaterialFonts.REGULAR);
        table.put("Slider.background", Color.WHITE);
        table.put("Slider.foreground", MaterialColors.GRAY_700);
        table.put("Slider.trackColor", Color.BLACK);
        //        table.put ("Slider.border",
        // BorderFactory.createCompoundBorder(MaterialBorders.LIGHT_LINE_BORDER,
        // BorderFactory.createEmptyBorder (5, 5, 5, 5)));

        table.put("SplitPane.border", MaterialBorders.LIGHT_LINE_BORDER);
        table.put("SplitPane.background", Color.WHITE);
        table.put("SplitPane.dividerSize", 10);
        table.put("SplitPaneDivider.border", MaterialBorders.LIGHT_SHADOW_BORDER);

        table.put("TabbedPane.font", MaterialFonts.REGULAR);
        table.put("TabbedPane.background", Color.WHITE);
        table.put("TabbedPane.foreground", Color.BLACK);
        table.put("TabbedPane.border", BorderFactory.createEmptyBorder());
        table.put("TabbedPane.shadow", null);
        table.put("TabbedPane.darkShadow", null);
        table.put("TabbedPane.highlight", MaterialColors.GRAY_200);
        table.put("TabbedPane.borderHighlightColor", MaterialColors.GRAY_300);

        table.put("Table.selectionBackground", MaterialColors.GRAY_100);
        table.put("Table.selectionForeground", Color.BLACK);
        table.put("Table.background", Color.WHITE);
        table.put("Table.font", MaterialFonts.REGULAR);
        table.put("Table.border", MaterialBorders.LIGHT_LINE_BORDER);
        table.put("Table.gridColor", MaterialColors.GRAY_200);
        table.put("TableHeader.background", MaterialColors.GRAY_200);
        table.put("TableHeader.font", MaterialFonts.BOLD);
        table.put(
                "TableHeader.cellBorder",
                BorderFactory.createCompoundBorder(
                        MaterialBorders.LIGHT_LINE_BORDER,
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        table.put("TextArea.background", MaterialColors.GRAY_200);
        table.put("TextArea.border", BorderFactory.createEmptyBorder());
        table.put("TextArea.foreground", Color.BLACK);

        table.put("ToggleButton.border", BorderFactory.createEmptyBorder());
        table.put("ToggleButton.font", MaterialFonts.REGULAR);
        table.put("ToggleButton.background", Color.WHITE);
        table.put("ToggleButton.foreground", Color.BLACK);
        table.put("ToggleButton.icon", new ImageIcon(MaterialImages.TOGGLE_BUTTON_OFF));
        table.put("ToggleButton.selectedIcon", new ImageIcon(MaterialImages.TOGGLE_BUTTON_ON));

        table.put("ToolBar.font", MaterialFonts.REGULAR);
        table.put("ToolBar.background", Color.WHITE);
        table.put("ToolBar.foreground", Color.BLACK);
        table.put("ToolBar.border", MaterialBorders.LIGHT_SHADOW_BORDER);
        table.put("ToolBar.dockingBackground", MaterialColors.LIGHT_GREEN_A100);
        table.put("ToolBar.floatingBackground", MaterialColors.GRAY_200);

        table.put("Tree.font", MaterialFonts.REGULAR);
        table.put("Tree.selectionForeground", Color.BLACK);
        table.put("Tree.foreground", Color.BLACK);
        table.put("Tree.selectionBackground", MaterialColors.GRAY_200);
        table.put("Tree.background", Color.WHITE);
        table.put("Tree.closedIcon", new ImageIcon(MaterialImages.RIGHT_ARROW));
        table.put("Tree.openIcon", new ImageIcon(MaterialImages.DOWN_ARROW));
        table.put("Tree.selectionBorderColor", null);

        table.put("RadioButtonMenuItem.foreground", Color.BLACK);
        table.put("RadioButtonMenuItem.selectionForeground", Color.BLACK);
        // If it changes the background of the menuitem it must change this too, irrespective of its
        // setting
        table.put("RadioButtonMenuItem.background", UIManager.getColor("MenuItem.background"));
        table.put("RadioButtonMenuItem.selectionBackground", MaterialColors.GRAY_200);
        table.put("RadioButtonMenuItem.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        table.put("RadioButtonMenuItem.checkIcon", new ImageIcon(MaterialImages.RADIO_BUTTON_OFF));
        table.put(
                "RadioButtonMenuItem.selectedCheckIcon",
                new ImageIcon(MaterialImages.RADIO_BUTTON_ON));

        // If it changes the background of the menuitem it must change this too, irrespective of its
        // setting
        table.put("CheckBoxMenuItem.background", UIManager.getColor("MenuItem.background"));
        table.put("CheckBoxMenuItem.selectionBackground", MaterialColors.GRAY_200);
        table.put("CheckBoxMenuItem.foreground", Color.BLACK);
        table.put("CheckBoxMenuItem.selectionForeground", Color.BLACK);
        table.put("CheckBoxMenuItem.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));
        table.put("CheckBoxMenuItem.checkIcon", new ImageIcon(MaterialImages.UNCHECKED_BOX));
        table.put(
                "CheckBoxMenuItem.selectedCheckIcon",
                new ImageIcon(MaterialImages.PAINTED_CHECKED_BOX));

        table.put("TextPane.border", MaterialBorders.LIGHT_LINE_BORDER);
        table.put("TextPane.background", MaterialColors.GRAY_50);
        table.put("TextPane.selectionBackground", MaterialColors.LIGHT_BLUE_200);
        table.put("TextPane.inactiveForeground", MaterialColors.GRAY_500);
        table.put("TextPane.font", MaterialFonts.REGULAR);

        table.put("EditorPane.border", MaterialBorders.LIGHT_LINE_BORDER);
        table.put("EditorPane.background", MaterialColors.GRAY_50);
        table.put("EditorPane.selectionBackground", MaterialColors.LIGHT_BLUE_200);
        table.put("EditorPane.inactiveForeground", MaterialColors.GRAY_500);
        table.put("EditorPane.font", MaterialFonts.REGULAR);

        table.put("Separator.background", MaterialColors.GRAY_300);
        table.put("Separator.foreground", MaterialColors.GRAY_300);

        table.put("ToolTip.background", MaterialColors.GRAY_500);
        table.put("ToolTip.foreground", MaterialColors.GRAY_50);
        table.put("ToolTip.border", BorderFactory.createEmptyBorder(5, 5, 5, 5));

        table.put("ColorChooser.background", MaterialColors.WHITE);
        table.put("ColorChooser.foreground", MaterialColors.BLACK);
    }
}
