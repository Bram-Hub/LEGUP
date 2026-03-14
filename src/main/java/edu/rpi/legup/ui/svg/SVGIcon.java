package edu.rpi.legup.ui.svg;

import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import javax.swing.Icon;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The {@code SVGIcon} class allows SVG files to be loaded and displayed as a Java Swing Icon.
 * It also allows these icons to be colored dynamically by creating css variables with
 * definitions provided by the {@code IconColorManager} instance.
 */
public class SVGIcon implements Icon {

    private Element styleElement;
    private GraphicsNode graphicsNode;

    private final int iconWidth;
    private final int iconHeight;
    private final Rectangle2D origBounds;

    public SVGIcon(URL svgUrl, int width, int height, boolean dynamic) {

        iconWidth = width;
        iconHeight = height;

        // Load SVG
        Document doc;
        try {
            doc = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName())
                    .createDocument(svgUrl.toString());

        } catch(IOException e) {

            System.out.println(e);
            origBounds = new Rectangle2D.Float(0,0,0,0);

            return;
        }

        if (dynamic) {
            styleElement = injectStyleElement(doc);
        }

        BridgeContext bridgeContext = new BridgeContext(new UserAgentAdapter());
        bridgeContext.setDynamicState(BridgeContext.DYNAMIC);
        graphicsNode = new GVTBuilder().build(bridgeContext, doc);

        // Get details about size of original SVG
        Element docElem = doc.getDocumentElement();
        if (docElem.hasAttribute("viewBox")) {

            String[] vals = docElem.getAttribute("viewBox").trim().split("[ ,]+");

            if (vals.length == 4) {
                origBounds = new Rectangle2D.Double(
                        Double.parseDouble(vals[0]), Double.parseDouble(vals[1]),
                        Double.parseDouble(vals[2]), Double.parseDouble(vals[3]));
            }
            else if (docElem.hasAttribute("width") && docElem.hasAttribute("height")) {
                origBounds = new Rectangle2D.Double(0, 0,
                        Integer.parseInt(docElem.getAttribute("width")),
                        Integer.parseInt(docElem.getAttribute("height")));
            }
            else {
                origBounds = graphicsNode.getPrimitiveBounds();
            }
        }
        else if (docElem.hasAttribute("width") && docElem.hasAttribute("height")) {
            origBounds = new Rectangle2D.Double(0, 0,
                    Integer.parseInt(docElem.getAttribute("width")),
                    Integer.parseInt(docElem.getAttribute("height")));
        }
        else {
            origBounds = graphicsNode.getPrimitiveBounds();
        }

        // Setup dynamic coloring
        if (dynamic) {
            IconColorManager.getInstance().addListener(this::updateCSSVariables);
            updateCSSVariables();
        }
    }

    /**
     * Creates a new style element at the top of the document.
     *
     * @param doc The document to inject an element into.
     * @return The newly created and injected style element.
     */
    private Element injectStyleElement(Document doc) {

        Element svg = doc.getDocumentElement();
        Element style = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, "style");
        style.setAttribute("type", "text/css");

        svg.insertBefore(style, svg.getFirstChild());
        return style;
    }

    /**
     * Updates the injected style element's contents with the current definitions
     * in the IconColorManager instance.
     */
    private void updateCSSVariables() {
        styleElement.setTextContent(IconColorManager.getInstance().snapshot());
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply transformations to scale and center SVG within icon
        double s = Math.min(iconWidth / origBounds.getWidth(), iconHeight / origBounds.getHeight());
        double transX = x + (iconWidth - s * origBounds.getWidth()) / 2;
        double transY = y + (iconHeight - s * origBounds.getHeight()) / 2;
        g2.translate(transX, transY);
        g2.scale(s, s);

        graphicsNode.paint(g2);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return iconWidth;
    }

    @Override
    public int getIconHeight() {
        return iconHeight;
    }
}
