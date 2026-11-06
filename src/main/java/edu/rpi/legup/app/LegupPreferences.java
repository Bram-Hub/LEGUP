package edu.rpi.legup.app;

import edu.rpi.legup.ui.LegupUI;

import javax.swing.*;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.prefs.Preferences;

/**
 * {@code LegupPreferences} is a class responsible for managing user preferences within the
 * application. It uses Java's Preferences API to store and retrieve preferences, and it provides
 * methods for accessing and updating these preferences.
 */
public class LegupPreferences {

    private static LegupPreferences instance;

    private static String savedPath = "";

    private static final Preferences preferences =
            Preferences.userNodeForPackage(LegupPreferences.class);

    private static final Map<LegupPreference, Object> preferencesMap =
            new EnumMap<>(LegupPreference.class);

    public enum LegupPreference {
        WORK_DIRECTORY("work-directory", defaultUserDirectory(), o -> o),
        FULL_SCREEN("full-screen", false, Boolean::parseBoolean),
        AUTO_UPDATE("auto-update", true, Boolean::parseBoolean),
        DARK_MODE("night-mode", false, Boolean::parseBoolean),
        USE_CUSTOM_COLOR_THEME("use-custom-color-theme", false, Boolean::parseBoolean),
        SHOW_MISTAKES("show-mistakes", true, Boolean::parseBoolean),
        SHOW_ANNOTATIONS("show-annotations", true, Boolean::parseBoolean),
        ALLOW_DEFAULT_RULES("allow-default-rules", false, Boolean::parseBoolean),
        AUTO_GENERATE_CASES("auto-generate-cases", true, Boolean::parseBoolean),
        IMMEDIATE_FEEDBACK("immediate-feedback", true, Boolean::parseBoolean),
        COLOR_THEME_FILE("color-theme-file", defaultUserDirectory(), o -> o),
        COLOR_BLIND("color-blind", false, Boolean::parseBoolean);

        private final String id;
        private final Object defaultValue;

        /** Converts the object to a string so that it can be saved in preferences */
        private final Function<Object, String> stringMapper;

        /** Converts a string value to an object so it's more convenient to use in code */
        private final Function<String, Object> stringToValueMapper;

        @SuppressWarnings("unchecked")
        <T> LegupPreference(
                String id,
                T defaultValue,
                Function<T, String> stringMapper,
                Function<String, T> stringToValueMapper) {
            this.id = id;
            this.defaultValue = defaultValue;
            this.stringMapper = (Function<Object, String>) stringMapper;
            this.stringToValueMapper = (Function<String, Object>) stringToValueMapper;
        }

