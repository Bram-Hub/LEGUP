package edu.rpi.legup.ui.svg;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * The {@code IconColorManager} class maintains a list of colors that can be used for dynamic
 * coloring of {@code SVGIcon} instances in accordance with an assignable palette.
 */
public final class IconColorManager {

    private static final IconColorManager INSTANCE = new IconColorManager();

    private String css;
    private final List<Runnable> listeners = new ArrayList<>();

    private IconColorManager() {}

    /**
     * Get the shared instance of {@code IconColorManager}.
     */
    public static IconColorManager getInstance() {
        return INSTANCE;
    }

    /**
     * Builds CSS variable definitions for provided color palette and injects into all
     * listening {@code SVGIcon} instances.
     *
     * @param palette Color palette to inject into icons.
     */
    public synchronized void setSharedPalette(Map<String, String> palette) {

        StringBuilder cssBuilder = new StringBuilder(":root {\n");
        palette.forEach((k, v) ->
                cssBuilder.append("  --").append(k).append(": ").append(v).append(";\n"));
        cssBuilder.append("}");

        css = cssBuilder.toString();
        listeners.forEach(Runnable::run);
    }

    /**
     * Gets the current state of the css variable definitions.
     */
    public synchronized String snapshot() {
        return css;
    }

    /**
     * Adds a listener which will be updated whenever the css variable definitions change.
     */
    public synchronized void addListener(Runnable r) {
        listeners.add(r);
    }
}