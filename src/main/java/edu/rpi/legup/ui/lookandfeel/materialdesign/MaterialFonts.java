package edu.rpi.legup.ui.lookandfeel.materialdesign;


import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MaterialFonts {

    private static final Map<TextAttribute, Object> fontSettings = new HashMap<>();

    public static final Font BLACK = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Black.ttf");
    public static final Font BLACK_ITALIC = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-BlackItalic.ttf");
    public static final Font BOLD = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Bold.ttf");
    public static final Font BOLD_ITALIC = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-BoldItalic.ttf");
    public static final Font ITALIC = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Italic.ttf");
    public static final Font LIGHT = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Light.ttf");
    public static final Font LIGHT_ITALIC = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-LightItalic.ttf");
    public static final Font MEDIUM = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Medium.ttf");
    public static final Font MEDIUM_ITALIC = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-MediumItalic.ttf");
    public static final Font REGULAR = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Regular.ttf");
    public static final Font THIN = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-Thin.ttf");
    public static final Font THIN_ITALIC = loadFont("/edu/rpi/legup/fonts/Roboto/Roboto-ThinItalic.ttf");

    private static Font loadFont(String fontPath) {
        if (fontSettings.isEmpty()) {
            fontSettings.put(TextAttribute.SIZE, 14f);
            fontSettings.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        }

        try (InputStream inputStream = MaterialFonts.class.getResourceAsStream(fontPath)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(fontSettings);
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Font " + fontPath + " wasn't loaded");
        }
    }

    public static Font getRegularFont(float size) {
        final Map<TextAttribute, Object> map = new HashMap<>();
        map.put(TextAttribute.SIZE, size);
        map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);

        try (InputStream inputStream = MaterialFonts.class.getResourceAsStream("/edu/rpi/legup/fonts/Roboto/Roboto-Regular.ttf")) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(map);
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Font regular wasn't loaded");
        }
    }
}
