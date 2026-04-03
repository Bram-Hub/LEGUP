package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class BattleshipElementView extends GridElementView {

    public BattleshipElementView(BattleshipCell cell) {
        super(cell);
    }

    @Override
    /**
     * Draws on the given frame based on the type of the cell of the current puzzleElement
     *
     * @param graphics2D the frame to be drawn on
     */
    public void drawElement(Graphics2D graphics2D) {
        BattleshipCell cell = (BattleshipCell) puzzleElement;
        BattleshipType type = cell.getType();

        switch (type) {
            case UNKNOWN:
                graphics2D.setColor(UIManager.getColor("Battleship.unknown"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                break;
            case WATER:
                graphics2D.setColor(UIManager.getColor("Battleship.water"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                break;
            case SHIP_UNKNOWN:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillRect(
                        location.x + 3 * size.width / 8,
                        location.y + 3 * size.height / 8,
                        size.width / 4,
                        size.height / 4);

                graphics2D.setColor(UIManager.getColor("Battleship.text"));
                graphics2D.setFont(UIManager.getFont("Battleship.font"));
                FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
                String value = "?";
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                graphics2D.drawString(value, xText, yText);
                break;
            case SUBMARINE:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillOval(
                        location.x + size.width / 4,
                        location.y + size.width / 4,
                        size.width / 2,
                        size.height / 2);
                break;
            case SHIP_TOP:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillArc(
                        location.x,
                        location.y - size.height / 2,
                        size.width,
                        size.height,
                        180,
                        180);
                break;
            case SHIP_RIGHT:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillArc(
                        location.x + size.height / 2, location.y, size.width, size.height, 90, 180);
                break;
            case SHIP_BOTTOM:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillArc(
                        location.x, location.y + size.height / 2, size.width, size.height, 0, 180);
                break;
            case SHIP_LEFT:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillArc(
                        location.x - size.height / 2,
                        location.y,
                        size.width,
                        size.height,
                        270,
                        180);
                break;
            case SHIP_MIDDLE:
                graphics2D.setColor(UIManager.getColor("Battleship.ship"));
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
                break;
            default:
                graphics2D.setColor(new Color(0xE040FB));
                break;
        }

        graphics2D.setColor(UIManager.getColor("Battleship.borderColor"));
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("Battleship.borderWidth")));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
