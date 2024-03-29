package edu.rpi.legup.puzzle.starbattle;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.rpi.legup.puzzle.starbattle.StarBattleView;
import edu.rpi.legup.ui.boardview.GridBoardView;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.*;

public class StarBattleView extends GridBoardView {
    static Image STAR;

    static {
        try {
            STAR = ImageIO.read(ClassLoader.getSystemClassLoader().getResource("edu/rpi/legup/images/starbattle/star.gif"));
        }
        catch (IOException e) {
            // pass
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
