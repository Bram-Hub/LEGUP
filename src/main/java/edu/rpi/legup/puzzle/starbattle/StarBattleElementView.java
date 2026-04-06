package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.UIManager;

public class StarBattleElementView extends GridElementView {
    private static final Logger LOGGER =
            LogManager.getLogger(StarBattleElementView.class.getName());

    public StarBattleElementView(StarBattleCell cell) {
        super(cell);
    }

    @Override
    public StarBattleCell getPuzzleElement() {
        return (StarBattleCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        StarBattleCell cell = (StarBattleCell) puzzleElement;
        StarBattleCellType type = cell.getType();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("point for cell is {},{}\n", location.x, location.y);
        }
        if (type == StarBattleCellType.STAR) {
            g.drawImage(
                    StarBattleView.STAR,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("StarBattle.background"),
                    null);
        } else if (type == StarBattleCellType.BLACK) {
            g.setColor(UIManager.getColor("StarBattle.background"));
            g.fillRect(location.x, location.y, size.width, size.height);
            g.setColor(UIManager.getColor("StarBattle.foreground"));
            g.fillRect(
                    location.x + size.width * 7 / 16,
                    location.y + size.height * 7 / 16,
                    size.width / 8,
                    size.height / 8);
        } else if (type == StarBattleCellType.UNKNOWN) {
            g.setColor(UIManager.getColor("StarBattle.unknown"));
            g.fillRect(location.x, location.y, size.width, size.height);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("StarBattle.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt("StarBattle.cellBorderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
