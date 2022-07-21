package edu.rpi.legup.controller;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class CursorController
{
    public static final Cursor BUSY_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    public static final int delay = 500;    // in milliseconds

    private CursorController()
    {
        // Intentionally left empty
    }

    public static ActionListener createListener(final Component component, final ActionListener mainActionListener)
    {
        ActionListener actionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TimerTask timerTask = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        component.setCursor(BUSY_CURSOR);
                    }
                };

                Timer timer = new Timer();
                try
                {
                    timer.schedule(timerTask, delay);
                    mainActionListener.actionPerformed(e);
                }
                finally
                {
                    timer.cancel();
                    component.setCursor(DEFAULT_CURSOR);
                }
            }
        };

        return actionListener;
    }
}

// Sources consulted: http://www.catalysoft.com/articles/busycursor.html
