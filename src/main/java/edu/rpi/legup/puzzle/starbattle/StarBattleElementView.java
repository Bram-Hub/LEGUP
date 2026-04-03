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
        StarBattleCell cell = (StarBattleCell) puzzleElement;
        StarBattleCellType type = cell.getType();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("point for cell is {},{}\n", location.x, location.y);
        }
        if (type == StarBattleCellType.STAR) {
            graphics2D.drawImage(
                    StarBattleView.STAR,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    UIManager.getColor("StarBattle.background"),
                    null);
        } else if (type == StarBattleCellType.BLACK) {
            graphics2D.setColor(UIManager.getColor("StarBattle.background"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(UIManager.getColor("StarBattle.foreground"));
            graphics2D.fillRect(
                    location.x + size.width * 7 / 16,
                    location.y + size.height * 7 / 16,
                    size.width / 8,
                    size.height / 8);
        } else if (type == StarBattleCellType.UNKNOWN) {
            graphics2D.setColor(UIManager.getColor("StarBattle.unknown"));
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        }
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("StarBattle.borderWidth")));
        graphics2D.setColor(UIManager.getColor("StarBattle.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
