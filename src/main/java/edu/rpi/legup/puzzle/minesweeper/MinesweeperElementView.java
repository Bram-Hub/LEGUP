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
        Graphics2D g = (Graphics2D) graphics2D.create();
        final MinesweeperCell cell = (MinesweeperCell) puzzleElement;
        final MinesweeperTileType type = cell.getTileType();
        if (type == MinesweeperTileType.NUMBER) {
            g.setColor(UIManager.getColor("Minesweeper.background"));
            g.fillRect(location.x, location.y, size.width, size.height);

            int intValue = ((MinesweeperCell) puzzleElement).getData().data();
            g.setColor(UIManager.getColor("Minesweeper.color" + intValue));

            g.setFont(UIManager.getFont("Minesweeper.font"));
            final FontMetrics metrics = g.getFontMetrics(g.getFont());
            final String value = String.valueOf(intValue);
            final int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            final int yText =
                    location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();

            g.drawString(value, xText, yText);
        }
        if (type == MinesweeperTileType.UNSET) {
            g.drawImage(
                    MinesweeperView.UNSET_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("Minesweeper.background"),
                    null);
        }
        if (type == MinesweeperTileType.EMPTY) {
            g.drawImage(
                    MinesweeperView.EMPTY_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("Minesweeper.background"),
                    null);
        }
        if (type == MinesweeperTileType.MINE) {
            g.drawImage(
                    MinesweeperView.MINE_IMAGE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("Minesweeper.background"),
                    null);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("Minesweeper.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("Minesweeper.borderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
