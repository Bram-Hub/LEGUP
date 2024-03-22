package edu.rpi.legup;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.ui.color.ColorPreferences;
import edu.rpi.legup.ui.lookandfeel.LegupLookAndFeel;
import edu.rpi.legup.utility.Logger;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.multi.MultiLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.*;

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
