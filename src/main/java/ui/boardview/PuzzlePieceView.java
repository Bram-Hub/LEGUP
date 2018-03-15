package ui.boardview;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class PuzzlePieceView implements Shape
{
    private ArrayList<PuzzleElement> puzzleElements;

    public PuzzlePieceView()
    {
        this.puzzleElements = new ArrayList<>();
    }

    /**
     * Draws the puzzle piece view on the screen
     *
     * @param graphics2D graphics2D object used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {

    }

    public ArrayList<PuzzleElement> getPuzzleElements()
    {
        return puzzleElements;
    }

    public void setPuzzleElements(ArrayList<PuzzleElement> puzzleElements)
    {
        this.puzzleElements = puzzleElements;
    }

    public void addPuzzleElement(PuzzleElement element)
    {
        this.puzzleElements.add(element);
    }

    public void removePuzzleElement(PuzzleElement element)
    {
        this.puzzleElements.remove(element);
    }

    @Override
    public Rectangle getBounds()
    {
        Rectangle bounds = new Rectangle();
        for(PuzzleElement element: puzzleElements)
        {
            bounds.union(element.getBounds());
        }
        return null;
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return getBounds();
    }

    @Override
    public boolean contains(double x, double y)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.contains(x, y))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Point2D p)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.contains(p))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.intersects(x, y, w, h))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.intersects(r))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(double x, double y, double w, double h)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.contains(x, y, w, h))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.contains(r))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at)
    {
        return null;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return null;
    }
}
