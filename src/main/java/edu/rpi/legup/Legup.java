package edu.rpi.legup;

import edu.rpi.legup.app.Config;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.InvalidConfigException;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Legup extends Application
{
    private final static Logger LOGGER = Logger.getLogger(Legup.class.getName());

    private static MainWindow mainWindow;

    /**
     * Starts the edu.rpi.legup.Legup Program
     *
     * @param args arguments to edu.rpi.legup.Legup
     */
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
        GameBoardFacade.getInstance();
        setConfig();
//        launch(args);
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

    public static MainWindow showProofWindow(Stage stage) throws IOException
    {
        MainWindow window = new MainWindow(stage);
        window.show();
        return window;
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainWindow = showProofWindow(primaryStage);
    }
}