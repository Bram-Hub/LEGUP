package puzzle.masyu;

import controller.BoardController;
import model.gameboard.Element;
import ui.boardview.GridBoardView;

import java.awt.*;

public class MasyuView extends GridBoardView
{
    public MasyuView(MasyuBoard board)
    {
        super(new BoardController(), new MasyuController(), board.getDimension());

        for(Element element : board.getElementData())
        {
            MasyuCell cell = (MasyuCell)element;
            Point loc = cell.getLocation();
            MasyuElementView elementView = new MasyuElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D)
    {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.drawBoard(graphics2D);
    }
}
