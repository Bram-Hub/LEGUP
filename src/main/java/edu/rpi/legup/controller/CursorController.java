package edu.rpi.legup.controller;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class CursorController {
    public static final Cursor BUSY_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    public static final int DELAY = 200; // in milliseconds

    private CursorController() {
        // Intentionally left empty
    }

    /**
     * Creates an ActionListener that will still do the same action processing as the given
     * ActionListener while also displaying a loading cursor if the time it takes to execute the
     * given process exceeds the time (in milliseconds) specified in this.DELAY
     *
     * <p>Sources consulted: http://www.catalysoft.com/articles/busycursor.html
     *
     * @param component The component you want to set the cursor for
     * @param mainActionListener The ActionListener that does the intended action processing
     * @return An ActionListener object that does the same action processing as mainActionListener
     *     while also modifying the cursor if needed
     */
    public static ActionListener createListener(
            final Component component, final ActionListener mainActionListener) {
        ActionListener actionListener =
                e -> {
                    TimerTask timerTask =
                            new TimerTask() {
                                @Override
                                public void run() {
                                    component.setCursor(BUSY_CURSOR);
                                }
                            };

                    Timer timer = new Timer();
                    try {
                        timer.schedule(timerTask, DELAY);
                        mainActionListener.actionPerformed(e);
                    } finally {
                        timer.cancel();
                        component.setCursor(DEFAULT_CURSOR);
                    }
                };

        return actionListener;
    }
}
