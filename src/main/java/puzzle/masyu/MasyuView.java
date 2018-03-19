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
}
