package puzzle.treetent;

import ui.boardview.PuzzleElement;

import java.awt.*;

public class TreeTentLineView extends PuzzleElement
{
    private final Color LINE_COLOR = Color.GREEN;
    private final Stroke LINE_STROKE = new BasicStroke(2);

    public TreeTentLineView(TreeTentLine line)
    {
        super(line);
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        TreeTentLine line = (TreeTentLine)data;
        Point p1 = line.getC1().getLocation();
        Point p2 = line.getC2().getLocation();
        int x1 = (p1.x + 1) * size.width + size.width / 2;
        int y1 = (p1.y + 1) * size.height + size.height / 2;

        int x2 = (p2.x + 1) * size.width + size.width / 2;
        int y2 = (p2.y + 1) * size.height + size.height / 2;

        graphics2D.setColor(LINE_COLOR);
        graphics2D.setStroke(LINE_STROKE);
        graphics2D.drawLine(x1, y1, x2, y2);
    }
}
