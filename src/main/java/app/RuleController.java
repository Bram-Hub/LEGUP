package app;

import model.rules.Rule;
import model.rules.Tree;
import ui.rulesview.RuleButton;
import ui.rulesview.RuleFrame;
import ui.rulesview.RulePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class RuleController extends Controller implements ActionListener, Scrollable
{
    protected static final int SCROLLABLE_INCREMENT = 16;

    protected RulePanel rulePanel;
    protected Object lastSource;

    /**
     * RuleController Constructor - creates a controller object to listen
     * to ui events from a RulePanelView
     */
    public RuleController()
    {
        super();
    }

    public void setViewer(RulePanel rulePanel)
    {
        this.rulePanel = rulePanel;
    }

    /**
     * Button Pressed event - occurs a when a rule button has been pressed
     *
     * @param rule rule of the button that was pressed
     */
    public void buttonPressed(Rule rule)
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        tree.verifySelected(rule);
    }

    /**
     * Action Performed event - occurs when a rule button has been pressed
     *
     * @param e action event object
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        lastSource = e.getSource();
        RuleButton button = (RuleButton) lastSource;
        buttonPressed(button.getRule());
    }

    /**
     * Gets the preferred scrollable viewport size
     *
     * @return preferred scrollable viewport size
     */
    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
        return rulePanel.getPreferredSize();
    }

    /**
     * Gets the scrollable unit increment
     *
     * @param visibleRect
     * @param orientation
     * @param direction
     * @return
     */
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return SCROLLABLE_INCREMENT;
    }

    /**
     * Gets the scrollable block increment
     *
     * @param visibleRect
     * @param orientation
     * @param direction
     * @return
     */
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        return SCROLLABLE_INCREMENT;
    }

    /**
     * Gets the scrollable tracks viewport width
     *
     * @return
     */
    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        return rulePanel.getParent().getHeight() > rulePanel.getPreferredSize().height;
    }

    /**
     * Gets the scrollable tracks viewport height
     *
     * @return
     */
    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        return true;
    }

    /**
     * Mouse Clicked event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    /**
     * Mouse Pressed event - sets the cursor to the move cursor and stores
     * info for possible panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    /**
     * Mouse Released event - sets the cursor back to the default cursor and reset
     * info for panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    /**
     * Mouse Entered event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    /**
     * Mouse Exited event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    /**
     * Mouse Dragged event - adjusts the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    /**
     * Mouse Moved event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {

    }

    /**
     * Mouse Wheel Moved event - zooms in on the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        viewer.zoom(e.getWheelRotation(), e.getPoint());
    }
}
