package puzzle.treetent;

import controller.BoardController;
import model.gameboard.Board;
import ui.boardview.GridBoardView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TreeTentView extends GridBoardView
{
    static Image TREE, GRASS, TENT;

    private ArrayList<TreeTentLineView> lineViews;

    private ArrayList<TreeTentClueView> northClues;
    private ArrayList<TreeTentClueView> eastClues;
    private ArrayList<TreeTentClueView> southClues;
    private ArrayList<TreeTentClueView> westClues;

    public TreeTentView(Dimension gridSize)
    {
        super(new BoardController(), new TreeTentController(), gridSize);

        this.lineViews = new ArrayList<>();

        this.northClues = new ArrayList<>();
        this.eastClues = new ArrayList<>();
        this.southClues = new ArrayList<>();
        this.westClues = new ArrayList<>();

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                Point location = new Point((k + 1) * elementSize.width, (i + 1) * elementSize.height);
                TreeTentElement element = new TreeTentElement(new TreeTentCell(0, null));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }

        for(int i = 0; i < gridSize.height; i++)
        {
            TreeTentClueView row = new TreeTentClueView(null);
            row.setLocation(new Point(0, (i + 1) * elementSize.height));
            row.setSize(elementSize);

            TreeTentClueView clue = new TreeTentClueView(null);
            clue.setLocation(new Point((gridSize.height + 1) * elementSize.height, (i + 1) * elementSize.height));
            clue.setSize(elementSize);

            westClues.add(row);
            eastClues.add(clue);
        }

        for(int i = 0; i < gridSize.width; i++)
        {
            TreeTentClueView col = new TreeTentClueView(null);
            col.setLocation(new Point((i + 1) * elementSize.width, 0));
            col.setSize(elementSize);

            TreeTentClueView clue = new TreeTentClueView(null);
            clue.setLocation(new Point( (i + 1) * elementSize.width, (gridSize.width + 1) * elementSize.width));
            clue.setSize(elementSize);

            northClues.add(col);
            southClues.add(clue);
        }

        try
        {
            TREE = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/treetent/tree.png"));
            GRASS = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/treetent/grass.png"));
            TENT = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/treetent/tent.png"));
        }
        catch(IOException e)
        {

        }
    }

    public ArrayList<TreeTentLineView> getLineViews()
    {
        return lineViews;
    }

    public ArrayList<TreeTentClueView> getNorthClues()
    {
        return northClues;
    }

    public ArrayList<TreeTentClueView> getEastClues()
    {
        return eastClues;
    }

    public ArrayList<TreeTentClueView> getSouthClues()
    {
        return southClues;
    }

    public ArrayList<TreeTentClueView> getWestClues()
    {
        return westClues;
    }

    @Override
    protected Dimension getProperSize()
    {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = (gridSize.width + 2) * elementSize.width;
        boardViewSize.height = (gridSize.height + 2) * elementSize.height;
        return boardViewSize;
    }

    @Override
    public void updateBoard(Board board)
    {
        super.updateBoard(board);
        TreeTentBoard treeTentBoard = (TreeTentBoard)board;

        lineViews.clear();
        for(TreeTentLine line : treeTentBoard.getLines())
        {
            TreeTentLineView lineView = new TreeTentLineView(line);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D)
    {
        super.drawBoard(graphics2D);

        for(TreeTentLineView view : lineViews)
        {
            view.draw(graphics2D);
        }

        for(TreeTentClueView clueView : northClues)
        {
            clueView.draw(graphics2D);
        }

        for(TreeTentClueView clueView : eastClues)
        {
            clueView.draw(graphics2D);
        }

        for(TreeTentClueView clueView : southClues)
        {
            clueView.draw(graphics2D);
        }

        for(TreeTentClueView clueView : westClues)
        {
            clueView.draw(graphics2D);
        }
    }
}
