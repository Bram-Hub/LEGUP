package edu.rpi.legup.controller;

import edu.rpi.legup.ui.zoompane.ZoomView;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

/**
 * {@code ZoomViewController} is an abstract class designed to handle various mouse and key events to provide
 * interactability for a {@code ZoomView}.
 */
public abstract class ZoomViewController implements KeyListener, ActionListener {

    /** The view that this controller is installed on. */
    private ZoomView view;

    /** Set the {@code ZoomView} that this controller is installed in. */
    public void setView(@Nullable ZoomView newView) { view = newView; }

    /** @return the {@code ZoomView} that this controller is installed in. */
    public ZoomView getView() { return view; }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {}

    @Override
    public void keyTyped(@NotNull KeyEvent e) {}

    @Override
    public void keyPressed(@NotNull KeyEvent e) {}

    @Override
    public void keyReleased(@NotNull KeyEvent e) {}

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     * @param e the event to be processed
     */
    public void mouseClicked(@NotNull ControllerMouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param e the event to be processed
     */
    public void mousePressed(@NotNull ControllerMouseEvent e) {}

    /**
     * Invoked when a mouse button has been released on a component.
     * @param e the event to be processed
     */
    public void mouseReleased(@NotNull ControllerMouseEvent e) {}

    /**
     * Invoked when the mouse enters a component.
     * @param e the event to be processed
     */
    public void mouseEntered(@NotNull ControllerMouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     * @param e the event to be processed
     */
    public void mouseExited(@NotNull ControllerMouseEvent e) {}

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.  {@code MOUSE_DRAGGED} events
     * will continue to be delivered to the component where the drag originated until the mouse button is
     * released (regardless of whether the mouse position is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations, {@code MOUSE_DRAGGED} events may not be
     * delivered during a native Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    public void mouseDragged(@NotNull ControllerMouseEvent e) {}

    /**
     * Invoked when the mouse cursor has been moved onto a component but no buttons have been pushed.
     * @param e the event to be processed
     */
    public void mouseMoved(@NotNull ControllerMouseEvent e) {}

    /** A {@code MouseEvent} that maintains a {@code Point2D} location to support higher precision. */
    public static class ControllerMouseEvent extends MouseEvent {

        /** High precision location of the event. */
        private final Point2D precisePoint;

        /**
         * Constructs a {@code ControllerMouseEvent} object with the specified source component, type, time,
         * modifiers, coordinates, click count, popupTrigger flag, and button number.
         * <p>
         * Creating an invalid event (such as by using more than one of the old _MASKs, or modifier/button
         * values which don't match) results in unspecified behavior.
         *
         * @param source       The {@code Component} that originated the event
         * @param id           An integer indicating the type of event.
         *                     For information on allowable values, see the class description for {@link MouseEvent}
         * @param when         A long integer that gives the time the event occurred.
         *                     Passing negative or zero value is not recommended
         * @param modifiers    a modifier mask describing the modifier keys and mouse buttons (for example, shift,
         *                     ctrl, alt, and meta) that are down during the event. Only extended modifiers are
         *                     allowed to be used as a value for this parameter (see the
         *                     {@link InputEvent#getModifiersEx} class for the description of extended modifiers).
         *                     Passing negative parameter is not recommended.
         *                     Zero value means that no modifiers were passed
         * @param where        The variable precision point for the mouse location.
         * @param clickCount   The number of mouse clicks associated with event.
         *                     Passing negative value is not recommended
         * @param popupTrigger A boolean that equals {@code true} if this event is a trigger for a popup menu
         * @param button       An integer that indicates, which of the mouse buttons has changed its state.
         * The following rules are applied to this parameter:
         * <ul>
         * <li> If support for the extended mouse buttons is {@link Toolkit#areExtraMouseButtonsEnabled() disabled}
         *      by Java then it is allowed to create {@code MouseEvent} objects only with the standard buttons:
         *      {@code NOBUTTON}, {@code BUTTON1}, {@code BUTTON2}, and {@code BUTTON3}.
         * <li> If support for the extended mouse buttons is {@link Toolkit#areExtraMouseButtonsEnabled() enabled}
         *      by Java then it is allowed to create {@code MouseEvent} objects with the standard buttons.
         *      In case the support for extended mouse buttons is {@link Toolkit#areExtraMouseButtonsEnabled() enabled}
         *      by Java, then in addition to the standard buttons, {@code MouseEvent} objects can be created
         *      using buttons from the range starting from 4 to
         *      {@link java.awt.MouseInfo#getNumberOfButtons() MouseInfo.getNumberOfButtons()}
         *      if the mouse has more than three buttons.
         * </ul>
         * @throws IllegalArgumentException if {@code button} is less than zero
         * @throws IllegalArgumentException if {@code button} is greater than BUTTON3
         *                                  and the support for extended mouse buttons is
         *                                  {@link Toolkit#areExtraMouseButtonsEnabled() disabled} by Java
         * @throws IllegalArgumentException if {@code button} is greater than the
         *                                  {@link java.awt.MouseInfo#getNumberOfButtons() current number of buttons}
         *                                  and the support for extended mouse buttons is
         *                                  {@link Toolkit#areExtraMouseButtonsEnabled() enabled} by Java
         * @throws IllegalArgumentException if an invalid {@code button} value is passed in
         * @see #getSource()
         * @see #getID()
         * @see #getWhen()
         * @see #getModifiersEx()
         * @see #getPrecisePoint()
         * @see #getClickCount()
         * @see #isPopupTrigger()
         * @see #getButton()
         */
        public ControllerMouseEvent(@NotNull Component source,
                                    int id,
                                    long when,
                                    @MagicConstant(intValues = {InputEvent.SHIFT_DOWN_MASK, InputEvent.CTRL_DOWN_MASK,
                                            InputEvent.META_DOWN_MASK, InputEvent.ALT_DOWN_MASK,
                                            InputEvent.BUTTON1_DOWN_MASK, InputEvent.BUTTON2_DOWN_MASK,
                                            InputEvent.BUTTON3_DOWN_MASK, InputEvent.ALT_GRAPH_DOWN_MASK})
                                    int modifiers,
                                    @NotNull Point2D where,
                                    int clickCount,
                                    boolean popupTrigger,
                                    int button)
        {
            super(source, id, when, modifiers, (int) Math.round(where.getX()),
                    (int) Math.round(where.getY()), clickCount, popupTrigger, button);

            precisePoint = where;
        }

        /**
         * Returns the {@code x,y} location of the event in view coordinates.
         *
         * @return a {@code Point2D} object containing the x and y view coordinates
         */
        public Point2D getPrecisePoint() { return precisePoint; }
    }
}
