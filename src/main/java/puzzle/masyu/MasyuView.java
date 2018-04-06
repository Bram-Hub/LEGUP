package puzzle.masyu;

import controller.BoardController;
import ui.boardview.GridBoardView;

import java.awt.*;

public class MasyuView extends GridBoardView
{
    public MasyuView(Dimension gridSize)
    {
        super(new BoardController(), new MasyuController(), gridSize);

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                Point location = new Point(k * elementSize.width, i * elementSize.height);
                MasyuElement element = new MasyuElement(new MasyuCell(0, null));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D)
    {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.drawBoard(graphics2D);
    }
}
