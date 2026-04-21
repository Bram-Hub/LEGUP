package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StarBattleElementView extends GridElementView {
    private static final Logger LOGGER =
            LogManager.getLogger(StarBattleElementView.class.getName());

    /**
     * Constructs a StarBattleElementView for the given StarBattleCell.
     *
     * @param cell the StarBattleCell associated with this view
     */
    public StarBattleElementView(StarBattleCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view.
     *
     * @return the StarBattleCell associated with this view
     */
    @Override
    public StarBattleCell getPuzzleElement() {
        return (StarBattleCell) super.getPuzzleElement();
    }

    /**
     * Draws the visual representation of the StarBattleCell based on its type.
     *
     * @param graphics2D the Graphics2D context used for rendering
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        StarBattleCell cell = (StarBattleCell) puzzleElement;
        StarBattleCellType type = cell.getType();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("point for cell is {},{}\n", location.x, location.y);
        }
        if (type == StarBattleCellType.STAR) {
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.drawImage(
                    StarBattleView.STAR,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    Color.WHITE,
                    null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == StarBattleCellType.BLACK) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(
                    location.x + size.width * 7 / 16,
                    location.y + size.height * 7 / 16,
                    size.width / 8,
                    size.height / 8);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == StarBattleCellType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        // else if (type == StarBattleCellType.BORDER){          //would likely define border as a
        // cell type so as to
        // not have to rewrite this whole function
        /*                                                      //maybe one type for vertical and another for horizontal
            graphics2D.setStrike(new BasicStrike(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        */

    }
}
