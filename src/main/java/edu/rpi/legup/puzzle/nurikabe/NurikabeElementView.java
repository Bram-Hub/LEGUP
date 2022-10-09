package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class NurikabeElementView extends GridElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public NurikabeElementView(NurikabeCell cell) {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public NurikabeCell getPuzzleElement() {
        return (NurikabeCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        NurikabeCell cell = (NurikabeCell) puzzleElement;
        NurikabeType type = cell.getType();
        if (type == NurikabeType.NUMBER) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);

            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        } else if (type == NurikabeType.BLACK) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
        } else if (type == NurikabeType.WHITE) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == NurikabeType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
