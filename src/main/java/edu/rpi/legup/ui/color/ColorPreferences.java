package edu.rpi.legup.ui.color;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ColorPreferences {

    private static final String COLOR_THEME_FILE_NAME = "color-theme.txt";

    private static final Map<UIColor, Color> COLOR_MAP = new EnumMap<>(UIColor.class);

    public enum UIColor {

        CORRECT,
        INCORRECT

        ;

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

        COLOR_MAP.putAll(lines.stream()
                .filter(l -> !l.startsWith("//")) // Use // for comments
                .map(l -> l.split(":"))
                .filter(a -> a.length == 2)
                .peek(a -> System.out.println(Arrays.toString(a)))
                .collect(Collectors.toMap(e -> UIColor.valueOf(e[0]), e -> colorFromString(e[1].strip()))));
        System.out.println("Colors: " + COLOR_MAP);
    }

    public static Color colorFromString(String color) {
        try {
            return (Color) Color.class.getField(color).get(null);
        }
        catch (NullPointerException | NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            return Color.getColor(color);
        }
    }

}
