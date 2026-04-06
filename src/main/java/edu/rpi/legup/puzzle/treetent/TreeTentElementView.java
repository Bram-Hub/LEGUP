package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.UIManager;

public class TreeTentElementView extends GridElementView {
    public TreeTentElementView(TreeTentCell cell) {
        super(cell);
    }

    /**
     * Draws on the given frame based on the type of the cell of the current puzzleElement
     *
     * @param graphics2D the frame to be drawn on
     */
    @Override
    public void drawElement(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        TreeTentType type = cell.getType();
        if (type == TreeTentType.UNKNOWN) {
            g.setColor(UIManager.getColor("TreeTent.unknown"));
            g.fill(new Rectangle2D.Double(location.x, location.y, size.width, size.height));
        } else if (type == TreeTentType.TREE) {
            g.drawImage(
                    TreeTentView.TREE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    null,
                    null);
        } else if (type == TreeTentType.GRASS) {
            g.drawImage(
                    TreeTentView.GRASS,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    null,
                    null);
        } else if (type == TreeTentType.TENT) {
            g.drawImage(
                    TreeTentView.TENT,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    null,
                    null);
        }
        g.dispose();
    }

    @Override
    public void drawBorder(Graphics2D graphics2D) {
        Graphics2D g = (Graphics2D) graphics2D.create();
        g.setColor(UIManager.getColor("TreeTent.borderColor"));
        g.setStroke(new BasicStroke(UIManager.getInt(
                ((TreeTentCell) puzzleElement).getType() == TreeTentType.UNKNOWN ?
                "TreeTent.unknownBorderWidth" : "TreeTent.knownBorderWidth")));
        g.drawRect(location.x, location.y, size.width, size.height);
        g.dispose();
    }
}
