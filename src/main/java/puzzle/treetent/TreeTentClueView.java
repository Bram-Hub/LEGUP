package puzzle.treetent;

import ui.boardview.PuzzleElement;

import java.awt.*;

public class TreeTentClueView extends PuzzleElement
{

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public TreeTentClueView(TreeTentClue clue)
    {
        super(clue);
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setColor(FONT_COLOR);
        graphics2D.setFont(FONT);
        FontMetrics metrics = graphics2D.getFontMetrics(FONT);
        String value;

        TreeTentClue clue = (TreeTentClue)data;
        switch(clue.getType())
        {
            case CLUE_NORTH:
                value = String.valueOf(data.getValueInt() + 1);
                break;
            case CLUE_EAST:
                value = String.valueOf(data.getValueInt());
                break;
            case CLUE_SOUTH:
                value = String.valueOf(data.getValueInt());
                break;
            case CLUE_WEST:
                value = TreeTentClue.colNumToString(data.getValueInt() + 1);
                break;
            default:
                value = "";
        }

        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(value, xText, yText);
    }
}