        <T> LegupPreference(
                String id, T defaultValue, Function<String, Object> stringToValueMapper) {
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
         *
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

    private static String defaultUserDirectory() {
        return Paths.get(System.getProperty("user.home")).toString();
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
     * Gets the legup preferences singleton instance This method ensures that only one instance of
     * LegupPreferences exists
     *
     * @return the singleton instance of LegupPreferences
     */
    public static LegupPreferences getInstance() {
        if (instance == null) {
            instance = new LegupPreferences();
        }
        return instance;
    }

    /**
     * Private constructor to prevent instantiation from outside the class Use {@link
     * #getInstance()} to access the singleton instance
     */
    private LegupPreferences() {}

    /**
     * Gets the user preference by the string key
     *
     * @param preference the preference to get
     * @return value of the preference or {@code null} if the preference does not exist
     */
    private Object getUserPref(LegupPreference preference) {
        return preferencesMap.get(preference);
    }

    /**
     * Sets the user preference for the specified key to the provided value
     *
     * @param preference the preference to change
     * @param value value to set for the preference
     */
    public void setUserPref(LegupPreference preference, Object value) {
        preferences.put(preference.id(), preference.convertToString(value));
        preferencesMap.put(preference, value);
    }

    /**
     * Retrieves the user preference associated with the specified key as a boolean
     *
     * @param preference the key for the preference to retrieve
     * @return the boolean value of the preference
     */
    public boolean getUserPrefAsBool(LegupPreference preference) {
        return preference.asBoolean("Cannot get user preference - " + preference.id());
    }

    /**
     * Gets the saved path
     *
     * @return the saved path as a String
     */
    public String getSavedPath() {
        return savedPath;
    }

    /**
     * Sets the saved path to the specified value
     *
     * @param path the new saved path
     */
    public void setSavedPath(String path) {
        savedPath = path;
    }

    /**
     * Gets the state of COLOR_BLIND
     *
     * @return the COLOR_BLIND state as a boolean
     */
    public static boolean colorBlind() {
        return LegupPreference.COLOR_BLIND.asBoolean();
    }

    /**
     * Gets the state of DARK_MODE
     *
     * @return the DARK_MODE state as a boolean
     */
    public static boolean darkMode() {
        return LegupPreference.DARK_MODE.asBoolean();
    }

    /**
     * Gets the state of SHOW_ANNOTATIONS
     *
     * @return the SHOW_ANNOTATIONS state as a boolean
     */
    public static boolean showAnnotations() {
        return LegupPreference.SHOW_ANNOTATIONS.asBoolean();
    }

    /**
     * Gets the state of ALLOW_DEFAULT_RULES
     *
     * @return the ALLOW_DEFAULT_RULES state as a boolean
     */
    public static boolean allowDefaultRules() {
        return LegupPreference.ALLOW_DEFAULT_RULES.asBoolean();
    }

    /**
     * Gets the state of WORK_DIRECTORY
     *
     * @return the WORK_DIRECTORY as a string
     */
    public static String workDirectory() {
        return LegupPreference.WORK_DIRECTORY.stringValue();
    }

    /**
     * Gets the state of FULL_SCREEN
     *
     * @return the FULL_SCREEN state as a boolean
     */
    public static boolean fullScreen() {return LegupPreference.FULL_SCREEN.asBoolean();}

    /**
     * Sets the screen to FullScreen or Windowed. Enters FullScreen if the screen is Windowed
     * and the caller was a click event OR if the screen is FullScreen and the
     * caller wasn't a click event
     *
     * @param isClick a boolean correlating to if the caller was an OnClick function
     */
    public static void setScreenSize(boolean isClick) {
        LegupUI ui = GameBoardFacade.getInstance().getLegupUI();
        if(ui == null) { return; }

        boolean full = ( isClick && !LegupPreferences.fullScreen() )
                || ( !isClick && LegupPreferences.fullScreen() );

        if( full ) {
            ui.setExtendedState(ui.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        } else {
            ui.setExtendedState(JFrame.NORMAL); }
    }

    /**
     * Gets the state of AUTO_UPDATE
     *
     * @return the AUTO_UPDATE state as a boolean
     */
    public static boolean autoUpdate() {
        return LegupPreference.AUTO_UPDATE.asBoolean();
    }

    /**
     * Gets the state of SHOW_MISTAKES
     *
     * @return the SHOW_MISTAKES state as a boolean
     */
    public static boolean showMistakes() {
        return LegupPreference.SHOW_MISTAKES.asBoolean();
    }

    /**
     * Gets the state of AUTO_GENERATE_CASES
     *
     * @return the AUTO_GENERATE_CASES state as a boolean
     */
    public static boolean autoGenerateCases() {
        return LegupPreference.AUTO_GENERATE_CASES.asBoolean();
    }

    /**
     * Gets the state of IMMEDIATE_FEEDBACK
     *
     * @return the IMMEDIATE_FEEDBACK state as a boolean
     */
    public static boolean immediateFeedback() {
        return LegupPreference.IMMEDIATE_FEEDBACK.asBoolean();
    }

    /**
     * Gets the state of USE_CUSTOM_COLOR_THEME
     *
     * @return the USE_CUSTOM_COLOR_THEME state as a boolean
     */
    public static boolean useCustomColorTheme() {
        return LegupPreference.USE_CUSTOM_COLOR_THEME.asBoolean();
    }

    /**
     * Gets the state of COLOR_THEME_FILE
     *
     * @return the COLOR_THEME_FILE as a string
     */
    public static String colorThemeFile() {
        return LegupPreference.COLOR_THEME_FILE.stringValue();
    }
}
