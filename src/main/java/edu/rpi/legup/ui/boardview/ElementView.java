package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * ElementView represents a visual representation of a PuzzleElement. It handles drawing, selection,
 * hover states, and interaction with the PuzzleElement.
 */
public abstract class ElementView implements Shape {
    protected int index;
    protected Point location;
    protected Dimension size;
    protected PuzzleElement puzzleElement;
    private Color hoverColor;
    private Color modifiedColor;
    private Color caseColor;
    private Color invalidColor;
    private Color goalColor;
    private boolean showCasePicker;
    private boolean isCaseRulePickable;
    private boolean isHover;
    private boolean isSelected;

    /**
     * ElementView Constructor creates a puzzleElement view
     *
     * @param puzzleElement puzzleElement to which the view uses to draw
     */
    public ElementView(PuzzleElement puzzleElement) {
        this.puzzleElement = puzzleElement;
        this.hoverColor = new Color(0x79, 0x86, 0xCB, 200);
        this.modifiedColor = new Color(0x64, 0xDD, 0x17, 200);
        this.caseColor = new Color(0x1A, 0x23, 0x7E, 200);
        this.invalidColor = new Color(0xF4, 0x43, 0x36, 200);
        this.goalColor = new Color(0xAC, 0x94, 0x10, 200);
        this.isHover = false;
        this.isSelected = false;
        this.isCaseRulePickable = false;
    }

    /**
     * Determines if the specified point is within the ElementView
     *
     * @param point point to check
     * @return true if the point is within the ElementView, false otherwise
     */
    public boolean isWithinBounds(Point point) {
        return point.x >= location.x
                && point.x <= location.x + size.width
                && point.y >= location.y
                && point.y <= location.y + size.height;
    }

    /**
     * Draws the puzzle element on the screen
     *
     * @param graphics2D graphics2D object used for drawing
     */
    public void draw(Graphics2D graphics2D) {
        drawElement(graphics2D);
        if (puzzleElement.isGiven()) {
            drawGiven(graphics2D);
        }
        if (puzzleElement.isGoal()) {
            drawGoal(graphics2D);
        }
        if (puzzleElement.isModified()) {
            drawModified(graphics2D);
        }
        if (showCasePicker && isCaseRulePickable) {
            drawCase(graphics2D);
        }
        if (isHover) {
            drawHover(graphics2D);
        }
    }

    /**
     * Draws the basic element representation (e.g., border, text) on the provided Graphics2D
     * context.
     *
     * @param graphics2D the Graphics2D context to use for drawing
     */
    public void drawElement(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.draw(
                new Rectangle2D.Double(
                        location.x + 0.5f, location.y + 0.5f, size.width - 2, size.height - 2));

        graphics2D.setColor(Color.BLACK);
        FontMetrics metrics = graphics2D.getFontMetrics(graphics2D.getFont());
        String value = String.valueOf(puzzleElement.getData());
        int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
        int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
    }

    /**
     * Draws additional elements for given PuzzleElements (default implementation does nothing).
     * Overriden in some puzzle element views.
     *
     * @param graphics2D the Graphics2D context to use for drawing
     */
    public void drawGiven(Graphics2D graphics2D) {}

    /**
     * Draws a goal effect on the ElementView.
     *
     * @param graphics2D the Graphics2D context to use for drawing
     */
    public void drawGoal(Graphics2D graphics2D) {
        graphics2D.setColor(goalColor);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.draw(
                new Rectangle2D.Double(
                        location.x + 1.5f, location.y + 1.5f, size.width - 3, size.height - 3));
    }

    /**
     * Draws a hover effect on the ElementView.
     *
     * @param graphics2D the Graphics2D context to use for drawing
     */
    public void drawHover(Graphics2D graphics2D) {
        graphics2D.setColor(hoverColor);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.draw(
                new Rectangle2D.Double(
                        location.x + 1.5f, location.y + 1.5f, size.width - 3, size.height - 3));
    }

