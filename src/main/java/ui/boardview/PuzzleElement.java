package ui.boardview;

import model.gameboard.ElementData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class PuzzleElement implements Shape
{
    protected int index;
    protected Point location;
    protected Dimension size;
    protected ElementData data;
    private Color highLightColor;
    private Color hoverColor;
    private Color modifiedColor;
    private Color caseColor;
    private boolean isCaseRulePickable;
    private boolean isHover;
    private boolean isSelected;

    /**
     * PuzzleElement Constructor - creates a puzzle element view
     *
     * @param data element data to which the puzzle element uses to draw
     */
    public PuzzleElement(ElementData data)
    {
        this.data = data;
        this.highLightColor = new Color(0,0,128,255);
        this.hoverColor = new Color(0,0,255,255);
        this.modifiedColor = new Color(0, 255,0,255);
        this.caseColor = new Color(0, 0,140,100);
        this.isHover = false;
        this.isSelected = false;
        this.isCaseRulePickable = false;
    }

    /**
     * Determines if the specified point is within the PuzzleElement
     *
     * @param point point to check
     * @return true if the point is within the PuzzleElement, false otherwise
     */
    public boolean isWithinBounds(Point point)
    {
        return point.x >= location.x && point.x <= location.x + size.width &&
                point.y >= location.y && point.y <= location.y + size.height;
    }

    /**
     * Draws the puzzle element on the screen
     *
     * @param graphics2D graphics2D object used for drawing
     */
    public void draw(Graphics2D graphics2D)
    {
        drawElement(graphics2D);
        if(data.isGiven())
        {
            drawGiven(graphics2D);
        }
        if(isHover)
        {
            drawHover(graphics2D);
        }
        if(data.isModified())
        {
            drawModified(graphics2D);
        }
        if(data.isCaseApplicable())
        {
            drawCase(graphics2D);
        }
    }

    public void drawElement(Graphics2D graphics2D)
    {
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        graphics2D.setColor(Color.BLACK);
        FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
        String value = String.valueOf(data.getValueInt());
        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(String.valueOf(data.getValueInt()), xText, yText);
    }

    public void drawGiven(Graphics2D graphics2D)
    {
//        graphics2D.setColor(new Color(200,200,200));
//        graphics2D.fillRect(location.x, location.y, size.width, size.height);
    }

    public void drawHover(Graphics2D graphics2D)
    {
        graphics2D.setColor(highLightColor);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
    }

    public void drawModified(Graphics2D graphics2D)
    {
        graphics2D.setColor(modifiedColor);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
    }

    public void drawCase(Graphics2D graphics2D)
    {
        graphics2D.setColor(caseColor);
        graphics2D.fillRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
    }

    /**
     * Gets the index of the PuzzleElement
     *
     * @return index of the PuzzleElement
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index of the PuzzleElement
     *
     * @param index index of the PuzzleElement
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Gets the location of the PuzzleElement
     *
     * @return location of the PuzzleElement
     */
    public Point getLocation()
    {
        return location;
    }

    /**
     * Sets the location of the PuzzleElement
     *
     * @param location location of the PuzzleElement
     */
    public void setLocation(Point location)
    {
        this.location = location;
    }

    /**
     * Gets the dimension of the PuzzleElement
     *
     * @return dimension of the PuzzleElement
     */
    public Dimension getSize()
    {
        return size;
    }

    /**
     * Sets the dimension of the PuzzleElement
     *
     * @param size dimension of the PuzzleElement
     */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /**
     * Gets the ElementData associated with this view
     *
     * @return ElementData associated with this view
     */
    public ElementData getData()
    {
        return data;
    }

    /**
     * Sets the ElementData associated with this view
     *
     * @param data ElementData associated with this view
     */
    public void setData(ElementData data)
    {
        this.data = data;
    }

    /**
     * Gets the isCaseRulePickable field to determine if this PuzzleElement
     * should be highlighted in some way to indicate if it can be chosen by
     * the CaseRule
     *
     * @return true if the PuzzleElement can be chosen for the CaseRule, false otherwise
     */
    public boolean isCaseRulePickable()
    {
        return isCaseRulePickable;
    }

    /**
     * Sets the isCaseRulePickable field to determine if this PuzzleElement
     * should be highlighted in some way to indicate if it can be chosen by
     * the CaseRule
     *
     * @param isCaseRulePickable true if the PuzzleElement can be chosen for the CaseRule, false otherwise
     */
    public void setCaseRulePickable(boolean isCaseRulePickable)
    {
        this.isCaseRulePickable = isCaseRulePickable;
    }

    /**
     * Gets the high-light color
     *
     * @return high-light color
     */
    public Color getHighLightColor()
    {
        return highLightColor;
    }

    /**
     * Sets the high-light color
     *
     * @param highLightColor high-light color
     */
    public void setHighLightColor(Color highLightColor)
    {
        this.highLightColor = highLightColor;
    }

    /**
     * Gets whether the element is currently being hovered over
     *
     * @return true if the element is currently being hover over, false otherwise
     */
    public boolean isHover()
    {
        return isHover;
    }

    /**
     * Sets whether the element is being hover over
     *
     * @param hover true if the element is correctly being hover over, false otherwise
     */
    public void setHover(boolean hover)
    {
        isHover = hover;
    }

    /**
     * Gets whether the element is being selected
     *
     * @return tue if the element is currently selected, false otherwise
     */
    public boolean isSelected()
    {
        return isSelected;
    }

    /**
     * Sets whether the element is being selected
     *
     * @param selected tue if the element is currently selected, false otherwise
     */
    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public JMenuItem getSelectionMenuItem()
    {
        JMenuItem item = new JMenuItem(data.getValueInt() + "");
        return item;
    }

    @Override
    public boolean contains(double x, double y)
    {
        return x >= location.x && x <= location.x + size.width &&
                y >= location.y && y <= location.y + size.height;
    }

    @Override
    public boolean contains(Point2D point)
    {
        return contains(point.getX(), point.getY());
    }

    @Override
    public boolean intersects(double x, double y, double width, double height)
    {
        return (x + width >= location.x && x <= location.x + size.width) ||
                (y + height >= location.y && y <= location.y + size.height);
    }

    @Override
    public boolean intersects(Rectangle2D rectangle2D)
    {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override
    public boolean contains(double x, double y, double width, double height)
    {
        return (x + width >= location.x && x <= location.x + size.width) &&
                (y + height >= location.y && y <= location.y + size.height);
    }

    @Override
    public boolean contains(Rectangle2D rectangle2D)
    {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at)
    {
        return new Rectangle(location.x, location.y, size.width, size.height).getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return new Rectangle(location.x, location.y, size.width, size.height).getPathIterator(at, flatness);
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle(location.x, location.y, size.width, size.height);
    }

    @Override
    public Rectangle2D getBounds2D()
    {
        return new Rectangle(location.x, location.y, size.width, size.height);
    }
}
