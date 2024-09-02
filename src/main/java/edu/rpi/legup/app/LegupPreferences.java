package edu.rpi.legup.app;

import edu.rpi.legup.ui.color.ColorPreferences;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.prefs.Preferences;

public class LegupPreferences {

    private static LegupPreferences instance;

    private static String savedPath = "";

    private static final Preferences preferences =
            Preferences.userNodeForPackage(LegupPreferences.class);

    private static final Map<LegupPreference, Object> preferencesMap = new EnumMap<>(LegupPreference.class);

    public enum LegupPreference {
        WORK_DIRECTORY("work-directory", System.getProperty("user.dir"), o -> o),
        START_FULL_SCREEN("start-full-screen", false, Boolean::parseBoolean),
        AUTO_UPDATE("auto-update", true, Boolean::parseBoolean),
        DARK_MODE("night-mode", false, Boolean::parseBoolean),
        USE_CUSTOM_COLOR_THEME("use-custom-color-theme", false, Boolean::parseBoolean),
        SHOW_MISTAKES("show-mistakes", true, Boolean::parseBoolean),
        SHOW_ANNOTATIONS("show-annotations", true, Boolean::parseBoolean),
        ALLOW_DEFAULT_RULES("allow-default-rules", false, Boolean::parseBoolean),
        AUTO_GENERATE_CASES("auto-generate-cases", true, Boolean::parseBoolean),
        IMMEDIATE_FEEDBACK("immediate-feedback", true, Boolean::parseBoolean),
        COLOR_THEME_FILE("color-theme-file", System.getProperty("user.dir") + File.separator + ColorPreferences.LIGHT_COLOR_THEME_FILE_NAME, o -> o),
        COLOR_BLIND("color-blind", false, Boolean::parseBoolean);

        private final String id;
        private final Object defaultValue;
        /**
         * Converts the object to a string so that it can be saved in preferences
         */
        private final Function<Object, String> stringMapper;
        /**
         * Converts a string value to an object so it's more convenient to use in code
         */
        private final Function<String, Object> stringToValueMapper;

        @SuppressWarnings("unchecked")
        <T> LegupPreference(
                String id,
                T defaultValue,
                Function<T, String> stringMapper,
                Function<String, T> stringToValueMapper
        ) {
            this.id = id;
            this.defaultValue = defaultValue;
            this.stringMapper = (Function<Object, String>) stringMapper;
            this.stringToValueMapper = (Function<String, Object>) stringToValueMapper;
        }

        <T> LegupPreference(String id, T defaultValue, Function<String, Object> stringToValueMapper) {
            this(id, defaultValue, String::valueOf, stringToValueMapper);
        }

        public String id() {
            return this.id;
        }

        public String defaultStringValue() {
            return stringMapper.apply(defaultValue);
        }

        @SuppressWarnings("unused")
        public Object defaultValue() {
            return defaultValue;
        }

        public String stringValue() {
            return stringMapper.apply(preferencesMap.get(this));
        }

        /**
         * Convenience method to return the value of this preference cast to {@code clazz}
         * @param clazz
         * @return
         * @param <T>
         */
        public <T> T as(Class<T> clazz) {
            return clazz.cast(stringToValueMapper.apply(stringValue()));
        }

        public boolean asBoolean() {
            return as(Boolean.class);
        }

        public boolean asBoolean(String errorMessage) {
            return as(Boolean.class, errorMessage);
        }

        public <T> T as(Class<T> clazz, String errorMessage) {
            try {
                return clazz.cast(defaultValue);
            } catch (Exception e) {
                throw new RuntimeException(errorMessage);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T convertToValue(String value) {
            return (T) stringToValueMapper.apply(value);
        }

        public String convertToString(Object value) {
            return stringMapper.apply(value);
        }
    }

    private static Object getPreferenceOrDefault(LegupPreference preference) {
        final String current = preferences.get(preference.id(), null);
        if (current == null) {
            return preference.defaultValue;
        }
        return current;
    }

    private static void addPreferenceFromDefault(LegupPreference preference) {
        preferencesMap.put(preference, getPreferenceOrDefault(preference));
    }

    static {
        for (final LegupPreference preference : LegupPreference.values()) {
            addPreferenceFromDefault(preference);
        }
    }

    /**
     * Gets the legup preferences singleton instance.
     *
     * @return legup preferences
     */
    public static LegupPreferences getInstance() {
        if (instance == null) {
            instance = new LegupPreferences();
        }
        return instance;
    }

    /**
     * Private LegupPreferences Singleton Constructor
     */
    private LegupPreferences() {
    }

    /**
     * Gets the user preference by the string key
     *
     * @param preference the preference to get
     * @return value of the preference
     */
    private Object getUserPref(LegupPreference preference) {
        return preferencesMap.get(preference);
    }

    /**
     * Gets the user preference by the string key, value pair
     *
     * @param preference the preference to change
     * @param value      value of the preference
     */
    public void setUserPref(LegupPreference preference, Object value) {
        preferences.put(preference.id(), preference.convertToString(value));
        preferencesMap.put(preference, value);
    }

    public boolean getUserPrefAsBool(LegupPreference preference) {
        return preference.asBoolean("Cannot get user preference - " + preference.id());
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String path) {
        savedPath = path;
    }

    public static boolean colorBlind() {
        return LegupPreference.COLOR_BLIND.asBoolean();
    }

    public static boolean darkMode() {
        return LegupPreference.DARK_MODE.asBoolean();
    }

    public static boolean showAnnotations() {
        return LegupPreference.SHOW_ANNOTATIONS.asBoolean();
    }

    public static boolean allowDefaultRules() {
        return LegupPreference.ALLOW_DEFAULT_RULES.asBoolean();
    }

    public static String workDirectory() {
        return LegupPreference.WORK_DIRECTORY.stringValue();
    }

    public static boolean startFullScreen() {
        return LegupPreference.START_FULL_SCREEN.asBoolean();
    }

    public static boolean autoUpdate() {
        return LegupPreference.AUTO_UPDATE.asBoolean();
    }

    public static boolean showMistakes() {
        return LegupPreference.SHOW_MISTAKES.asBoolean();
    }

    public static boolean autoGenerateCases() {
        return LegupPreference.AUTO_GENERATE_CASES.asBoolean();
    }

    public static boolean immediateFeedback() {
        return LegupPreference.IMMEDIATE_FEEDBACK.asBoolean();
    }

    public static boolean useCustomColorTheme() {
        return LegupPreference.USE_CUSTOM_COLOR_THEME.asBoolean();
    }

    public static String colorThemeFile() {
        return LegupPreference.COLOR_THEME_FILE.stringValue();
    }

}
