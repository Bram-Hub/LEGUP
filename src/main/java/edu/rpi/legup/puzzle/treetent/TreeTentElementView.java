package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class TreeTentElementView extends GridElementView
{
    public TreeTentElementView(TreeTentCell cell)
    {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D)
    {
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        TreeTentType type = cell.getType();
        if(type == TreeTentType.UNKNOWN)
        {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(location.x, location.y, size.width, size.height);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        else if(type == TreeTentType.TREE)
        {
            graphics2D.drawImage(TreeTentView.TREE, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        else if(type == TreeTentType.GRASS)
        {
            graphics2D.drawImage(TreeTentView.GRASS, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
        else if(type == TreeTentType.TENT)
        {
            graphics2D.drawImage(TreeTentView.TENT, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
