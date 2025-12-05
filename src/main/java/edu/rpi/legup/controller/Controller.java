package edu.rpi.legup.controller;

import edu.rpi.legup.ui.ScrollView;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * {@code Controller} is an abstract class designed to handle various mouse events and provide
 * control functionality for a {@code ScrollView}. It implements several mouse event interfaces to
 * manage interactions such as panning and zooming within a {@code ScrollView}
 */
public abstract class Controller implements MouseMotionListener, MouseListener, MouseWheelListener {
    protected ScrollView viewer;
    private int x, y;
    private boolean pan;
    private Timer scrollEaseTimer;

    /**
     * Controller Constructor creates a controller object to listen to ui events from a {@link
     * ScrollView}
     */
    public Controller() {
        x = y = -1;
        pan = false;
    }

    /**
     * Sets the ScrollView instance that this controller manages
     *
     * @param viewer The ScrollView instance to be set
     */
    public void setViewer(ScrollView viewer) {
        this.viewer = viewer;
    }

    /**
     * Mouse Clicked event no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Mouse Pressed event sets the cursor to the move cursor and stores info for possible panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            pan = true;
            x = e.getX();
            y = e.getY();
            viewer.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
    }

    /**
     * Mouse Released event sets the cursor back to the default cursor and reset info for panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            pan = false;
            viewer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Mouse Entered event no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Mouse Exited event no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Mouse Dragged event adjusts the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (pan) {
            JViewport viewport = viewer.getViewport();
            Point position = viewer.getViewport().getViewPosition();
            position.x += (x - e.getX());
            position.y += (y - e.getY());
            viewport.setViewPosition(position);
            viewer.revalidate();
        }
    }

    /**
     * Mouse Moved event no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseMoved(MouseEvent e) {}

    /**
     * Mouse Wheel Moved event zooms in on the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //System.out.println(e.getPreciseWheelRotation());
        double mag = e.getPreciseWheelRotation();
        if (mag == 0) {
            return;
        }
        if (e.isControlDown()) {
            mag *= 5;
        }
        if ((int) mag == mag) { // For notch-based scrolling mechanisms use timer to zoom smoothly
            if (scrollEaseTimer != null && scrollEaseTimer.isRunning()) {
                mag *= 3;
                scrollEaseTimer.stop();
            }
            final double usableMag = mag;
            final Point usablePoint = e.getPoint();
            scrollEaseTimer = new Timer(5, new ActionListener() {
                int counter = 5;
                public void actionPerformed(ActionEvent e) {
                    if (counter == 0) {
                        ((Timer) e.getSource()).stop();
                    }
                    else {
                        viewer.zoom(usableMag/5, usablePoint);
                        counter--;
                    }
                }
            });
            scrollEaseTimer.start();
        } else {
            viewer.zoom(mag, e.getPoint());
        }
    }
}
