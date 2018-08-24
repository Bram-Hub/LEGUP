package edu.rpi.legup;

import edu.rpi.legup.app.Config;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.InvalidConfigException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Legup
{
    private final static Logger LOGGER = Logger.getLogger(Legup.class.getName());

    /**
     * Starts the Legup Program
     *
     * @param args arguments to Legup
     */
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
        GameBoardFacade.getInstance();
        setConfig();
    }

    private static void setConfig()
    {
        Config config = null;
        try
        {
            config = new Config();
        }
        catch(InvalidConfigException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        GameBoardFacade.getInstance().setConfig(config);
    }
}