package puzzle.nurikabe;

import controller.BoardController;
import controller.ElementController;
import ui.boardview.GridBoardView;

import java.awt.*;

public class NurikabeView extends GridBoardView
{

    public NurikabeView(Dimension gridSize)
    {
        super(new BoardController(), new NurikabeController(), gridSize);

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                Point location = new Point(k * elementSize.width, i * elementSize.height);
                NurikabeElement element = new NurikabeElement(new NurikabeCell(-2, null));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
    }
}
