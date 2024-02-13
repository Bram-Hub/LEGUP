package edu.rpi.legup.app;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class LegupPreferences {

  private static LegupPreferences instance;

  private static String SAVED_PATH = "";

  private static final Preferences preferences =
      Preferences.userNodeForPackage(LegupPreferences.class);

  private static final Map<String, String> preferencesMap = new HashMap<>();
  private static final Map<String, String> defaultPreferencesMap = new HashMap<>();

  public static final String WORK_DIRECTORY = "work-directory";
  public static final String START_FULL_SCREEN = "start-full-screen";
  public static final String AUTO_UPDATE = "auto-update";
  public static final String DARK_MODE = "night-mode";
  public static final String SHOW_MISTAKES = "show-mistakes";
  public static final String SHOW_ANNOTATIONS = "show-annotations";
  public static final String ALLOW_DEFAULT_RULES = "allow-default-rules";
  public static final String AUTO_GENERATE_CASES = "auto-generate-cases";
  public static final String IMMEDIATE_FEEDBACK = "immediate-feedback";
  public static final String COLOR_BLIND = "color-blind";

  static {
    defaultPreferencesMap.put(WORK_DIRECTORY, System.getProperty("user.home"));
    defaultPreferencesMap.put(START_FULL_SCREEN, Boolean.toString(false));
    defaultPreferencesMap.put(AUTO_UPDATE, Boolean.toString(true));
    defaultPreferencesMap.put(DARK_MODE, Boolean.toString(false));
    defaultPreferencesMap.put(SHOW_MISTAKES, Boolean.toString(true));
    defaultPreferencesMap.put(SHOW_ANNOTATIONS, Boolean.toString(false));
    defaultPreferencesMap.put(ALLOW_DEFAULT_RULES, Boolean.toString(false));
    defaultPreferencesMap.put(AUTO_GENERATE_CASES, Boolean.toString(true));
    defaultPreferencesMap.put(IMMEDIATE_FEEDBACK, Boolean.toString(true));
    defaultPreferencesMap.put(COLOR_BLIND, Boolean.toString(false));
  }

  static {
    preferencesMap.put(
        WORK_DIRECTORY, preferences.get(WORK_DIRECTORY, defaultPreferencesMap.get(WORK_DIRECTORY)));
    preferencesMap.put(
        START_FULL_SCREEN,
        preferences.get(START_FULL_SCREEN, defaultPreferencesMap.get(START_FULL_SCREEN)));
    preferencesMap.put(
        AUTO_UPDATE, preferences.get(AUTO_UPDATE, defaultPreferencesMap.get(AUTO_UPDATE)));
    preferencesMap.put(DARK_MODE, preferences.get(DARK_MODE, defaultPreferencesMap.get(DARK_MODE)));
    preferencesMap.put(
        SHOW_MISTAKES, preferences.get(SHOW_MISTAKES, defaultPreferencesMap.get(SHOW_MISTAKES)));
    preferencesMap.put(
        SHOW_ANNOTATIONS,
        preferences.get(SHOW_ANNOTATIONS, defaultPreferencesMap.get(SHOW_ANNOTATIONS)));
    preferencesMap.put(
        ALLOW_DEFAULT_RULES,
        preferences.get(ALLOW_DEFAULT_RULES, defaultPreferencesMap.get(ALLOW_DEFAULT_RULES)));
    preferencesMap.put(
        AUTO_GENERATE_CASES,
        preferences.get(AUTO_GENERATE_CASES, defaultPreferencesMap.get(AUTO_GENERATE_CASES)));
    preferencesMap.put(
        IMMEDIATE_FEEDBACK,
        preferences.get(IMMEDIATE_FEEDBACK, defaultPreferencesMap.get(IMMEDIATE_FEEDBACK)));
    preferencesMap.put(
        COLOR_BLIND, preferences.get(COLOR_BLIND, defaultPreferencesMap.get(COLOR_BLIND)));
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

  /** Private LegupPreferences Singleton Constructor */
  private LegupPreferences() {}

  /**
   * Gets the user preference by the string key
   *
   * @param key key name of the preference
   * @return value of the preference
   */
  public String getUserPref(String key) {
    return preferencesMap.get(key);
  }

  /**
   * Gets the user preference by the string key, value pair
   *
   * @param key key name of the preference
   * @param value value of the preference
   */
  public void setUserPref(String key, String value) {
    preferences.put(key, value);
    preferencesMap.put(key, value);
  }

  public boolean getUserPrefAsBool(String key) {
    if (preferencesMap.get(key).equalsIgnoreCase(Boolean.toString(true))) {
      return true;
    } else {
      if (preferencesMap.get(key).equalsIgnoreCase(Boolean.toString(false))) {
        return false;
      } else {
        throw new RuntimeException("Cannot get user preference - " + key);
      }
    }
  }

  public String getSavedPath() {
    return SAVED_PATH;
  }

  public void setSavedPath(String path) {
    SAVED_PATH = path;
  }
}
