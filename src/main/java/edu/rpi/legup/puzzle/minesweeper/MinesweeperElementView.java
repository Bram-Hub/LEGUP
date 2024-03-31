package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.puzzle.lightup.LightUpView;
import edu.rpi.legup.ui.boardview.GridElementView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MinesweeperElementView extends GridElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public MinesweeperElementView(@NotNull MinesweeperCell cell) {
        super(cell);
    }

    @Override
    public @NotNull MinesweeperCell getPuzzleElement() {
        return (MinesweeperCell) super.getPuzzleElement();
    }

    @Override
    @SuppressWarnings("Duplicates")
    @Contract(pure = true)
    public void drawElement(@NotNull Graphics2D graphics2D) {
        final MinesweeperCell cell = (MinesweeperCell) puzzleElement;
        final MinesweeperTileType type = cell.getTileType();
        if (type == MinesweeperTileType.FLAG) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            final FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            final String value = String.valueOf(((MinesweeperCell) puzzleElement).getData().data());
            final int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            final int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
            return;
        }
        if (type == MinesweeperTileType.UNSET) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.DARK_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            return;
        }
        if (type == MinesweeperTileType.EMPTY) {
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.drawImage(
                    MinesweeperView.EMPTY_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    Color.GRAY,
                    null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        if (type == MinesweeperTileType.BOMB) {
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.drawImage(
                    MinesweeperView.BOMB_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    Color.GRAY,
                    null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
