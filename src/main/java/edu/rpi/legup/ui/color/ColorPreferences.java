package edu.rpi.legup.ui.color;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ColorPreferences {

    private static final String COLOR_THEME_FILE_NAME = "color-theme.txt";

    private static final Map<UIColor, Color> COLOR_MAP = new EnumMap<>(UIColor.class);

    public enum UIColor {

        CORRECT,
        INCORRECT,
        DATA_SELECTION_BACKGROUND,
        ELEMENT_VIEW_DEFAULT,
        BUTTON_HIGHLIGHT_DEFAULT,
        BUTTON_BACKGROUND_DEFAULT,
        BUTTON_FOREGROUND_DEFAULT,
        CHECKBOX_BACKGROUND_DEFAULT,
        CHECKBOX_FOREGROUND_DEFAULT,
        COMBOBOX_BACKGROUND_DEFAULT,
        COMBOBOX_FOREGROUND_DEFAULT,
        COMBOBOX_BUTTON_BACKGROUND_DEFAULT,
        COMBOBOX_SELECTION_BACKGROUND_DEFAULT,
        COMBOBOX_SELECTION_FOREGROUND_DEFAULT,
        COMBOBOX_SELECTED_IN_DROP_DOWN_BACKGROUND_DEFAULT,
        LABEL_BACKGROUND_DEFAULT,
        LABEL_FOREGROUND_DEFAULT,
        MENU_BACKGROUND_DEFAULT,
        MENU_FOREGROUND_DEFAULT,
        MENU_SELECTION_BACKGROUND_DEFAULT,
        MENU_SELECTION_FOREGROUND_DEFAULT,
        MENU_DISABLED_FOREGROUND_DEFAULT,
        MENU_BAR_BACKGROUND_DEFAULT,
        MENU_BAR_FOREGROUND_DEFAULT,
        MENU_ITEM_DISABLED_FOREGROUND_DEFAULT,
        MENU_ITEM_SELECTION_BACKGROUND_DEFAULT,
        MENU_ITEM_SELECTION_FOREGROUND_DEFAULT,
        MENU_ITEM_BACKGROUND_DEFAULT,
        MENU_ITEM_FOREGROUND_DEFAULT,
        OPTION_PANE_BACKGROUND_DEFAULT,
        PANEL_BACKGROUND_COLOR_DEFAULT,
        POPUP_MENU_BACKGROUND_DEFAULT,
        POPUP_MENU_FOREGROUND_DEFAULT,
        RADIO_BUTTON_BACKGROUND_DEFAULT,
        RADIO_BUTTON_FOREGROUND_DEFAULT,
        SPINNER_BACKGROUND_DEFAULT,
        SPINNER_FOREGROUND_DEFAULT,
        SPINNER_ARROW_BUTTON_BACKGROUND_DEFAULT,
        SCROLL_BAR_TRACK_DEFAULT,
        SCROLL_BAR_THUMB_DEFAULT,
        SCROLL_BAR_THUMB_DARK_SHADOW_DEFAULT,
        SCROLL_BAR_THUMB_HIGHLIGHT_DEFAULT,
        SCROLL_BAR_THUMB_SHADOW_DEFAULT,
        SCROLL_BAR_ARROW_BUTTON_BACKGROUND_DEFAULT,
        SCROLL_PANE_BACKGROUND_DEFAULT,
        SLIDER_BACKGROUND_DEFAULT,
        SLIDER_FOREGROUND_DEFAULT,
        SLIDER_TRACK_COLOR_DEFAULT,
        SPLIT_PANE_BACKGROUND_DEFAULT,
        TABBED_PANE_BACKGROUND_DEFAULT,
        TABBED_PANE_FOREGROUND_DEFAULT,
        TABBED_PANE_HIGHLIGHT_DEFAULT,
        TABBED_PANE_BORDER_HIGHLIGHT_DEFAULT,
        TABLE_SELECTION_BACKGROUND_DEFAULT,
        TABLE_SELECTION_FOREGROUND_DEFAULT,
        TABLE_BACKGROUND_DEFAULT,
        TABLE_GRID_COLOR_DEFAULT,
        TABLE_HEADER_BACKGROUND_DEFAULT,
        TEXT_AREA_BACKGROUND_DEFAULT,
        TEXT_AREA_FOREGROUND_DEFAULT,
        TOGGLE_BUTTON_BACKGROUND_DEFAULT,
        TOGGLE_BUTTON_FOREGROUND_DEFAULT,
        TOOL_BAR_BACKGROUND_DEFAULT,
        TOOL_BAR_FOREGROUND_DEFAULT,
        TOOL_BAR_DOCKING_BACKGROUND_DEFAULT,
        TOOL_BAR_FLOATING_BACKGROUND_DEFAULT,
        TREE_SELECTION_FOREGROUND_DEFAULT,
        TREE_FOREGROUND_DEFAULT,
        TREE_SELECTION_BACKGROUND_DEFAULT,
        TREE_BACKGROUND_DEFAULT,
        RADIO_BUTTON_MENU_ITEM_FOREGROUND_DEFAULT,
        RADIO_BUTTON_MENU_ITEM_SELECTION_FOREGROUND_DEFAULT,
        RADIO_BUTTON_MENU_ITEM_SELECTION_BACKGROUND_DEFAULT,
        CHECKBOX_MENU_ITEM_SELECTION_BACKGROUND_DEFAULT,
        CHECKBOX_MENU_ITEM_FOREGROUND_DEFAULT,
        CHECKBOX_MENU_ITEM_SELECTION_FOREGROUND_DEFAULT,
        TEXT_PANE_BACKGROUND_DEFAULT,
        TEXT_PANE_SELECTION_BACKGROUND_DEFAULT,
        TEXT_PANE_INACTIVE_FOREGROUND_DEFAULT,
        EDITOR_PANE_BACKGROUND_DEFAULT,
        EDITOR_PANE_SELECTION_BACKGROUND_DEFAULT,
        EDITOR_PANE_INACTIVE_FOREGROUND_DEFAULT,
        SEPARATOR_BACKGROUND_DEFAULT,
        SEPARATOR_FOREGROUND_DEFAULT,
        TOOL_TIP_BACKGROUND_DEFAULT,
        TOOL_TIP_FOREGROUND_DEFAULT,
        COLOR_CHOOSER_BACKGROUND_DEFAULT,
        COLOR_CHOOSER_FOREGROUND_DEFAULT;

        public String configKey() {
            return this.toString().toLowerCase().replace('_', '-');
        }

        public Color get() {
            return COLOR_MAP.get(this);
        }
    }

    public static void loadColorScheme() {
        final File file = Path.of(COLOR_THEME_FILE_NAME).toFile();
        final InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(COLOR_THEME_FILE_NAME);
        BufferedReader reader;
        boolean copyResourceToFile = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                copyResourceToFile = true;
            }
            catch (IOException e) {
                System.err.println("Could not create " + COLOR_THEME_FILE_NAME);
            }
            if (input == null) {
                throw new RuntimeException("Could not find resource " + COLOR_THEME_FILE_NAME);
            }
            reader = new BufferedReader(new InputStreamReader(input));
        }
        else {
            try {
                reader = new BufferedReader(new FileReader(file));
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException("Could not find file " + file.getAbsoluteFile());
            }
        }
        final List<String> lines = reader.lines().toList();
        if (copyResourceToFile) {
            final Path path = file.toPath();
            lines.forEach(line -> {
                try {
                    Files.writeString(path, line + System.lineSeparator(), StandardOpenOption.APPEND);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Set<UIColor> temp = new HashSet<>();

        COLOR_MAP.putAll(
                lines.stream()
                        .filter(l -> !l.startsWith("//")) // Use // for comments
                        .map(l -> l.split(":"))
                        .filter(a -> a.length == 2)
                        .peek(a -> System.out.println("Contains key: " + a[0] + " " + !temp.add(UIColor.valueOf(a[0].replace("-", "_").toUpperCase()))))
                        .map(e -> Map.entry(UIColor.valueOf(e[0].replace("-", "_").toUpperCase()), colorFromString(e[1].strip())))
                        .peek(e -> System.out.println("Key: " + e.getKey() + " value: " + e.getValue()))
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
//                        .collect(Collectors.toMap(e -> UIColor.valueOf(e[0].replace("-", "_").toUpperCase()), e -> colorFromString(e[1].strip()))));
        System.out.println("Colors: " + COLOR_MAP);
    }

    public static Color colorFromString(String color) {
        try {
            return (Color) Color.class.getField(color).get(null);
        }
        catch (NullPointerException | NoSuchFieldException | IllegalAccessException | ClassCastException ignored) {
            try {
                return (Color) MaterialColors.class.getField(color).get(null);
            }
            catch (NullPointerException | NoSuchFieldException | IllegalAccessException | ClassCastException ignored2) {
                if (color.startsWith("#")) {
                    return new Color(HexFormat.fromHexDigits(color.replace("#", "")));
                }
                return Color.getColor(color);
            }
        }
    }

}
