
import app.Config;
import app.GameBoardFacade;
import app.InvalidConfigException;

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
