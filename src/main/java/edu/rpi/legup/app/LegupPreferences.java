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
    private static final Map<LegupPreference, Object> defaultPreferencesMap = new EnumMap<>(LegupPreference.class);

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
        private final Function<Object, String> stringMapper;
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
            return clazz.cast(defaultValue);
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

//    public static final String WORK_DIRECTORY = "work-directory";
//    public static final String START_FULL_SCREEN = "start-full-screen";
//    public static final String AUTO_UPDATE = "auto-update";
//    public static final String DARK_MODE = "night-mode";
//    public static final String USE_CUSTOM_COLOR_THEME = "use-custom-color-theme";
//    public static final String SHOW_MISTAKES = "show-mistakes";
//    public static final String SHOW_ANNOTATIONS = "show-annotations";
//    public static final String ALLOW_DEFAULT_RULES = "allow-default-rules";
//    public static final String AUTO_GENERATE_CASES = "auto-generate-cases";
//    public static final String IMMEDIATE_FEEDBACK = "immediate-feedback";
//    public static final String COLOR_THEME_FILE = "color-theme-file";
//    public static final String COLOR_BLIND = "color-blind";

    private static void addDefaultPreference(LegupPreference preference) {
        defaultPreferencesMap.put(preference, preference.defaultStringValue());
    }

    static {
        for (final LegupPreference preference : LegupPreference.values()) {
            addDefaultPreference(preference);
        }
//        defaultPreferencesMap.put(WORK_DIRECTORY, System.getProperty("user.dir"));
//        defaultPreferencesMap.put(START_FULL_SCREEN, Boolean.toString(false));
//        defaultPreferencesMap.put(AUTO_UPDATE, Boolean.toString(true));
//        defaultPreferencesMap.put(DARK_MODE, Boolean.toString(false));
//        defaultPreferencesMap.put(USE_CUSTOM_COLOR_THEME, Boolean.toString(false));
//        defaultPreferencesMap.put(SHOW_MISTAKES, Boolean.toString(true));
//        defaultPreferencesMap.put(SHOW_ANNOTATIONS, Boolean.toString(false));
//        defaultPreferencesMap.put(ALLOW_DEFAULT_RULES, Boolean.toString(false));
//        defaultPreferencesMap.put(AUTO_GENERATE_CASES, Boolean.toString(true));
//        defaultPreferencesMap.put(IMMEDIATE_FEEDBACK, Boolean.toString(true));
//        defaultPreferencesMap.put(COLOR_BLIND, Boolean.toString(false));
//        defaultPreferencesMap.put(COLOR_THEME_FILE, System.getProperty("user.dir") + File.separator + ColorPreferences.LIGHT_COLOR_THEME_FILE_NAME);
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
//        preferencesMap.put(
//                WORK_DIRECTORY,
//                preferences.get(WORK_DIRECTORY, defaultPreferencesMap.get(WORK_DIRECTORY)));
//        preferencesMap.put(
//                START_FULL_SCREEN,
//                preferences.get(START_FULL_SCREEN, defaultPreferencesMap.get(START_FULL_SCREEN)));
//        preferencesMap.put(
//                AUTO_UPDATE, preferences.get(AUTO_UPDATE, defaultPreferencesMap.get(AUTO_UPDATE)));
//        preferencesMap.put(
//                DARK_MODE, preferences.get(DARK_MODE, defaultPreferencesMap.get(DARK_MODE)));
//        preferencesMap.put(
//                SHOW_MISTAKES,
//                preferences.get(SHOW_MISTAKES, defaultPreferencesMap.get(SHOW_MISTAKES)));
//        preferencesMap.put(
//                SHOW_ANNOTATIONS,
//                preferences.get(SHOW_ANNOTATIONS, defaultPreferencesMap.get(SHOW_ANNOTATIONS)));
//        preferencesMap.put(
//                ALLOW_DEFAULT_RULES,
//                preferences.get(
//                        ALLOW_DEFAULT_RULES, defaultPreferencesMap.get(ALLOW_DEFAULT_RULES)));
//        preferencesMap.put(
//                AUTO_GENERATE_CASES,
//                preferences.get(
//                        AUTO_GENERATE_CASES, defaultPreferencesMap.get(AUTO_GENERATE_CASES)));
//        preferencesMap.put(
//                IMMEDIATE_FEEDBACK,
//                preferences.get(IMMEDIATE_FEEDBACK, defaultPreferencesMap.get(IMMEDIATE_FEEDBACK)));
//        preferencesMap.put(
//                COLOR_BLIND, preferences.get(COLOR_BLIND, defaultPreferencesMap.get(COLOR_BLIND)));
//        preferencesMap.put(COLOR_THEME_FILE, preferences.get(COLOR_THEME_FILE, defaultPreferencesMap.get(COLOR_THEME_FILE)));
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
//        if (preferencesMap.get(preference).equalsIgnoreCase(Boolean.toString(true))) {
//            return true;
//        }
//        else {
//            if (preferencesMap.get(preference).equalsIgnoreCase(Boolean.toString(false))) {
//                return false;
//            }
//            else {
//                throw new RuntimeException("Cannot get user preference - " + preference.id());
//            }
//        }
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
}
