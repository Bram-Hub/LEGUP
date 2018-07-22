package ui.boardview;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class PuzzlePieceView implements Shape
{
    private ArrayList<ElementView> elementViews;

    public PuzzlePieceView()
    {
        this.elementViews = new ArrayList<>();
    }

    /**
     * Draws the puzzle piece view on the screen
     *
     * @param graphics2D graphics2D object used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {

    }

    public ArrayList<ElementView> getElementViews()
    {
        return elementViews;
    }

    public void setElementViews(ArrayList<ElementView> elementViews)
    {
        this.elementViews = elementViews;
    }

    public void addPuzzleElement(ElementView element)
    {
        this.elementViews.add(element);
    }

    public void removePuzzleElement(ElementView element)
    {
        this.elementViews.remove(element);
    }

    @Override
    public Rectangle getBounds()
    {
        Rectangle bounds = new Rectangle();
        for(ElementView element: elementViews)
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
        for(ElementView element: elementViews)
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
        for(ElementView element: elementViews)
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
        for(ElementView element: elementViews)
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
        for(ElementView element: elementViews)
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
        for(ElementView element: elementViews)
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
        for(ElementView element: elementViews)
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
