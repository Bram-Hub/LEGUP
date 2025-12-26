package edu.rpi.legup.app;

import java.io.InputStream;
import java.util.Properties;

public final class VersionInfo {

    // A private static final field to hold the version string
    private static final String VERSION = loadVersion();

    /**
     * Reads the version from the version.properties file.
     * @return The version string, or "UNKNOWN" if it cannot be found.
     */
    private static String loadVersion() {
        try (InputStream input = VersionInfo.class.getResourceAsStream("/version.properties")) {
            if (input == null) {
                System.err.println("Could not find version.properties file.");
                return "UNKNOWN";
            }
            Properties props = new Properties();
            props.load(input);
            return props.getProperty("app.version", "UNKNOWN");
        } catch (Exception e) {
            System.err.println("Error reading version.properties file: " + e.getMessage());
            return "UNKNOWN";
        }
    }

    /**
     * Gets the application version string.
     * @return The version string (e.g., "1.2.3").
     */
    public static String getVersion() {
        return VERSION;
    }

    // Private constructor to prevent instantiation
    private VersionInfo() {}
}
