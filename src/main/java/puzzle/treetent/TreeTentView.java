package puzzle.treetent;

import controller.BoardController;
import ui.boardview.GridBoardView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TreeTentView extends GridBoardView
{
    static Image TREE, GRASS, TENT;

    public TreeTentView(Dimension gridSize)
    {
        super(new BoardController(), new TreeTentController(), gridSize);

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                Point location = new Point(k * elementSize.width, i * elementSize.height);
                TreetTentElement element = new TreetTentElement(new TreeTentCell(0, null));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }

        try
        {
            TREE = ImageIO.read(new File("images/treetent/tree.png"));
            GRASS = ImageIO.read(new File("images/treetent/grass.png"));
            TENT = ImageIO.read(new File("images/treetent/tent.png"));
        }
        catch(IOException e)
        {

        }
    }
}
