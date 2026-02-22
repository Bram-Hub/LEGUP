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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Arc2D;
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
    record ColoredStroke(Stroke stroke, Color color) {}
    record Drawable(Optional<Color> fill, Optional<ColoredStroke> stroke, Area shape) {}
    record ElementProcessor(String element, Processor processor) implements Map.Entry<String, ElementProcessor> {
        Drawable process(Element node) {
            Color strokeColor = null;
            if (node.hasAttribute("stroke")) {
                strokeColor = SVGNamedColors.getColor(node.getAttribute("stroke"));
            }
            float strokeWidth = 1;
            if (node.hasAttribute("stroke-width")) {
                strokeWidth = Float.parseFloat(node.getAttribute("stroke-width"));
            }
            ColoredStroke stroke = null;
            if (strokeColor != null) {
                stroke = new ColoredStroke(new BasicStroke(strokeWidth), strokeColor);
            }
            Color fill = null;
            if (node.hasAttribute("fill")) {
                fill = SVGNamedColors.getColor(node.getAttribute("fill"));
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

    // Map tag name to processor that creates a drawable object from the tag's
    // attribute data. The implementations for each tag's processor are defined within.
    static Map<String, ElementProcessor> processors = Map.ofEntries(
        new ElementProcessor("rect", (Element node) -> {
            float radiusX = 0;
            if (node.hasAttribute("rx")) {
                radiusX = Float.parseFloat(node.getAttribute("rx"));
            }
            float radiusY = 0;
            if (node.hasAttribute("ry")) {
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
            String lastOp = "";
            double[] lastCtrlPt = new double[2];
            while (scanner.hasNext()) {
                String op = scanner.next();
                switch (op) {
                    case "M" -> {
                        path.moveTo(scanner.nextFloat(), scanner.nextFloat());
                    }
                    case "m" -> {
                        Point2D current = path.getCurrentPoint();
                        double x = scanner.nextFloat() + current.getX();
                        double y = scanner.nextFloat() + current.getY();
                        path.moveTo(x, y);
                    }
                    case "Q", "q" -> {
                        double x1 = scanner.nextFloat();
                        double y1 = scanner.nextFloat();
                        double x2 = scanner.nextFloat();
                        double y2 = scanner.nextFloat();
                        if (op.equals("q")) {
                            Point2D current = path.getCurrentPoint();
                            x1 += current.getX();
                            y1 += current.getY();
                            x2 += current.getX();
                            y2 += current.getY();
                        }
                        path.quadTo(x1, y1, x2, y2);
                        lastCtrlPt[0] = x1;
                        lastCtrlPt[1] = y1;
                    }
                    case "C", "c" -> {
                        double x1 = scanner.nextFloat();
                        double y1 = scanner.nextFloat();
                        double x2 = scanner.nextFloat();
                        double y2 = scanner.nextFloat();
                        double x3 = scanner.nextFloat();
                        double y3 = scanner.nextFloat();
                        if (op.equals("c")) {
                            Point2D current = path.getCurrentPoint();
                            x1 += current.getX();
                            y1 += current.getY();
                            x2 += current.getX();
                            y2 += current.getY();
                            x3 += current.getX();
                            y3 += current.getY();
                        }
                        path.curveTo(x1, y1, x2, y2, x3, y3);
                        lastCtrlPt[0] = x2;
                        lastCtrlPt[1] = y2;
                    }
                    case "T", "t" -> {
                        Point2D current = path.getCurrentPoint();
                        double x1 = current.getX();
                        double y1 = current.getY();
                        if ("QqTt".contains(lastOp)) {
                            x1 = 2 * x1 - lastCtrlPt[0];
                            y1 = 2 * y1 - lastCtrlPt[1];
                        }
                        double x2 = scanner.nextFloat();
                        double y2 = scanner.nextFloat();
                        if (op.equals("t")) {
                            x2 += current.getX();
                            y2 += current.getY();
                        }
                        path.quadTo(x1, y1, x2, y2);
                        lastCtrlPt[0] = x1;
                        lastCtrlPt[1] = y1;
                    }
                    case "S", "s" -> {
                        Point2D current = path.getCurrentPoint();
                        double x1 = current.getX();
                        double y1 = current.getY();
                        if ("CcSs".contains(lastOp)) {
                            x1 = 2 * x1 - lastCtrlPt[0];
                            y1 = 2 * y1 - lastCtrlPt[1];
                        }
                        double x2 = scanner.nextFloat();
                        double y2 = scanner.nextFloat();
                        double x3 = scanner.nextFloat();
                        double y3 = scanner.nextFloat();
                        if (op.equals("s")) {
                            x2 += current.getX();
                            y2 += current.getY();
                            x3 += current.getX();
                            y3 += current.getY();
                        }
                        path.curveTo(x1, y1, x2, y2, x3, y3);
                        lastCtrlPt[0] = x2;
                        lastCtrlPt[1] = y2;
                    }
                    case "L" -> {
                        path.lineTo(scanner.nextFloat(), scanner.nextFloat());
                    }
                    case "l" -> {
                        Point2D current = path.getCurrentPoint();
                        double x = scanner.nextFloat() + current.getX();
                        double y = scanner.nextFloat() + current.getY();
                        path.lineTo(x, y);
                    }
                    case "V" -> {
                        path.lineTo(path.getCurrentPoint().getX(), scanner.nextFloat());
                    }
                    case "v" -> {
                        Point2D current = path.getCurrentPoint();
                        path.lineTo(current.getX(), scanner.nextFloat() + current.getY());
                    }
                    case "H" -> {
                        path.lineTo(scanner.nextFloat(), path.getCurrentPoint().getY());
                    }
                    case "h" -> {
                        Point2D current = path.getCurrentPoint();
                        path.lineTo(scanner.nextFloat() + current.getX(), current.getY());
                    }
                    case "A", "a" -> {
                        Point2D current = path.getCurrentPoint();
                        double radiusX = Math.abs(scanner.nextFloat());
                        double radiusY = Math.abs(scanner.nextFloat());
                        double rotation = Math.toRadians(scanner.nextFloat() % 360);
                        boolean largeArcFlag = scanner.nextInt() == 1;
                        boolean sweepFlag = scanner.nextInt() == 1;
                        double x = scanner.nextFloat();
                        double y = scanner.nextFloat();
                        if (op.equals("a")) {
                            x += current.getX();
                            y += current.getY();
                        }
                        if (radiusX == 0 || radiusY == 0 ||
                                (current.getX() == x && current.getY() == y)) {
                            return null;
                        }
                        rotation = Math.toRadians(rotation % 360);
                        radiusX = Math.abs(radiusX);
                        radiusY = Math.abs(radiusY);
                        double c = Math.cos(rotation), s = Math.sin(rotation);
                        double dx = (current.getX() - x) / 2.0, dy = (current.getY() - y) / 2.0;
                        double xp = c * dx + s * dy, yp = -s * dx + c * dy; // Ellipse coord space
                        // Correct radii
                        double rx2 = radiusX * radiusX, ry2 = radiusY * radiusY;
                        double xp2 = xp * xp, yp2 = yp * yp;
                        double lambda = xp2 / rx2 + yp2 / ry2;
                        if (lambda > 1) {
                            double scale = Math.sqrt(lambda);
                            radiusX *= scale;
                            radiusY *= scale;
                            rx2 = radiusX * radiusX;
                            ry2 = radiusY * radiusY;
                        }
                        // Find center of ellipse
                        double sign = (largeArcFlag == sweepFlag) ? -1 : 1;
                        double coeff = sign * Math.sqrt(Math.max(0,
                                (rx2 * ry2 - rx2 * yp2 - ry2 * xp2) / (rx2 * yp2 + ry2 * xp2)));
                        double cxp = coeff * (radiusX * yp / radiusY);
                        double cyp = coeff * (-radiusY * xp / radiusX);
                        double cx = c * cxp - s * cyp + (current.getX() + x) / 2.0;
                        double cy = s * cxp + c * cyp + (current.getY() + y) / 2.0;
                        // Find arc angles
                        double start = angleHelper(1, 0,
                                (xp - cxp) / radiusX, (yp - cyp) / radiusY);
                        double sweep = angleHelper((xp - cxp) / radiusX, (yp - cyp) / radiusY,
                                (-xp - cxp) / radiusX, (-yp - cyp) / radiusY);
                        if (!sweepFlag && sweep < 0) {
                            sweep += 360;
                        } else if (sweepFlag && sweep > 0) {
                            sweep -= 360;
                        }
                        path.append(AffineTransform.getRotateInstance(rotation, cx, cy)
                                .createTransformedShape(new Arc2D.Double(cx - radiusX,
                                        cy - radiusY, radiusX * 2, radiusY * 2,
                                        start, sweep, Arc2D.OPEN)), true);
                    }
                    case "Z", "z" -> {
                        path.closePath();
                    }
                    default -> {
                        System.err.println("Operation not supported");
                    }
                }
                lastOp = op;
            }
            return path;
        }),
        new ElementProcessor("polyline", (Element node) -> {
            Path2D path = new Path2D.Float();
            Scanner scanner = new Scanner(node.getAttribute("points"));
            path.moveTo(scanner.nextFloat(), scanner.nextFloat());
            while (scanner.hasNext()) {
                path.lineTo(scanner.nextFloat(), scanner.nextFloat());
            }
            return path;
        }),
        new ElementProcessor("polygon", (Element node) -> {
            Path2D path = new Path2D.Float();
            Scanner scanner = new Scanner(node.getAttribute("points"));
            path.moveTo(scanner.nextFloat(), scanner.nextFloat());
            while (scanner.hasNext()) {
                path.lineTo(scanner.nextFloat(), scanner.nextFloat());
            }
            path.closePath();
            return path;
        }),
        new ElementProcessor("circle", (Element node) -> {
            float x = Float.parseFloat(node.getAttribute("cx"));
            float y = Float.parseFloat(node.getAttribute("cy"));
            float r = Float.parseFloat(node.getAttribute("r"));
            return new Ellipse2D.Float(x - r, y - r, r * 2, r * 2);
        }),
        new ElementProcessor("ellipse", (Element node) -> {
            float x = Float.parseFloat(node.getAttribute("cx"));
            float y = Float.parseFloat(node.getAttribute("cy"));
            float rx = Float.parseFloat(node.getAttribute("rx"));
            float ry = Float.parseFloat(node.getAttribute("ry"));
            return new Ellipse2D.Float(x - rx, y - ry, rx * 2, ry * 2);
        }),
        new ElementProcessor("line", (Element node) -> {
            float x1 = Float.parseFloat(node.getAttribute("x1"));
            float y1 = Float.parseFloat(node.getAttribute("y1"));
            float x2 = Float.parseFloat(node.getAttribute("x2"));
            float y2 = Float.parseFloat(node.getAttribute("y2"));
            return new Line2D.Float(x1, y1, x2, y2);
        })
    );

    // Helper for the path's arc operation to calculate angle in degrees between vectors
    private static double angleHelper(double ux, double uy, double vx, double vy) {
        double dot = ux * vx + uy * vy;
        double det = ux * vy - uy * vx;
        return Math.toDegrees(-Math.atan2(det, dot));
    }

    /**
     * SVGImage Constructor creates a dynamically-rendered vector image with data
     * from a provided SVG image URL.
     *
     * @param url URL to SVG file.
     */
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
            for (int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);
                ElementProcessor processor = processors.get(node.getNodeName());
                if (processor != null) {
                    items[i] = processor.process(node);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Get a dynamically-rendered {@code Icon} of a desired width and height displaying
     * the SVG image.
     *
     * @param width Desired icon width.
     * @param height Desired icon height.
     * @return Icon displaying SVG Image.
     */
    public Icon getIcon(int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics graphics, int x, int y) {
                Graphics2D g = (Graphics2D) graphics;
                g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                for (Drawable item : items) {
                    if (item == null) {
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