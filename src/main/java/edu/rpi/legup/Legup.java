package edu.rpi.legup;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.utility.Logger;

public class Legup {

    /**
     * Starts the Legup Program
     *
     * @param args arguments to Legup
     */
    public static void main(String[] args) {
        Logger.initLogger();
        GameBoardFacade.getInstance();
        GameBoardFacade.setupConfig();
    }
}