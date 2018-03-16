package puzzles.fillapix;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static puzzles.fillapix.CellState.BLACK;
import static puzzles.fillapix.CellState.UNKNOWN;
import static puzzles.fillapix.CellState.WHITE;

public class FillapixCellController implements MouseListener {
    private FillapixView fillapixView;

    public FillapixCellController(FillapixView fillapixView) {this.fillapixView = fillapixView; }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        FillapixElement element = (FillapixElement) e.getSource();
        element.getData().setValueInt(5);
        switch (element.fillapixCell.getState()) {
            case UNKNOWN: element.fillapixCell.setState(BLACK);
                break;
            case BLACK: element.fillapixCell.setState(WHITE);
                break;
            case WHITE: element.fillapixCell.setState(UNKNOWN);
                break;
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}