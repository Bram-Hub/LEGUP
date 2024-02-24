package edu.rpi.legup.puzzle.starbattle;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeView;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleController;
import edu.rpi.legup.puzzle.starbattle.StarBattleElementView;
import edu.rpi.legup.puzzle.starbattle.StarBattleView;
import edu.rpi.legup.ui.boardview.GridBoardView;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.ElementView;
import java.awt.*;
import java.util.ArrayList;

public class StarBattleView extends GridBoardView {
    private final static Logger LOGGER = LogManager.getLogger(StarBattleView.class.getName());

    static Image STAR;

    static {
        try {
            STAR = ImageIO.read(ClassLoader.getSystemResourceAsStream("edu/rpi/legup/images/starbattle/star.png"));
        }
        catch (IOException e) {
            LOGGER.error("Failed to open StarBattle images");
        }
    }

    public StarBattleView(StarBattleBoard board) {
        super(new BoardController(), new StarBattleController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            StarBattleCell cell = (StarBattleCell) puzzleElement;
            Point loc = cell.getLocation();
            StarBattleElementView elementView = new StarBattleElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }

    
}
