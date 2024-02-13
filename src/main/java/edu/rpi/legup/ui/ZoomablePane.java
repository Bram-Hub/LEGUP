package edu.rpi.legup.ui;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;

public class ZoomablePane extends JLayeredPane {
  private ScrollView viewer;

  /**
   * ZoomablePane Constructor creates scalable JComponent
   *
   * @param viewer dynamic dynamicView
   */
  public ZoomablePane(ScrollView viewer) {
    this.viewer = viewer;
  }

  /**
   * Paints the JComponents
   *
   * @param graphics graphics object used to paint the JComponent
   */
  public void paint(Graphics graphics) {
    Graphics2D graphics2D = (Graphics2D) graphics;
    graphics2D.scale(viewer.getScale(), viewer.getScale());
    viewer.draw(graphics2D);
  }

  /**
   * Processes an event and sends it to the dynamicView
   *
   * @param e AWTEvent to process
   */
  protected void processEvent(AWTEvent e) {
    viewer.dispatchEvent(e);
    super.processEvent(e);
  }
}