    /**
     * Draws a modified effect on the ElementView.
     *
     * @param graphics2D the Graphics2D context to use for drawing
     */
    public void drawModified(Graphics2D graphics2D) {
        graphics2D.setColor(puzzleElement.isValid() ? modifiedColor : invalidColor);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.draw(
                new Rectangle2D.Double(
                        location.x + 1.5f, location.y + 1.5f, size.width - 3, size.height - 3));
    }

    /**
     * Draws a case rule picker on the ElementView.
     *
     * @param graphics2D the Graphics2D context to use for drawing
     */
    public void drawCase(Graphics2D graphics2D) {
        graphics2D.setColor(caseColor);
        graphics2D.fill(
                new Rectangle2D.Double(
                        location.x + 1.5f, location.y + 1.5f, size.width - 3, size.height - 3));
    }

    /**
     * Creates an image representation of the ElementView.
     *
     * @return a BufferedImage of the ElementView
     */
    public BufferedImage getImage() {
        BufferedImage image =
                new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        drawElement(graphics2D);
        graphics2D.dispose();
        return image;
    }

    /**
     * Gets the index of the ElementView
     *
     * @return index of the ElementView
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the ElementView
     *
     * @param index index of the ElementView
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the location of the ElementView
     *
     * @return location of the ElementView
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the location of the ElementView
     *
     * @param location location of the ElementView
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Gets the dimension of the ElementView
     *
     * @return dimension of the ElementView
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Sets the dimension of the ElementView
     *
     * @param size dimension of the ElementView
     */
    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    public PuzzleElement getPuzzleElement() {
        return puzzleElement;
    }

    /**
     * Sets the PuzzleElement associated with this view
     *
     * @param data PuzzleElement associated with this view
     */
    public void setPuzzleElement(PuzzleElement data) {
        this.puzzleElement = data;
    }

    /**
     * Checks if the case picker should be shown for this ElementView
     *
     * @return true if the case picker should be shown, false otherwise
     */
    public boolean isShowCasePicker() {
        return showCasePicker;
    }

    /**
     * Sets whether the case picker should be shown for this ElementView
     *
     * @param showCasePicker true if the case picker should be shown, false otherwise
     */
    public void setShowCasePicker(boolean showCasePicker) {
        this.showCasePicker = showCasePicker;
    }

    /**
     * Gets the isCaseRulePickable field to determine if this ElementView should be highlighted in
     * some way to indicate if it can be chosen by the CaseRule
     *
     * @return true if the ElementView can be chosen for the CaseRule, false otherwise
     */
    public boolean isCaseRulePickable() {
        return isCaseRulePickable;
    }

    /**
     * Sets the isCaseRulePickable field to determine if this ElementView should be highlighted in
     * some way to indicate if it can be chosen by the CaseRule
     *
     * @param isCaseRulePickable true if the ElementView can be chosen for the CaseRule, false
     *     otherwise
     */
    public void setCaseRulePickable(boolean isCaseRulePickable) {
        this.isCaseRulePickable = isCaseRulePickable;
    }

    /**
     * Gets the high-light color
     *
     * @return high-light color
     */
    public Color getHoverColor() {
        return hoverColor;
    }

    /**
     * Sets the high-light color
     *
     * @param hoverColor high-light color
     */
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    /**
     * Gets whether the puzzleElement is currently being hovered over
     *
     * @return true if the puzzleElement is currently being hover over, false otherwise
     */
    public boolean isHover() {
        return isHover;
    }

    /**
     * Sets whether the puzzleElement is being hover over
     *
     * @param hover true if the puzzleElement is correctly being hover over, false otherwise
     */
    public void setHover(boolean hover) {
        isHover = hover;
    }

    /**
     * Gets whether the puzzleElement is being selected
     *
     * @return tue if the puzzleElement is currently selected, false otherwise
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets whether the puzzleElement is being selected
     *
     * @param selected tue if the puzzleElement is currently selected, false otherwise
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public JMenuItem getSelectionMenuItem() {
        JMenuItem item = new JMenuItem(puzzleElement.getData() + "");
        return item;
    }

    /**
     * Determines if the specified point (x, y) is within the bounds of this ElementView
     *
     * @param x the x-coordinate of the point to check
     * @param y the y-coordinate of the point to check
     * @return {@code true} if the point is within the bounds of this ElementView; {@code false}
     *     otherwise
     */
    @Override
    public boolean contains(double x, double y) {
        return x >= location.x
                && x <= location.x + size.width
                && y >= location.y
                && y <= location.y + size.height;
    }

