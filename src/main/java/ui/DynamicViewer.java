package ui;

import app.Controller;

import java.lang.Double;

import java.util.TreeSet;
import java.util.logging.Logger;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ViewportLayout;

public abstract class DynamicViewer extends JScrollPane
{
    private final static Logger LOGGER = Logger.getLogger(DynamicViewer.class.getName());

    private static final double minScale = 0.25;
    private static final double maxScale = 4.0;
    private static final double levels[] = {0.25, 1.0 / 3.0, 0.50, 2.0 / 3.0, 1.0, 2.0, 3.0, 4.0};

    private Dimension size;
    private Dimension zoomSize;
    private TreeSet<Double> zoomLevels;
    private double scale;

    private Controller controller;
    protected ZoomablePane canvas;
    private ZoomWidget widget;

    /**
     * DynamicViewer Constructor - creates a DynamicViewer object using
     * the controller handle the ui events
     *
     * @param controller controller that handles the ui events
     */
    public DynamicViewer(Controller controller)
    {
        // construct JScrollPane
        super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);

        size = new Dimension();
        zoomSize = new Dimension();
        scale = 1.0;
        setVisible(true);
        // setup viewport
        this.canvas = new ZoomablePane(this);
        canvas.setVisible(true);

        viewport.setView(canvas);
        viewport.setVisible(true);

        // setup zoom levels
        zoomLevels = new TreeSet<>();
        for(Double level : levels)
        {
            zoomLevels.add(level);
        }

        // setup the zoom widget
        widget = new ZoomWidget(this);
        setCorner(JScrollPane.LOWER_RIGHT_CORNER, widget);

        // setup mouse events
        setWheelScrollingEnabled(false);

