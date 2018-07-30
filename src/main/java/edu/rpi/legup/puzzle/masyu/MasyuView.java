package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MasyuView extends GridBoardView
{
    private List<MasyuLineView> lineViews;

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
        lineViews = new ArrayList<>();
        for(MasyuLine line : board.getLines())
        {
            MasyuLineView lineView = new MasyuLineView(line);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D)
    {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.drawBoard(graphics2D);
        lineViews.forEach(masyuLineView -> masyuLineView.draw(graphics2D));
    }

    /**
     * Called when the board element changed
     *
     * @param data element of the element that changed
     */
    @Override
    public void onBoardDataChanged(Element data)
    {
        if(data instanceof MasyuLine)
        {
            MasyuLineView lineView = new MasyuLineView((MasyuLine) data);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }
        repaint();
    }
}
