package edu.rpi.legup.app;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class LegupPreferences {

    private static LegupPreferences instance;

    private static final Preferences preferences = Preferences.userNodeForPackage(LegupPreferences.class);

    private static final Map<String, String> preferencesMap = new HashMap<>();
    private static final Map<String, String> defaultPreferencesMap = new HashMap<>();

    public static final String WORK_DIRECTORY = "work-directory";
    public static final String START_FULL_SCREEN = "start-full-screen";
    public static final String AUTO_UPDATE = "auto-update";
    public static final String SHOW_MISTAKES = "show-mistakes";
    public static final String ALLOW_DEFAULT_RULES = "allow-default-rules";
    public static final String AUTO_GENERATE_CASES = "auto-generate-cases";
    public static final String IMMEDIATE_FEEDBACK = "immediate-feedback";

    static {
        defaultPreferencesMap.put(WORK_DIRECTORY, System.getProperty("user.home"));
        defaultPreferencesMap.put(START_FULL_SCREEN, Boolean.toString(false));
        defaultPreferencesMap.put(AUTO_UPDATE, Boolean.toString(true));
        defaultPreferencesMap.put(SHOW_MISTAKES, Boolean.toString(true));
        defaultPreferencesMap.put(ALLOW_DEFAULT_RULES, Boolean.toString(false));
        defaultPreferencesMap.put(AUTO_GENERATE_CASES, Boolean.toString(true));
        defaultPreferencesMap.put(IMMEDIATE_FEEDBACK, Boolean.toString(true));
    }

    static {
        preferencesMap.put(WORK_DIRECTORY, preferences.get(WORK_DIRECTORY, defaultPreferencesMap.get(WORK_DIRECTORY)));
        preferencesMap.put(START_FULL_SCREEN, preferences.get(START_FULL_SCREEN, defaultPreferencesMap.get(START_FULL_SCREEN)));
        preferencesMap.put(AUTO_UPDATE, preferences.get(AUTO_UPDATE, defaultPreferencesMap.get(AUTO_UPDATE)));
        preferencesMap.put(SHOW_MISTAKES, preferences.get(SHOW_MISTAKES, defaultPreferencesMap.get(SHOW_MISTAKES)));
        preferencesMap.put(ALLOW_DEFAULT_RULES, preferences.get(ALLOW_DEFAULT_RULES, defaultPreferencesMap.get(ALLOW_DEFAULT_RULES)));
        preferencesMap.put(AUTO_GENERATE_CASES, preferences.get(AUTO_GENERATE_CASES, defaultPreferencesMap.get(AUTO_GENERATE_CASES)));
        preferencesMap.put(IMMEDIATE_FEEDBACK, preferences.get(IMMEDIATE_FEEDBACK, defaultPreferencesMap.get(IMMEDIATE_FEEDBACK)));
    }

    public static LegupPreferences getInstance() {
        if(instance == null) {
            instance = new LegupPreferences();
        }
        return instance;
    }

    private LegupPreferences() {

    }

    public String getUserPref(String key) {
        return preferencesMap.get(key);
    }

    public String setUserPref(String key, String value) {
        preferences.put(key, value);
        return preferencesMap.put(key, value);
    }
}
