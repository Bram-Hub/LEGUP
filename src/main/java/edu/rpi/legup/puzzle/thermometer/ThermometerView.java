package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ThermometerView extends GridBoardView {
    static Image EMPTYHEAD, BLOCKEDHEAD, FILLEDHEAD,
            EMPTYSHAFT, BLOCKEDSHAFT, FILLEDSHAFT,
            EMPTYTIP, BLOCKEDTIP, FILLEDTIP;

    static {
        try {
            EMPTYHEAD =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/HeadEmp.png"));
            BLOCKEDHEAD =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/HeadBlock.png"));
            FILLEDHEAD =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/HeadFill.png"));
            EMPTYSHAFT =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/ShaftEmp.png"));
            BLOCKEDSHAFT =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/ShaftBlock.png"));
            FILLEDSHAFT =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/ShaftFill.png"));
            EMPTYTIP =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/TipEmp.png"));
            BLOCKEDTIP =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/TipBlock.png"));
            FILLEDTIP =
                    ImageIO.read(ClassLoader.getSystemResourceAsStream(
                            "edu/rpi/legup/images/thermometer/TipFill.png"));
        } catch (IOException e) {
            System.out.println("Twagedy :((((");
        }
    }
    public ThermometerView(ThermometerBoard board) {
        super(new BoardController(), new ThermometerController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            ThermometerCell cell = (ThermometerCell) puzzleElement;
            Point loc = cell.getLocation();
            ThermometerElementView elementView = new ThermometerElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}

