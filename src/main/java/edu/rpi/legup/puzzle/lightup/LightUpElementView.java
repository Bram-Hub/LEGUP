package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class LightUpElementView extends GridElementView {

    public LightUpElementView(LightUpCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public LightUpCell getPuzzleElement() {
        return (LightUpCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        LightUpCell cell = (LightUpCell) puzzleElement;
        LightUpCellType type = cell.getType();
        switch (type) {
            case NUMBER -> {
                graphics2D.setColor(UIManager.getColor("LightUp.white"));
                graphics2D.setFont(UIManager.getFont("LightUp.font"));
                FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
                String value = String.valueOf(puzzleElement.getData());
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
                graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
            }
            case BLACK -> {
                graphics2D.setColor(UIManager.getColor("LightUp.black"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            case EMPTY -> {
                graphics2D.setColor(UIManager.getColor(
                        cell.isLite() ? "LightUp.light" : "LightUp.white"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                graphics2D.setColor(UIManager.getColor("LightUp.black"));
                graphics2D.fillRect(
                        location.x + size.width * 7 / 16,
                        location.y + size.height * 7 / 16,
                        size.width / 8,
                        size.height / 8);
            }
            case UNKNOWN -> {
                graphics2D.setColor(UIManager.getColor(
                        cell.isLite() ? "LightUp.light" : "LightUp.unknown"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            case BULB -> {
                graphics2D.setColor(UIManager.getColor("LightUp.unknown"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                graphics2D.drawImage(
                        LightUpView.lightImage,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        UIManager.getColor("LightUp.light"),
                        null);
            }
        }
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("LightUp.borderWidth")));
        graphics2D.setColor(UIManager.getColor("LightUp.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
