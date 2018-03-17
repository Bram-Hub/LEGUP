package puzzle.lightup;

import controller.BoardController;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.GridBoard;
import ui.boardview.GridBoardView;
import ui.boardview.PuzzleElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LightUpView extends GridBoardView
{
    static Image lightImage;

    public LightUpView(Dimension gridSize)
    {
        super(new BoardController(), new LightUpCellController(), gridSize);

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                Point location = new Point(k * elementSize.width, i * elementSize.height);
                LightUpElement element = new LightUpElement(new LightUpCell(-2, null));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
        try
        {
            lightImage = ImageIO.read(new File("images/lightup/light.png"));
        }
        catch(IOException e)
        {

        }
    }

    @Override
    public void updateBoard(Board board)
    {
        LightUpBoard lightUpBoard = (LightUpBoard)board;
        for(PuzzleElement element: puzzleElements)
        {
            element.setData(lightUpBoard.getElementData(element.getIndex()));
        }
        lightUpBoard.fillWithLight();
        repaint();
    }
}
