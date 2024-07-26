package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StarBattleElementView extends GridElementView {

    public StarBattleElementView(@NotNull StarBattleCell cell) {
        super(cell);
    }

    @Override
    public @NotNull StarBattleCell getPuzzleElement() {
        return (StarBattleCell) super.getPuzzleElement();
    }

    @Override
    @Contract(pure = true)
    public void drawElement(@NotNull Graphics2D graphics2D) {
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
