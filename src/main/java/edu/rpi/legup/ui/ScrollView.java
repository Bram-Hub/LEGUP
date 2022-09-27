package edu.rpi.legup.ui;

import edu.rpi.legup.controller.Controller;

import java.awt.*;
import java.lang.Double;

import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.*;

public class ScrollView extends JScrollPane {
    private final static Logger LOGGER = Logger.getLogger(ScrollView.class.getName());

    private static final double minScale = 0.25;
    private static final double maxScale = 4.0;
    private static final double[] levels = {0.25, 1.0 / 3.0, 0.50, 2.0 / 3.0, 1.0, 2.0, 3.0, 4.0};

    private Dimension viewSize;
    private Dimension zoomSize;
    private TreeSet<Double> zoomLevels;

    private double scale;

    private Controller controller;
    private ZoomablePane canvas;
    private ZoomWidget widget;

    /**
     * ScrollView Constructor - creates a ScrollView object using
     * the controller handle the ui events
     *
     * @param controller controller that handles the ui events
     */
    public ScrollView(Controller controller) {
        super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);

        viewSize = new Dimension();
        zoomSize = new Dimension();
        scale = 1.0;

        this.canvas = new ZoomablePane(this);

        viewport.setView(canvas);

        zoomLevels = new TreeSet<>();
        for (Double level : levels) {
            zoomLevels.add(level);
        }

        widget = new ZoomWidget(this);
        setCorner(JScrollPane.LOWER_RIGHT_CORNER, widget);

        setWheelScrollingEnabled(false);

