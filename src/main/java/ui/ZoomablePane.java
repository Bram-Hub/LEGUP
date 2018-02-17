package ui;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AWTEvent;

public class ZoomablePane extends JComponent
{
    private DynamicViewer viewer;

    /**
     * ZoomablePane Constructor - creates scalable JComponent
     *
     * @param viewer dynamic viewer
     */
    public ZoomablePane(DynamicViewer viewer)
    {
        this.viewer = viewer;
    }

    /**
     * Paints the JComponents
     *
     * @param graphics graphics object used to paint the JComponent
     */
    public void paint(Graphics graphics)
    {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.scale(viewer.getScale(), viewer.getScale());
        graphics2D.drawString("Hello World", 100,100);
        viewer.draw(graphics2D);
    }

    /**
     * Processes the event
     *
     * @param e AWTEvent to process
     */
    protected void processEvent(AWTEvent e)
    {
        viewer.dispatchEvent(e);
        super.processEvent(e);
    }
}
