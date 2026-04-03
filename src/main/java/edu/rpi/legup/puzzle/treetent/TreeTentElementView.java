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
        TreeTentCell cell = (TreeTentCell) puzzleElement;
        TreeTentType type = cell.getType();
        graphics2D.setStroke(new BasicStroke(UIManager.getInt("TreeTent.knownBorderWidth")));
        if (type == TreeTentType.UNKNOWN) {
            graphics2D.setStroke(new BasicStroke(UIManager.getInt("TreeTent.unknownBorderWidth")));
            graphics2D.setColor(UIManager.getColor("TreeTent.unknown"));
            graphics2D.fill(new Rectangle2D.Double(location.x, location.y, size.width, size.height));
        } else if (type == TreeTentType.TREE) {
            graphics2D.drawImage(
                    TreeTentView.TREE,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    null,
                    null);
        } else if (type == TreeTentType.GRASS) {
            graphics2D.drawImage(
                    TreeTentView.GRASS,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    null,
                    null);
        } else if (type == TreeTentType.TENT) {
            graphics2D.drawImage(
                    TreeTentView.TENT,
                    location.x,
                    location.y,
                    size.width,
                    size.height,
                    null,
                    null);
        }
        graphics2D.setColor(UIManager.getColor("TreeTent.borderColor"));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }
}
