package puzzle.nurikabe;

import controller.BoardController;
import model.gameboard.Element;
import ui.boardview.GridBoardView;

import java.awt.*;

public class NurikabeView extends GridBoardView
{

    public NurikabeView(NurikabeBoard board)
    {
        super(new BoardController(), new NurikabeController(), board.getDimension());

        for(Element element : board.getElementData())
        {
            NurikabeCell cell = (NurikabeCell)element;
            Point loc = cell.getLocation();
            NurikabeElementView elementView = new NurikabeElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}
