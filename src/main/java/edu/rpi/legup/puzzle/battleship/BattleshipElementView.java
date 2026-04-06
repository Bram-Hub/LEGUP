package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import javax.swing.UIManager;

public class BattleshipElementView extends GridElementView {

    public BattleshipElementView(BattleshipCell cell) {
        super(cell);
    }

    /**
     * Draws on the given frame based on the type of the cell of the current puzzleElement
     *
     * @param graphics2D the frame to be drawn on
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        BattleshipCell cell = (BattleshipCell) puzzleElement;
        BattleshipType type = cell.getType();

        switch (type) {
            case UNKNOWN:
                g.setColor(UIManager.getColor("Battleship.unknown"));
                g.fillRect(location.x, location.y, size.width, size.height);
                break;
            case WATER:
                g.setColor(UIManager.getColor("Battleship.water"));
                g.fillRect(location.x, location.y, size.width, size.height);
                break;
            case SHIP_UNKNOWN:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillRect(
                        location.x + 3 * size.width / 8,
                        location.y + 3 * size.height / 8,
                        size.width / 4,
                        size.height / 4);

                g.setColor(UIManager.getColor("Battleship.text"));
                g.setFont(UIManager.getFont("Battleship.font"));
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                String value = "?";
                int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
                int yText =
                        location.y
                                + ((size.height - metrics.getHeight()) / 2)
                                + metrics.getAscent();
                g.drawString(value, xText, yText);
                break;
            case SUBMARINE:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillOval(
                        location.x + size.width / 4,
                        location.y + size.width / 4,
                        size.width / 2,
                        size.height / 2);
                break;
            case SHIP_TOP:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillArc(
                        location.x,
                        location.y - size.height / 2,
                        size.width,
                        size.height,
                        180,
                        180);
                break;
            case SHIP_RIGHT:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillArc(
                        location.x + size.height / 2, location.y, size.width, size.height, 90, 180);
                break;
            case SHIP_BOTTOM:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillArc(
                        location.x, location.y + size.height / 2, size.width, size.height, 0, 180);
                break;
            case SHIP_LEFT:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillArc(
                        location.x - size.height / 2,
                        location.y,
                        size.width,
                        size.height,
                        270,
                        180);
                break;
            case SHIP_MIDDLE:
                g.setColor(UIManager.getColor("Battleship.ship"));
                g.fillRect(location.x, location.y, size.width, size.height);
                break;
            default:
                g.setColor(UIManager.getColor("Battleship.unrecognized"));
                break;
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setStroke(new BasicStroke(UIManager.getInt("Battleship.borderWidth")));
        g.setColor(UIManager.getColor("Battleship.borderColor"));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
