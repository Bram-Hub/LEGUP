package ui.boardview;

import java.awt.*;
import java.util.ArrayList;

public class ElementSelection
{
    private ArrayList<PuzzleElement> selection;
    private PuzzleElement hover;
    private Point mousePoint;

    public ElementSelection()
    {
        this.selection = new ArrayList<>();
        this.hover = null;
        this.mousePoint = null;
    }

    public ArrayList<PuzzleElement> getSelection()
    {
        return selection;
    }

    public PuzzleElement getFirstSelection()
    {
        return selection.size() == 0 ? null : selection.get(0);
    }

    public void toggleSelection(PuzzleElement puzzleElement)
    {
        if(selection.contains(puzzleElement))
        {
            selection.remove(puzzleElement);
            puzzleElement.setSelected(false);
        }
        else
        {
            selection.add(puzzleElement);
            puzzleElement.setSelected(true);
        }
    }

    public void newSelection(PuzzleElement puzzleElement)
    {
        clearSelection();
        selection.add(puzzleElement);
        puzzleElement.setSelected(true);
    }

    public void clearSelection()
    {
        for(PuzzleElement puzzleElement : selection)
        {
            puzzleElement.setSelected(false);
        }
        selection.clear();
    }

    public PuzzleElement getHover()
    {
        return hover;
    }

    public void newHover(PuzzleElement newHovered)
    {
        newHovered.setHover(true);
        if(hover != null)
        {
            hover.setHover(false);
        }
        hover = newHovered;
    }

    public void clearHover()
    {
        if(hover != null)
        {
            hover.setHover(false);
            hover = null;
        }
    }

    public Point getMousePoint()
    {
        return mousePoint;
    }

    public void setMousePoint(Point point)
    {
        this.mousePoint = point;
    }
}
