package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MinesweeperElementView extends GridElementView {

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
        if (type == MinesweeperTileType.NUMBER) {
            graphics2D.setColor(UIManager.getColor("Minesweeper.background"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);

            int intValue = ((MinesweeperCell) puzzleElement).getData().data();
            graphics2D.setColor(UIManager.getColor("Minesweeper.color" + intValue));

            graphics2D.setFont(UIManager.getFont("Minesweeper.font"));
            final FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
            final String value = String.valueOf(intValue);
            final int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            final int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();

            graphics2D.drawString(value, xText, yText);
        }
        if (type == MinesweeperTileType.UNSET) {
            graphics2D.drawImage(
                    MinesweeperView.UNSET_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("Minesweeper.background"),
                    null);
        }
        if (type == MinesweeperTileType.EMPTY) {
            graphics2D.drawImage(
                    MinesweeperView.EMPTY_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("Minesweeper.background"),
                    null);
        }
        if (type == MinesweeperTileType.MINE) {
            graphics2D.drawImage(
                    MinesweeperView.MINE_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("Minesweeper.background"),
                    null);
        }
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Minesweeper.borderWidth")));
        graphics2D.setColor(UIManager.getColor("Minesweeper.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
