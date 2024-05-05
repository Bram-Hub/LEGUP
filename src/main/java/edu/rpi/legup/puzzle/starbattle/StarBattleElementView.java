package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;

public class StarBattleElementView extends GridElementView {

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
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == StarBattleCellType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