        this.controller = controller;
        controller.setViewer(this);
        canvas.addMouseMotionListener(controller);
        canvas.addMouseListener(controller);
        viewport.addMouseWheelListener(controller);
        viewport.addMouseListener(controller);
        viewport.addMouseMotionListener(controller);
    }

    /**
     * Creates a customized viewport for the scroll pane
     *
     * @return viewport for the scroll pane
     */
    @Override
    protected JViewport createViewport() {
        return new JViewport() {
            @Override
            protected LayoutManager createLayoutManager() {
                return new ViewportLayout() {
                    @Override
                    public void layoutContainer(Container parent) {
                        Point point = viewport.getViewPosition();
                        // determine the maximum x and y view positions
                        int mx = canvas.getWidth() - viewport.getWidth();
                        int my = canvas.getHeight() - viewport.getHeight();
                        // obey edge boundaries
                        if (point.x < 0) {
                            point.x = 0;
                        }
                        if (point.x > mx) {
                            point.x = mx;
                        }
                        if (point.y < 0) {
                            point.y = 0;
                        }
                        if (point.y > my) {
                            point.y = my;
                        }
                        // center margins
                        if (mx < 0) {
                            point.x = mx / 2;
                        }
                        if (my < 0) {
                            point.y = my / 2;
                        }
                        viewport.setViewPosition(point);
                    }
                };
            }
        };
    }

    /**
     * Updates zoomSize and view viewSize with the new scale
     */
    private void updateSize() {
        zoomSize.setSize((int) (viewSize.width * scale), (int) (viewSize.height * scale));
        viewport.setViewSize(zoomSize);
    }

    /**
     * Updates the viewport position
     *
     * @param point         point to set the viewport to
     * @param magnification magnification to set the viewport to
     */
    public void updatePosition(Point point, double magnification) {
        Point position = viewport.getViewPosition();
        position.x = (int) ((double) (point.x + position.x) * magnification - point.x + 0.0);
        position.y = (int) ((double) (point.y + position.y) * magnification - point.y + 0.0);
        viewport.setViewPosition(position);
    }

    /**
     * Zooms in or out on a position within the dynamicView
     *
     * @param n     level of zoom - n < 0 is zoom in, n > 0 is zoom out
     * @param point position to zoom in on
     */
    public void zoom(int n, Point point) {
        // if no Point is given, keep current center
        if (point == null) {
            point = new Point(viewport.getWidth() / 2 + viewport.getX(), viewport.getHeight() / 2 + viewport.getY());
        }
        // magnification level
        double mag = (double) n * 1.05;
        // zoom in
        if (n < 0) {
            mag = -mag;
            // check zoom bounds
            if (scale * mag > maxScale) {
                mag = maxScale / scale;
            }
            // update
            scale *= mag;
            updateSize();
            updatePosition(point, mag);
            // zoom out
        }
        else {
            mag = 1 / mag;
            // check zoom bounds
            if (scale * mag < minScale) {
                mag = minScale / scale;
            }
            // update
            scale *= mag;
            updatePosition(point, mag);
            updateSize();
        }
        // update the scrollpane and subclass
        revalidate();
    }

    public void zoomTo(double newScale) {
        //System.out.println("Zooming to " + newScale);

        // check zoom bounds
        if (newScale < minScale) {
            newScale = minScale;
        }
        if (newScale > maxScale) {
            newScale = maxScale;
        }
        if (newScale == scale) {
            return;
        }
        // calculate the newScale and center point
        double mag = newScale / scale;
        Point p = new Point(viewport.getWidth() / 2 + viewport.getX(),
                viewport.getHeight() / 2 + viewport.getY());

        // set scale directly
        scale = newScale;
        // zoom in
        if (mag > 1.0) {
            updateSize();
            updatePosition(p, mag);
            // zoom out
        }
        else {
            updatePosition(p, mag);
            updateSize();
        }
        // update the scrollpane and subclass
        revalidate();
    }

    /**
     * Get the ideal zoom based on the viewSize
     */
    public void zoomFit() {
        if (viewport.getWidth() != 0 && viewport.getHeight() != 0) {
            double fitWidth = (viewport.getWidth() - 8.0) / viewSize.width;
            double fitHeight = (viewport.getHeight() - 8.0) / viewSize.height;

            zoomTo(Math.min(fitWidth, fitHeight));
        }
    }

    /**
     * Zooms in to the next zoom level
     */
    public void zoomIn() {
        // find the next valid zoom level
        Double newScale = zoomLevels.higher(scale);
        if (newScale != null) {
            zoomTo(newScale);
        }
    }

    /**
     * Zooms out to the previous zoom level
     */
    public void zoomOut() {
        // find the next valid zoom level
        Double newScale = zoomLevels.lower(scale);
        if (newScale != null) {
            zoomTo(newScale);
        }
    }

    /**
     * Converts canvas coordinates to draw coordinates
     *
     * @param point canvas coordinate
     * @return draw coordinate as Point
     */
    public Point getActualPoint(Point point) {
        return new Point((int) (point.x / scale), (int) (point.y / scale));
    }

    /**
     * Gets the zoom amount
     *
     * @return zoom scale
     */
    public int getZoom() {
        return (int) (scale * 100.0);
    }

    /**
     * Gets the scale for the dynamic dynamicView
     *
     * @return scale of the dynamic dynamicView
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets the background of the viewport
     *
     * @param c color of the background
     */
    public void setBackground(Color c) {
        viewport.setBackground(c);
    }

    /**
     * Gets the dimension of the viewport
     *
     * @return dimension of the viewport
     */
    public Dimension getSize() {
        return viewSize;
    }

    /**
     * Sets the dimension of the viewport
     *
     * @param size new dimension of the viewport
     */
    public void setSize(Dimension size) {
        this.viewSize = size;
        updateSize();
    }

    public ZoomablePane getCanvas() {
        return canvas;
    }

    /**
     * Draws the ScrollView
     *
     * @param graphics2D Graphics2D object used for drawing
     */
    protected void draw(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        canvas.paint(graphics2D);
    }

    /**
     * Scroll up or down on the ScrollView
     *
     * @param mag The magnitude for scroll up
     *            positive is scroll up, negative is scroll down,
     *            recommend to use getWheelRotation() as the mag
     */
    public void scroll(int mag) {
        Point point = super.viewport.getViewPosition();
        // the point changing speed changes with the scale
        point.y += mag * getZoom() / 20;
        viewport.setViewPosition(point);
        updateSize();
        revalidate();
    }
}
