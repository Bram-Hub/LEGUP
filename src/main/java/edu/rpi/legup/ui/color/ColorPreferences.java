package edu.rpi.legup.ui.color;

//import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;

import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ColorPreferences {

    public static final String LIGHT_COLOR_THEME_FILE_NAME = "light-color-theme.txt";
    public static final String DARK_COLOR_THEME_FILE_NAME = "dark-color-theme.txt";

    private static final Map<UIColor, Color> COLOR_MAP = new EnumMap<>(UIColor.class);

    public enum UIColor {

        CORRECT(Color.GREEN),
        INCORRECT(Color.RED),
        ERROR(Color.RED),
        INFO(Color.GREEN),
        UI_MOVEMENT(MaterialColors.GRAY_300),
        PASSWORD_FIELD_BACKGROUND(MaterialColors.LIGHT_BLUE_400),
        PASSWORD_FIELD_UNFOCUSED_BACKGROUND(MaterialColors.GRAY_200),
        PROGRESS_BAR_BACKGROUND(MaterialColors.GRAY_200),
        PROGRESS_BAR_FOREGROUND(MaterialColors.LIGHT_BLUE_400),
        TEXT_FIELD_BACKGROUND(MaterialColors.LIGHT_BLUE_400),
        TEXT_FIELD_UNFOCUSED_BACKGROUND(MaterialColors.GRAY_200),
        LIGHT_LINE_BORDER(MaterialColors.GRAY_200),
        THICK_LINE_BORDER(MaterialColors.GRAY_200),
        DATA_SELECTION_BACKGROUND(Color.GRAY),
        ELEMENT_VIEW(Color.BLACK),
        BUTTON_HIGHLIGHT,
        BUTTON_BACKGROUND,
        BUTTON_FOREGROUND,
        CHECKBOX_BACKGROUND,
        CHECKBOX_FOREGROUND,
        COMBOBOX_BACKGROUND,
        COMBOBOX_FOREGROUND,
        COMBOBOX_BUTTON_BACKGROUND,
        COMBOBOX_SELECTION_BACKGROUND,
        COMBOBOX_SELECTION_FOREGROUND,
        COMBOBOX_SELECTED_IN_DROP_DOWN_BACKGROUND,
        LABEL_BACKGROUND,
        LABEL_FOREGROUND,
        MENU_BACKGROUND,
        MENU_FOREGROUND,
        MENU_SELECTION_BACKGROUND,
        MENU_SELECTION_FOREGROUND,
        MENU_DISABLED_FOREGROUND,
        MENU_BAR_BACKGROUND,
        MENU_BAR_FOREGROUND,
        MENU_ITEM_DISABLED_FOREGROUND,
        MENU_ITEM_SELECTION_BACKGROUND,
        MENU_ITEM_SELECTION_FOREGROUND,
        MENU_ITEM_BACKGROUND,
        MENU_ITEM_FOREGROUND,
        OPTION_PANE_BACKGROUND,
        PANEL_BACKGROUND_COLOR,
        POPUP_MENU_BACKGROUND,
        POPUP_MENU_FOREGROUND,
        RADIO_BUTTON_BACKGROUND,
        RADIO_BUTTON_FOREGROUND,
        SPINNER_BACKGROUND,
        SPINNER_FOREGROUND,
        SPINNER_ARROW_BUTTON_BACKGROUND,
        SCROLL_BAR_TRACK,
        SCROLL_BAR_THUMB,
        SCROLL_BAR_THUMB_DARK_SHADOW,
        SCROLL_BAR_THUMB_HIGHLIGHT,
        SCROLL_BAR_THUMB_SHADOW,
        SCROLL_BAR_ARROW_BUTTON_BACKGROUND,
        SCROLL_PANE_BACKGROUND,
        SLIDER_BACKGROUND,
        SLIDER_FOREGROUND,
        SLIDER_TRACK_COLOR,
        SPLIT_PANE_BACKGROUND,
        TABBED_PANE_BACKGROUND,
        TABBED_PANE_FOREGROUND,
        TABBED_PANE_HIGHLIGHT,
        TABBED_PANE_BORDER_HIGHLIGHT,
        TABLE_SELECTION_BACKGROUND,
        TABLE_SELECTION_FOREGROUND,
        TABLE_BACKGROUND,
        TABLE_GRID_COLOR,
        TABLE_HEADER_BACKGROUND,
        TEXT_AREA_BACKGROUND,
        TEXT_AREA_FOREGROUND,
        TOGGLE_BUTTON_BACKGROUND,
        TOGGLE_BUTTON_FOREGROUND,
        TOOL_BAR_BACKGROUND,
        TOOL_BAR_FOREGROUND,
        TOOL_BAR_DOCKING_BACKGROUND,
        TOOL_BAR_FLOATING_BACKGROUND,
        TREE_SELECTION_FOREGROUND,
        TREE_FOREGROUND,
        TREE_SELECTION_BACKGROUND,
        TREE_BACKGROUND,
        RADIO_BUTTON_MENU_ITEM_FOREGROUND,
        RADIO_BUTTON_MENU_ITEM_SELECTION_FOREGROUND,
        RADIO_BUTTON_MENU_ITEM_SELECTION_BACKGROUND,
        CHECKBOX_MENU_ITEM_SELECTION_BACKGROUND,
        CHECKBOX_MENU_ITEM_FOREGROUND,
        CHECKBOX_MENU_ITEM_SELECTION_FOREGROUND,
        TEXT_PANE_BACKGROUND,
        TEXT_PANE_SELECTION_BACKGROUND,
        TEXT_PANE_INACTIVE_FOREGROUND,
        EDITOR_PANE_BACKGROUND,
        EDITOR_PANE_SELECTION_BACKGROUND,
        EDITOR_PANE_INACTIVE_FOREGROUND,
        SEPARATOR_BACKGROUND,
        SEPARATOR_FOREGROUND,
        TOOL_TIP_BACKGROUND,
        TOOL_TIP_FOREGROUND,
        COLOR_CHOOSER_BACKGROUND,
        COLOR_CHOOSER_FOREGROUND;

        private final Color defaultColor;

        UIColor() {
            this.defaultColor = null;
        }

        UIColor(Color defaultColor) {
            this.defaultColor = defaultColor;
        }

        public String configKey() {
            return this.toString().toLowerCase().replace('_', '-');
        }

        public Optional<Color> get() {
            return Optional.ofNullable(COLOR_MAP.getOrDefault(this, defaultColor));
        }

        /**
         * This method should only be used when this color <b>needs</b> to exist
         */
        public Color getOrThrow() throws IllegalStateException {
            return this.get().orElseThrow(() -> new IllegalStateException(this + " is a required color!"));
        }

        public Optional<String> getAsHex() {
            return this.get()
                    .map(color -> String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
        }
    }

    private static void checkNewColors(Path path, Set<UIColor> usedColors) {
        if (usedColors.size() == UIColor.values().length) {
            return;
        }
        final InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(path.getFileName().toString());
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final Map<UIColor, Color> newColors = loadColors(reader.lines().toList());
        final Map<UIColor, Color> addColors = newColors.entrySet().stream()
                .filter(e -> !usedColors.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        try {
            for (final var entry : addColors.entrySet()) {
                final Color color = entry.getValue();
                final String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                Files.writeString(
                        path,
                        entry.getKey().configKey() + ": " + hex + "\n",
                        StandardOpenOption.APPEND
                );
                COLOR_MAP.put(entry.getKey(), color);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @Unmodifiable Map<UIColor, Color> loadColors(List<String> lines) {
        return lines.stream()
                .filter(l -> !l.startsWith("//")) // Use // for comments
                .map(l -> l.split(":"))
                .filter(a -> a.length == 2)
                .collect(Collectors.toUnmodifiableMap(e -> UIColor.valueOf(e[0].replace("-", "_").toUpperCase()), e -> colorFromString(e[1].strip())));
    }

    public static void loadColorScheme(String fileName) {
        final Path path = Path.of(fileName);
        final File file = path.toFile();
        final InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(path.getFileName().toString());
        BufferedReader reader;
        boolean copyResourceToFile = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                copyResourceToFile = true;
            }
            catch (IOException e) {
                System.err.println("Could not create " + fileName);
            }
            if (input == null) {
                throw new RuntimeException("Could not find resource " + fileName);
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
            lines.forEach(line -> {
                try {
                    Files.writeString(path, line + System.lineSeparator(), StandardOpenOption.APPEND);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        COLOR_MAP.putAll(loadColors(lines));

        checkNewColors(path, COLOR_MAP.keySet());
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
