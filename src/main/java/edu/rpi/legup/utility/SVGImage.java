package edu.rpi.legup.utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import javax.swing.Icon;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SVGImage {
    Drawable[] items;
    float x;
    float y;
    float width;
    float height;
    interface Processor {
        Shape process(Element node);
    }
    static Map<String, Color> colors = Map.of(
        "white", Color.WHITE,
        "black", Color.BLACK,
        "red", Color.RED,
        "green", Color.GREEN,
        "blue", Color.BLUE,
        "cyan", Color.CYAN,
        "magenta", Color.MAGENTA,
        "yellow", Color.YELLOW
    );
    record ColoredStroke(Stroke stroke, Color color) {}
    record Drawable(Optional<Color> fill, Optional<ColoredStroke> stroke, Area shape) {}
    record ElementProcessor(String element, Processor processor) implements Map.Entry<String, ElementProcessor> {
        Drawable process(Element node) {
            Color strokeColor = null;
            if(node.hasAttribute("stroke")) {
                strokeColor = colors.get(node.getAttribute("stroke").toLowerCase());
            }
            Float strokeWidth = null;
            if(node.hasAttribute("stroke-width")) {
                strokeWidth = Float.valueOf(node.getAttribute("stroke-width"));
            }
            ColoredStroke stroke = null;
            if(strokeWidth != null && strokeColor != null) {
                stroke = new ColoredStroke(new BasicStroke(strokeWidth), strokeColor);
            }
            Color fill = null;
            if(node.hasAttribute("fill")) {
                fill = colors.get(node.getAttribute("fill").toLowerCase());
            }
            Area area = new Area(processor.process(node));
            return new Drawable(Optional.ofNullable(fill), Optional.ofNullable(stroke), area);
        }
        @Override
        public String getKey() {
            return element;
        }
        @Override
        public ElementProcessor getValue() {
            return this;
        }
        @Override
        public ElementProcessor setValue(ElementProcessor value) {
            return this;
        }
    }
    static Map<String, ElementProcessor> processors = Map.ofEntries(
        new ElementProcessor("rect", (Element node) -> {
            float radiusX = 0;
            if(node.hasAttribute("rx")) {
                radiusX = Float.parseFloat(node.getAttribute("rx"));
            }
            float radiusY = 0;
            if(node.hasAttribute("ry")) {
                radiusX = Float.parseFloat(node.getAttribute("ry"));
            }
            float x = Float.parseFloat(node.getAttribute("x"));
            float y = Float.parseFloat(node.getAttribute("y"));
            float width = Float.parseFloat(node.getAttribute("width"));
            float height = Float.parseFloat(node.getAttribute("height"));
            return new RoundRectangle2D.Float(x, y, width, height, radiusX, radiusY);
        }),
        new ElementProcessor("path", (Element node) -> {
            Path2D path = new Path2D.Float();
            Scanner scanner = new Scanner(node.getAttribute("d"));
            while(scanner.hasNext()) {
                switch(scanner.next()) {
                    case "M" -> {
                        path.moveTo(scanner.nextFloat(), scanner.nextFloat());
                    }
                    case "Q" -> {
                        path.quadTo(scanner.nextFloat(), scanner.nextFloat(),
                            scanner.nextFloat(), scanner.nextFloat());
                    }
                    case "L" -> {
                        path.lineTo(scanner.nextFloat(), scanner.nextFloat());
                    }
                    case "V" -> {
                        path.lineTo(path.getCurrentPoint().getX(), scanner.nextFloat());
                    }
                    case "H" -> {
                        path.lineTo(scanner.nextFloat(), path.getCurrentPoint().getY());
                    }
                    case "Z" -> {
                        path.closePath();
                    }
                }
            }
            return path;
        })
    );
    public SVGImage(URL url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url.toString());
            String viewBox = doc.getDocumentElement().getAttribute("viewBox");
            Scanner scanner = new Scanner(viewBox);
            x = scanner.nextFloat();
            y = scanner.nextFloat();
            width = scanner.nextFloat();
            height = scanner.nextFloat();
            NodeList nodes = doc.getElementsByTagName("*");
            items = new Drawable[nodes.getLength()];
            for(int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);
                ElementProcessor processor = processors.get(node.getNodeName());
                if(processor != null) {
                    items[i] = processor.process(node);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e);
        }
    }
    public Icon getIcon(int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics graphics, int x, int y) {
                Graphics2D g = (Graphics2D) graphics;
                g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                for(Drawable item : items) {
                    if(item == null) {
                        continue;
                    }
                    Area shape = (Area) item.shape().clone();
                    float scaleX = width / SVGImage.this.width;
                    float scaleY = height / SVGImage.this.height;
                    AffineTransform transform = AffineTransform.getScaleInstance(scaleX, scaleY);
                    float shiftX = x - SVGImage.this.x;
                    float shiftY = y - SVGImage.this.y;
                    transform.translate(shiftX, shiftY);
                    shape.transform(transform);
                    item.fill.ifPresent((Color fill) -> {
                        g.setColor(fill);
                        g.fill(shape);
                    });
                    item.stroke.ifPresent((ColoredStroke stroke) -> {
                        g.setColor(stroke.color());
                        g.setStroke(stroke.stroke());
                        g.draw(shape);
                    });
                }
            }
            @Override
            public int getIconWidth() {
                return width;
            }
            @Override
            public int getIconHeight() {
                return height;
            }
        };
    }
}