package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;
import org.jetbrains.annotations.NotNull;

public class LightUpElementView extends GridElementView {

    public LightUpElementView(@NotNull LightUpCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public @NotNull LightUpCell getPuzzleElement() {
        return (LightUpCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(@NotNull Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        LightUpCell cell = (LightUpCell) puzzleElement;
        LightUpCellType type = cell.getType();
        switch (type) {
            case NUMBER -> {
                g.setColor(UIManager.getColor("LightUp.black"));
                g.fillRect(location.x, location.y, size.width, size.height);
                g.setColor(UIManager.getColor("LightUp.white"));
                g.setFont(UIManager.getFont("LightUp.font"));
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                String value = String.valueOf(puzzleElement.getData());
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                g.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
            }
            case BLACK -> {
                g.setColor(UIManager.getColor("LightUp.black"));
                g.fillRect(location.x, location.y, size.width, size.height);
            }
            case EMPTY -> {
                g.setColor(UIManager.getColor(cell.isLite() ? "LightUp.light" : "LightUp.white"));
                g.fillRect(location.x, location.y, size.width, size.height);
                g.setColor(UIManager.getColor("LightUp.black"));
                g.fillRect(
                        location.x + size.width * 7 / 16,
                        location.y + size.height * 7 / 16,
                        size.width / 8,
                        size.height / 8);
            }
            case UNKNOWN -> {
                g.setColor(UIManager.getColor(cell.isLite() ? "LightUp.light" : "LightUp.unknown"));
                g.fillRect(location.x, location.y, size.width, size.height);
            }
            case BULB -> {
                g.setColor(UIManager.getColor("LightUp.unknown"));
                g.fillRect(location.x, location.y, size.width, size.height);
                g.drawImage(
                        LightUpView.lightImage,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        UIManager.getColor("LightUp.light"),
                        null);
            }
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("LightUp.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("LightUp.borderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
