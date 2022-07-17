package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class SkyscrapersElementView extends GridElementView {
	private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = new Color(0x212121);
    private static final Color BORDER_COLOR = new Color(0x424242);
    private static final Color BACKGROUND_COLOR = new Color(0xEEEEEE);
    public SkyscrapersElementView(SkyscrapersCell cell) {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
    	/*SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
    	SkyscrapersType type = cell.getType();
        graphics2D.setStroke(new BasicStroke(0));
        if (type == SkyscrapersType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fill(new Rectangle2D.Double(location.x + 0.5f, location.y + 0.5f, size.width - 1, size.height - 1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(new Rectangle2D.Double(location.x + 0.5f, location.y + 0.5f, size.width - 1, size.height - 1));
        } else if (type == SkyscrapersType.TREE) {
            graphics2D.drawImage(SkyscrapersView.TREE, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == SkyscrapersType.GRASS) {
            graphics2D.drawImage(SkyscrapersView.GRASS, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == SkyscrapersType.TENT) {
            graphics2D.drawImage(SkyscrapersView.TENT, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }*/
    	
    	graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(BACKGROUND_COLOR);
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
        graphics2D.setColor(BORDER_COLOR);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        int val = cell.getData();
        if (val != 0) {
            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(val);
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(value, xText, yText);
        }
    }
}
