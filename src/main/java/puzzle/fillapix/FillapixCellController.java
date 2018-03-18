package puzzle.fillapix;

import controller.ElementController;
import model.gameboard.ElementData;

import java.awt.event.MouseEvent;

public class FillapixCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, ElementData data) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                int value = data.getValueInt();
                if (FillapixCell.isUnknown(value)) {
                    data.setValueInt(value+FillapixCell.BLACK);
                } else if (FillapixCell.isBlack(value)) {
                    data.setValueInt(value+FillapixCell.WHITE);
                } else if (FillapixCell.isWhite(value)) {
                    data.setValueInt(value+FillapixCell.UNKNOWN);
                } else {
                    // there must be an error, this block should never be entered because all cells have a state
                }
            }
        }
    }
}