package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.ui.boardview.GridBoardView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TreeTentView extends GridBoardView
{
    static Image TREE, GRASS, TENT;
    static
    {
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

    private ArrayList<TreeTentLineView> lineViews;

    private ArrayList<TreeTentClueView> northClues;
    private ArrayList<TreeTentClueView> eastClues;
    private ArrayList<TreeTentClueView> southClues;
    private ArrayList<TreeTentClueView> westClues;

    public TreeTentView(TreeTentBoard board)
    {
        super(new BoardController(), new TreeTentController(), board.getDimension());

        this.lineViews = new ArrayList<>();

        this.northClues = new ArrayList<>();
        this.eastClues = new ArrayList<>();
        this.southClues = new ArrayList<>();
        this.westClues = new ArrayList<>();

        for(Element element : board.getElementData())
        {
            TreeTentCell cell = (TreeTentCell)element;
            Point loc = cell.getLocation();
            TreeTentElementView elementView = new TreeTentElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point((loc.x + 1)* elementSize.width, (loc.y + 1) * elementSize.height));
            elementViews.add(elementView);
        }

        for(TreeTentLine line : board.getLines())
        {
            TreeTentLineView lineView = new TreeTentLineView(line);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }

        for(int i = 0; i < gridSize.height; i++)
        {
            TreeTentClueView row = new TreeTentClueView(new TreeTentClue(i, i, TreeTentType.CLUE_WEST));
            row.setLocation(new Point(0, (i + 1) * elementSize.height));
            row.setSize(elementSize);

            TreeTentClueView clue = new TreeTentClueView(board.getEast().get(i));
            clue.setLocation(new Point((gridSize.height + 1) * elementSize.height, (i + 1) * elementSize.height));
            clue.setSize(elementSize);

            westClues.add(row);
            eastClues.add(clue);
        }

        for(int i = 0; i < gridSize.width; i++)
        {
            TreeTentClueView col = new TreeTentClueView(new TreeTentClue(i, i, TreeTentType.CLUE_NORTH));
            col.setLocation(new Point((i + 1) * elementSize.width, 0));
            col.setSize(elementSize);

            TreeTentClueView clue = new TreeTentClueView(board.getSouth().get(i));
            clue.setLocation(new Point( (i + 1) * elementSize.width, (gridSize.width + 1) * elementSize.width));
            clue.setSize(elementSize);

            northClues.add(col);
            southClues.add(clue);
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
    public void onBoardChanged(Board board)
    {
        super.onBoardChanged(board);
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
