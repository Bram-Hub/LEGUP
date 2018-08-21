package edu.rpi.legup.app;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class LegupPreferences {

    private static LegupPreferences instance;

    private Preferences preferences;

    private Map<String, String> keyMap;

    public LegupPreferences getInstance() {
        if(instance == null) {
            instance = new LegupPreferences();
        }
        return instance;
    }

    private LegupPreferences() {
        preferences = Preferences.userNodeForPackage(this.getClass());
        keyMap = new HashMap<>();
    }
}
