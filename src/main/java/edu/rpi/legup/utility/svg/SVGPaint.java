package edu.rpi.legup.utility.svg;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SVGPaint {

    private final static HashMap<String, Color> namedColors;
    static {
        namedColors = new HashMap<String, Color>();
        namedColors.put("aliceblue", new Color(240, 248, 255));
        namedColors.put("antiquewhite", new Color(250, 235, 215));
        namedColors.put("aqua", new Color( 0, 255, 255));
        namedColors.put("aquamarine", new Color(127, 255, 212));
        namedColors.put("azure", new Color(240, 255, 255));
        namedColors.put("beige", new Color(245, 245, 220));
        namedColors.put("bisque", new Color(255, 228, 196));
        namedColors.put("black", new Color( 0, 0, 0));
        namedColors.put("blanchedalmond", new Color(255, 235, 205));
        namedColors.put("blue", new Color( 0, 0, 255));
        namedColors.put("blueviolet", new Color(138, 43, 226));
        namedColors.put("brown", new Color(165, 42, 42));
        namedColors.put("burlywood", new Color(222, 184, 135));
        namedColors.put("cadetblue", new Color( 95, 158, 160));
        namedColors.put("chartreuse", new Color(127, 255, 0));
        namedColors.put("chocolate", new Color(210, 105, 30));
        namedColors.put("coral", new Color(255, 127, 80));
        namedColors.put("cornflowerblue", new Color(100, 149, 237));
        namedColors.put("cornsilk", new Color(255, 248, 220));
        namedColors.put("crimson", new Color(220, 20, 60));
        namedColors.put("cyan", new Color( 0, 255, 255));
        namedColors.put("darkblue", new Color( 0, 0, 139));
        namedColors.put("darkcyan", new Color( 0, 139, 139));
        namedColors.put("darkgoldenrod", new Color(184, 134, 11));
        namedColors.put("darkgray", new Color(169, 169, 169));
        namedColors.put("darkgreen", new Color( 0, 100, 0));
        namedColors.put("darkgrey", new Color(169, 169, 169));
        namedColors.put("darkkhaki", new Color(189, 183, 107));
        namedColors.put("darkmagenta", new Color(139, 0, 139));
        namedColors.put("darkolivegreen", new Color( 85, 107, 47));
        namedColors.put("darkorange", new Color(255, 140, 0));
        namedColors.put("darkorchid", new Color(153, 50, 204));
        namedColors.put("darkred", new Color(139, 0, 0));
        namedColors.put("darksalmon", new Color(233, 150, 122));
        namedColors.put("darkseagreen", new Color(143, 188, 143));
        namedColors.put("darkslateblue", new Color( 72, 61, 139));
        namedColors.put("darkslategray", new Color( 47, 79, 79));
        namedColors.put("darkslategrey", new Color( 47, 79, 79));
        namedColors.put("darkturquoise", new Color( 0, 206, 209));
        namedColors.put("darkviolet", new Color(148, 0, 211));
        namedColors.put("deeppink", new Color(255, 20, 147));
        namedColors.put("deepskyblue", new Color( 0, 191, 255));
        namedColors.put("dimgray", new Color(105, 105, 105));
        namedColors.put("dimgrey", new Color(105, 105, 105));
        namedColors.put("dodgerblue", new Color( 30, 144, 255));
        namedColors.put("firebrick", new Color(178, 34, 34));
        namedColors.put("floralwhite", new Color(255, 250, 240));
        namedColors.put("forestgreen", new Color( 34, 139, 34));
        namedColors.put("fuchsia", new Color(255, 0, 255));
        namedColors.put("gainsboro", new Color(220, 220, 220));
        namedColors.put("ghostwhite", new Color(248, 248, 255));
        namedColors.put("gold", new Color(255, 215, 0));
        namedColors.put("goldenrod", new Color(218, 165, 32));
        namedColors.put("gray", new Color(128, 128, 128));
        namedColors.put("grey", new Color(128, 128, 128));
        namedColors.put("green", new Color( 0, 128, 0));
        namedColors.put("greenyellow", new Color(173, 255, 47));
        namedColors.put("honeydew", new Color(240, 255, 240));
        namedColors.put("hotpink", new Color(255, 105, 180));
        namedColors.put("indianred", new Color(205, 92, 92));
        namedColors.put("indigo", new Color( 75, 0, 130));
        namedColors.put("ivory", new Color(255, 255, 240));
        namedColors.put("khaki", new Color(240, 230, 140));
        namedColors.put("lavender", new Color(230, 230, 250));
        namedColors.put("lavenderblush", new Color(255, 240, 245));
        namedColors.put("lawngreen", new Color(124, 252, 0));
        namedColors.put("lemonchiffon", new Color(255, 250, 205));
        namedColors.put("lightblue", new Color(173, 216, 230));
        namedColors.put("lightcoral", new Color(240, 128, 128));
        namedColors.put("lightcyan", new Color(224, 255, 255));
        namedColors.put("lightgoldenrodyellow", new Color(250, 250, 210));
        namedColors.put("lightgray", new Color(211, 211, 211));
        namedColors.put("lightgreen", new Color(144, 238, 144));
        namedColors.put("lightgrey", new Color(211, 211, 211));
        namedColors.put("lightpink", new Color(255, 182, 193));
        namedColors.put("lightsalmon", new Color(255, 160, 122));
        namedColors.put("lightseagreen", new Color( 32, 178, 170));
        namedColors.put("lightskyblue", new Color(135, 206, 250));
        namedColors.put("lightslategray", new Color(119, 136, 153));
        namedColors.put("lightslategrey", new Color(119, 136, 153));
        namedColors.put("lightsteelblue", new Color(176, 196, 222));
        namedColors.put("lightyellow", new Color(255, 255, 224));
        namedColors.put("lime", new Color( 0, 255, 0));
        namedColors.put("limegreen", new Color( 50, 205, 50));
        namedColors.put("linen", new Color(250, 240, 230));
        namedColors.put("magenta", new Color(255, 0, 255));
        namedColors.put("maroon", new Color(128, 0, 0));
        namedColors.put("mediumaquamarine", new Color(102, 205, 170));
        namedColors.put("mediumblue", new Color( 0, 0, 205));
        namedColors.put("mediumorchid", new Color(186, 85, 211));
        namedColors.put("mediumpurple", new Color(147, 112, 219));
        namedColors.put("mediumseagreen", new Color( 60, 179, 113));
        namedColors.put("mediumslateblue", new Color(123, 104, 238));
        namedColors.put("mediumspringgreen", new Color( 0, 250, 154));
        namedColors.put("mediumturquoise", new Color( 72, 209, 204));
        namedColors.put("mediumvioletred", new Color(199, 21, 133));
        namedColors.put("midnightblue", new Color( 25, 25, 112));
        namedColors.put("mintcream", new Color(245, 255, 250));
        namedColors.put("mistyrose", new Color(255, 228, 225));
        namedColors.put("moccasin", new Color(255, 228, 181));
        namedColors.put("navajowhite", new Color(255, 222, 173));
        namedColors.put("navy", new Color( 0, 0, 128));
        namedColors.put("oldlace", new Color(253, 245, 230));
        namedColors.put("olive", new Color(128, 128, 0));
        namedColors.put("olivedrab", new Color(107, 142, 35));
        namedColors.put("orange", new Color(255, 165, 0));
        namedColors.put("orangered", new Color(255, 69, 0));
        namedColors.put("orchid", new Color(218, 112, 214));
        namedColors.put("palegoldenrod", new Color(238, 232, 170));
        namedColors.put("palegreen", new Color(152, 251, 152));
        namedColors.put("paleturquoise", new Color(175, 238, 238));
        namedColors.put("palevioletred", new Color(219, 112, 147));
        namedColors.put("papayawhip", new Color(255, 239, 213));
        namedColors.put("peachpuff", new Color(255, 218, 185));
        namedColors.put("peru", new Color(205, 133, 63));
        namedColors.put("pink", new Color(255, 192, 203));
        namedColors.put("plum", new Color(221, 160, 221));
        namedColors.put("powderblue", new Color(176, 224, 230));
        namedColors.put("purple", new Color(128, 0, 128));
        namedColors.put("red", new Color(255, 0, 0));
        namedColors.put("rosybrown", new Color(188, 143, 143));
        namedColors.put("royalblue", new Color( 65, 105, 225));
        namedColors.put("saddlebrown", new Color(139, 69, 19));
        namedColors.put("salmon", new Color(250, 128, 114));
        namedColors.put("sandybrown", new Color(244, 164, 96));
        namedColors.put("seagreen", new Color( 46, 139, 87));
        namedColors.put("seashell", new Color(255, 245, 238));
        namedColors.put("sienna", new Color(160, 82, 45));
        namedColors.put("silver", new Color(192, 192, 192));
        namedColors.put("skyblue", new Color(135, 206, 235));
        namedColors.put("slateblue", new Color(106, 90, 205));
        namedColors.put("slategray", new Color(112, 128, 144));
        namedColors.put("slategrey", new Color(112, 128, 144));
        namedColors.put("snow", new Color(255, 250, 250));
        namedColors.put("springgreen", new Color( 0, 255, 127));
        namedColors.put("steelblue", new Color( 70, 130, 180));
        namedColors.put("tan", new Color(210, 180, 140));
        namedColors.put("teal", new Color( 0, 128, 128));
        namedColors.put("thistle", new Color(216, 191, 216));
        namedColors.put("tomato", new Color(255, 99, 71));
        namedColors.put("turquoise", new Color( 64, 224, 208));
        namedColors.put("violet", new Color(238, 130, 238));
        namedColors.put("wheat", new Color(245, 222, 179));
        namedColors.put("white", new Color(255, 255, 255));
        namedColors.put("whitesmoke", new Color(245, 245, 245));
        namedColors.put("yellow", new Color(255, 255, 0));
        namedColors.put("yellowgreen", new Color(154, 205, 50));
    }

    /**
     * Gets the Color object associated with a given case-insensitive name according to the
     * <a href="https://www.w3.org/TR/2000/CR-SVG-20001102/types.html#ColorKeywords">
     *     W3C SVG specification.</a>
     *
     * @param name Name of the color.
     * @return Color value associated with given name.
     */
    public static Color getNamedColor(String name) {
        return namedColors.get(name.toLowerCase());
    }

    /**
     * Get a Paint object corresponding to the input string.
     *
     * @param paintStr Color or gradient description.
     */
    public static Paint getPaint(String paintStr) {
        if (paintStr.equals("none")) {
            return new Color(0, 0, 0, 0);
        }
        if (Pattern.compile("^#(?:[0-9a-fA-F]{3,4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$")
                .matcher(paintStr).find()) {
            return parseHex(paintStr);
        }
        // Regex snippets
        String regexInt = "[+-]?\\d+";
        String regexFloat = "[+-]?(?:\\d+\\.\\d+|\\d+|\\.\\d+)";
        String regexPcnt = regexFloat + "%";
        if (Pattern.compile("^rgb\\(\\s*(" + regexInt + "|" + regexPcnt + ")\\s*[, ]\\s*("
                        + regexInt + "|" + regexPcnt + ")\\s*[, ]\\s*(" + regexInt + "|" + regexPcnt
                        + ")\\s*\\)$", Pattern.CASE_INSENSITIVE).matcher(paintStr).find()) {
            return null;
        }
        return namedColors.get(paintStr);
    }

    /**
     * Helper function to parse a hex color code.
     *
     * @param toParse Hex color code to parse.
     */
    private static Color parseHex(String toParse) {
        int r, g, b, a = 255;
        if (toParse.length() < 6) {
            r = 16 * Integer.parseInt(toParse.substring(1, 2), 16);
            g = 16 * Integer.parseInt(toParse.substring(2, 3), 16);
            b = 16 * Integer.parseInt(toParse.substring(3, 4), 16);
            if (toParse.length() == 5) {
                a = 16 * Integer.parseInt(toParse.substring(4), 16);
            }
        }
        else {
            r = Integer.parseInt(toParse.substring(1, 3), 16);
            g = Integer.parseInt(toParse.substring(3, 5), 16);
            b = Integer.parseInt(toParse.substring(5, 7), 16);
            if (toParse.length() == 9) {
                a = Integer.parseInt(toParse.substring(7), 16);
            }
        }
        return new Color(r, g, b, a);
    }
}