package puzzle.lightup;

import controller.BoardController;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.GridBoard;
import ui.boardview.DataSelectionView;
import ui.boardview.GridBoardView;
import ui.boardview.PuzzleElement;
import ui.boardview.SelectionItemView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
            lightImage = ImageIO.read(ClassLoader.getSystemClassLoader().getResource("images/lightup/light.png"));
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

    public DataSelectionView getSelectionPopupMenu()
    {
        DataSelectionView selectionView = new DataSelectionView(elementController);
        GridLayout layout = new GridLayout(3,1);
        selectionView.setLayout(layout);

        Dimension iconSize = new Dimension(32,32);
        Point loc = new Point(0,0);

        LightUpElement element1 = new LightUpElement(new LightUpCell(-2, null));
        element1.setSize(iconSize);
        element1.setLocation(loc);
        SelectionItemView item1 = new SelectionItemView(element1.getData(), new ImageIcon(element1.getImage()));
        item1.addActionListener(elementController);
        item1.setHorizontalTextPosition(SwingConstants.CENTER);
        selectionView.add(item1);

        LightUpElement element2 = new LightUpElement(new LightUpCell(-4, null));
        element2.setSize(iconSize);
        element2.setLocation(loc);
        SelectionItemView item2 = new SelectionItemView(element2.getData(), new ImageIcon(element2.getImage()));
        item2.addActionListener(elementController);
        item2.setHorizontalTextPosition(SwingConstants.CENTER);
        selectionView.add(item2);

        LightUpElement element3 = new LightUpElement(new LightUpCell(-3, null));
        element3.setSize(iconSize);
        element3.setLocation(loc);
        SelectionItemView item3 = new SelectionItemView(element3.getData(), new ImageIcon(element3.getImage()));
        item3.addActionListener(elementController);
        item3.setHorizontalTextPosition(SwingConstants.CENTER);
        selectionView.add(item3);

        return selectionView;
    }
}