    /**
     * Determines if the specified Point2D object is within the bounds of this ElementView
     *
     * @param point the Point2D object representing the point to check
     * @return {@code true} if the point is within the bounds of this ElementView; {@code false}
     *     otherwise
     */
    @Override
    public boolean contains(Point2D point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Determines if the specified rectangle defined by (x, y, width, height) intersects with the
     * bounds of this ElementView.
     *
     * @param x The x-coordinate of the rectangle to check
     * @param y The y-coordinate of the rectangle to check
     * @param width The width of the rectangle to check
     * @param height The height of the rectangle to check
     * @return {@code true} if the rectangle intersects with the bounds of this ElementView; {@code
     *     false} otherwise
     */
    @Override
    public boolean intersects(double x, double y, double width, double height) {
        return (x + width >= location.x && x <= location.x + size.width)
                || (y + height >= location.y && y <= location.y + size.height);
    }

    /**
     * Determines if the specified Rectangle2D object intersects with the bounds of this
     * ElementView.
     *
     * @param rectangle2D the Rectangle2D object representing the rectangle to check
     * @return {@code true} if the rectangle intersects with the bounds of this ElementView; {@code
     *     false} otherwise
     */
    @Override
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(
                rectangle2D.getX(),
                rectangle2D.getY(),
                rectangle2D.getWidth(),
                rectangle2D.getHeight());
    }

    /**
     * Determines if the specified rectangle defined by (x, y, width, height) is entirely contained
     * within the bounds of this ElementView
     *
     * @param x the x-coordinate of the rectangle to check
     * @param y the y-coordinate of the rectangle to check
     * @param width the width of the rectangle to check
     * @param height the height of the rectangle to check
     * @return {@code true} if the rectangle is entirely contained within the bounds of this
     *     ElementView; {@code false} otherwise
     */
    @Override
    public boolean contains(double x, double y, double width, double height) {
        return (x + width >= location.x && x <= location.x + size.width)
                && (y + height >= location.y && y <= location.y + size.height);
    }

    /**
     * Determines if the specified Rectangle2D object is entirely contained within the bounds of
     * this ElementView.
     *
     * @param rectangle2D the Rectangle2D object representing the rectangle to check
     * @return {@code true} if the rectangle is entirely contained within the bounds of this
     *     ElementView; {@code false} otherwise
     */
    @Override
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(
                rectangle2D.getX(),
                rectangle2D.getY(),
                rectangle2D.getWidth(),
                rectangle2D.getHeight());
    }

    /**
     * Returns an iterator over the path geometry of this ElementView. The iterator provides access
     * to the path's segments and their coordinates, which can be used for rendering or hit testing.
     *
     * @param at the AffineTransform to apply to the path geometry
     * @return a PathIterator that iterates over the path geometry of this ElementView
     */
    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new Rectangle(location.x, location.y, size.width, size.height).getPathIterator(at);
    }

    /**
     * Returns an iterator over the path geometry of this ElementView with the specified flatness.
     * The iterator provides access to the path's segments and their coordinates, which can be used
     * for rendering or hit testing.
     *
     * @param at the AffineTransform to apply to the path geometry
     * @param flatness the maximum distance that the line segments can deviate from the true path
     * @return a PathIterator that iterates over the path geometry of this ElementView
     */
    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new Rectangle(location.x, location.y, size.width, size.height)
                .getPathIterator(at, flatness);
    }

    /**
     * Returns the bounding rectangle of this ElementView
     *
     * @return a Rectangle representing the bounding box of this ElementView
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(location.x, location.y, size.width, size.height);
    }

    /**
     * Returns the bounding rectangle of this ElementView as a Rectangle2D
     *
     * @return a Rectangle2D representing the bounding box of this ElementView
     */
    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle(location.x, location.y, size.width, size.height);
    }
}
