package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TreeTentElementView extends GridElementView {
    public TreeTentElementView(TreeTentCell cell) {
        super(cell);
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        TreeTentType type = cell.getType();
        graphics2D.setStroke(new BasicStroke(0));
        if (type == TreeTentType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fill(new Rectangle2D.Double(location.x + 0.5f, location.y + 0.5f, size.width - 1, size.height - 1));
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(new Rectangle2D.Double(location.x + 0.5f, location.y + 0.5f, size.width - 1, size.height - 1));
        } else if (type == TreeTentType.TREE) {
            graphics2D.drawImage(TreeTentView.TREE, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == TreeTentType.GRASS) {
            graphics2D.drawImage(TreeTentView.GRASS, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        } else if (type == TreeTentType.TENT) {
            graphics2D.drawImage(TreeTentView.TENT, location.x, location.y, size.width, size.height, null, null);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(location.x, location.y, size.width, size.height);
        }
    }
}
