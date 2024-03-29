package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.lightup.LightUpView;
import edu.rpi.legup.ui.boardview.GridBoardView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class MinesweeperView extends GridBoardView {

    private static final Logger LOGGER = LogManager.getLogger(MinesweeperView.class.getName());
    public static final Image BOMB_IMAGE;

    public static final Image EMPTY_IMAGE;

    static {
        Image tempBombImage = null;
        try {
            tempBombImage =
                    ImageIO.read(
                            Objects.requireNonNull(ClassLoader.getSystemClassLoader()
                                    .getResource("edu/rpi/legup/images/minesweeper/tiles/Bomb.png")));
        } catch (IOException e) {
            LOGGER.error("Failed to open Minesweeper images");
        }
        BOMB_IMAGE = tempBombImage;
    }

    static {
        Image tempEmptyImage = null;
        try {
            tempEmptyImage =
                    ImageIO.read(
                            Objects.requireNonNull(ClassLoader.getSystemClassLoader()
                                    .getResource("edu/rpi/legup/images/minesweeper/tiles/Empty.png")));
        } catch (IOException e) {
            LOGGER.error("Failed to open Minesweeper images");
        }
        EMPTY_IMAGE = tempEmptyImage;
    }


    public MinesweeperView(@NotNull MinesweeperBoard board) {
        super(new BoardController(), new MinesweeperController(), board.getDimension());

        for (PuzzleElement<?> puzzleElement : board.getPuzzleElements()) {
            final MinesweeperCell cell = (MinesweeperCell) puzzleElement;
            final Point loc = cell.getLocation();
            final MinesweeperElementView elementView = new MinesweeperElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}
