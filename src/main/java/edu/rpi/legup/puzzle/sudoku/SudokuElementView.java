package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class SudokuElementView extends GridElementView
{
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = new Color(0x212121);
    private static final Color BORDER_COLOR = new Color(0x424242);
    private static final Color GIVEN_COLOR = new Color(0x75,0x75,0x75, 0x80);
    private static final Color BACKGROUND_COLOR = new Color(0xEEEEEE);

    public SudokuElementView(GridCell cell)
    {
        super(cell);
    }

    /**
     * Gets the PuzzleElement associated with this view
     *
     * @return PuzzleElement associated with this view
     */
    @Override
    public SudokuCell getPuzzleElement()
    {
        return (SudokuCell) super.getPuzzleElement();
    }

//    /**
//     * Draws the sudoku puzzleElement to the screen
//     *
//     * @param graphics2D graphics object
//     */
//    public void draw(Graphics2D graphics2D)
//    {
//        SudokuCell cell = (SudokuCell) puzzleElement;
//        if(cell.isGiven())
//        {
//            graphics2D.setColor(new Color(200,200,200));
//            graphics2D.fillRect(location.x, location.y, size.width, size.height);
//        }
//
//        if(isHover())
//        {
//            graphics2D.setColor(new Color(63,101,244));
//            graphics2D.setStroke(new BasicStroke(2));
//            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
//        }
//        if(puzzleElement.isModified())
//        {
//            graphics2D.setStroke(new BasicStroke(2));
//            graphics2D.setColor(Color.GREEN);
//            graphics2D.drawRect(location.x + 1, location.y + 1, size.width - 2, size.height - 2);
//        }
//
//        graphics2D.setStroke(new BasicStroke(1));
//        graphics2D.setColor(Color.BLACK);
//        graphics2D.drawRect(location.x, location.y, size.width, size.height);
//
//        if(getPuzzleElement().getData() != 0)
//        {
//            graphics2D.setColor(FONT_COLOR);
//            graphics2D.setFont(FONT);
//            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
//            String value = String.valueOf(puzzleElement.getData());
//            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
//            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
//            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
//        }
//    }

    @Override
    public void drawGiven(Graphics2D graphics2D)
    {
        graphics2D.setColor(GIVEN_COLOR);
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
    }

    @Override
    public void drawElement(Graphics2D graphics2D)
    {
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(BACKGROUND_COLOR);
        graphics2D.fillRect(location.x, location.y, size.width, size.height);
        graphics2D.setColor(BORDER_COLOR);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);

        if(getPuzzleElement().getData() != 0)
        {
            graphics2D.setColor(FONT_COLOR);
            graphics2D.setFont(FONT);
            FontMetrics metrics = graphics2D.getFontMetrics(FONT);
            String value = String.valueOf(puzzleElement.getData());
            int xText = location.x + (size.width - metrics.stringWidth(value)) / 2;
            int yText = location.y + ((size.height - metrics.getHeight()) / 2) + metrics.getAscent();
            graphics2D.drawString(String.valueOf(puzzleElement.getData()), xText, yText);
        }
    }
}