        // setup controllers and listeners
        this.controller = controller;
        controller.setViewer(this);
        canvas.addMouseMotionListener(controller);
        canvas.addMouseListener(controller);
        viewport.addMouseWheelListener(controller);
    }

    /*** LAYOUT MANAGER ***/
    // override to return a customized viewport
    protected JViewport createViewport()
    {
        // customized viewport
        return new JViewport()
        {
            // override to return a customized layoutmanager

            protected LayoutManager createLayoutManager()
            {
                // customized layoutmanager
                return new ViewportLayout()
                {
                    // positions the view within viewport

                    public void layoutContainer(Container parent)
                    {
                        Point point = viewport.getViewPosition();
                        // determine the maximum x and y view positions
                        int mx = canvas.getWidth() - viewport.getWidth();
                        int my = canvas.getHeight() - viewport.getHeight();
                        // obey edge boundaries
                        if(point.x < 0)
                        {
                            point.x = 0;
                        }
                        if(point.x > mx)
                        {
                            point.x = mx;
                        }
                        if(point.y < 0)
                        {
                            point.y = 0;
                        }
                        if(point.y > my)
                        {
                            point.y = my;
                        }
                        // center margins
                        if(mx < 0)
                        {
                            point.x = mx / 2;
                        }
                        if(my < 0)
                        {
                            point.y = my / 2;
                        }
                        viewport.setViewPosition(point);
                    }
                };
            }
        };
    }

    /**
     * Gets the viewport for this DynamicViewer
     *
     * @return viewport for this DynamicViewer
     */
    public JViewport getViewport()
    {
        return viewport;
    }

    /**
     * Updates zoomSize and view size with the new scale
     */
    private void updateSize()
    {
        zoomSize.setSize((int) (size.width * scale), (int) (size.height * scale));
        viewport.setViewSize(zoomSize);
        System.out.println("Zoom size: " + zoomSize + ", Viewport size: " + size);
    }

    // updates view position to user for zooming

    /**
     * Updates the viewport position
     *
     * @param point
     * @param magnification
     */
    public void updatePosition(Point point, double magnification)
    {
        Point position = viewport.getViewPosition();
        position.x = (int) ((double) (point.x + position.x) * magnification - point.x + 0.0);
        position.y = (int) ((double) (point.y + position.y) * magnification - point.y + 0.0);
        viewport.setViewPosition(position);
    }

    /**
     * Zooms in or out on a position within the viewer
     *
     * @param n level of zoom - n < 0 is zoom in, n > 0 is zoom out
     * @param point position to zoom in on
     */
    public void zoom(int n, Point point)
    {
        // if no Point is given, keep current center
        if(point == null)
        {
            point = new Point(viewport.getWidth() / 2 + viewport.getX(), viewport.getHeight() / 2 + viewport.getY());
        }
        // magnification level
        double mag = (double) n * 1.05;
        // zoom in
        if(n < 0)
        {
            mag = -mag;
            // check zoom bounds
            if(scale * mag > maxScale)
            {
                mag = maxScale / scale;
            }
            // update
            scale *= mag;
            updateSize();
            updatePosition(point, mag);
            // zoom out
        }
        else
        {
            mag = 1 / mag;
            // check zoom bounds
            if(scale * mag < minScale)
            {
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

    public void zoomTo(double newScale)
    {
        // check zoom bounds
        if(newScale < minScale)
        {
            newScale = minScale;
        }
        if(newScale > maxScale)
        {
            newScale = maxScale;
        }
        if(newScale == scale)
        {
            return;
        }
        // calculate the newScale and center point
        double mag = newScale / scale;
        Point p = new Point(viewport.getWidth() / 2 + viewport.getX(), viewport.getHeight() / 2 + viewport.getY());

        // set scale directly
        scale = newScale;
        // zoom in
        if(mag > 1.0)
        {
            updateSize();
            updatePosition(p, mag);
            // zoom out
        }
        else
        {
            updatePosition(p, mag);
            updateSize();
        }
        // update the scrollpane and subclass
        revalidate();
    }

    /**
     * Get the ideal zoom based on the size
     */
    public void zoomFit()
    {
        // find the ideal width and height scale
        double fitWidth = (viewport.getWidth() - 8.0) / size.width;
        double fitHeight = (viewport.getHeight() - 8.0) / size.height;
        // choose the smaller of the two and zoom
        zoomTo((fitWidth < fitHeight) ? fitWidth : fitHeight);
    }

    /**
     * Zooms in to the next zoom level
     */
    public void zoomIn()
    {
        // find the next valid zoom level
        Double newScale = zoomLevels.higher(scale);
        if(newScale != null)
        {
            zoomTo(newScale);
        }
    }

    /**
     * Zooms out to the previous zoom level
     */
    public void zoomOut()
    {
        // find the next valid zoom level
        Double newScale = zoomLevels.lower(scale);
        if(newScale != null)
        {
            zoomTo(newScale.doubleValue());
        }
    }

    /**
     * Converts canvas coordinates to draw coordinates
     *
     * @param point canvas coordinate
     * @return draw coordinate as Point
     */
    public Point getActualPoint(Point point)
    {
        return new Point((int)(point.x / scale), (int)(point.y / scale));
    }

    /**
     * Gets the zoom amount
     *
     * @return zoom scale
     */
    public int getZoom()
    {
        return (int) (scale * 100.0);
    }

    /**
     * Gets the scale for the dynamic viewer
     *
     * @return scale of the dynamic viewer
     */
    public double getScale()
    {
        return scale;
    }

    /**
     * Sets the background of the viewport
     *
     * @param c color of the background
     */
    public void setBackground(Color c)
    {
        viewport.setBackground(c);
    }

    /**
     * Gets the dimension of the viewport
     *
     * @return dimension of the viewport
     */
    public Dimension getSize()
    {
        return size;
    }

    /**
     * Sets the dimension of the viewport
     *
     * @param size new dimension of the viewport
     */
    public void setSize(Dimension size)
    {
        this.size = size;
        updateSize();
    }

    /**
     * Draws the DynamicViewer
     *
     * @param graphics2D Graphics2D object used for drawing
     */
    protected void draw(Graphics2D graphics2D)
    {
        canvas.paint(graphics2D);
    }
}
