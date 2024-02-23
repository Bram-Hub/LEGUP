package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;

public class MinesweeperController extends ElementController {

    public static @NotNull MinesweeperTileData getNewCellDataOnClick(
            @NotNull MouseEvent event,
            @NotNull MinesweeperTileData current
    ) {
        final int numberData = current.data();
        return switch (event.getButton()) {
            case MouseEvent.BUTTON1 -> MinesweeperTileData.fromData(numberData + 1);
            case MouseEvent.BUTTON2,
                    MouseEvent.BUTTON3 -> MinesweeperTileData.fromData(numberData - 1);
            default -> MinesweeperTileData.empty();
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void changeCell(
            @NotNull MouseEvent event,
            @SuppressWarnings("rawtypes") @NotNull PuzzleElement data
    ) {
        final MinesweeperCell cell = (MinesweeperCell) data;
        if (event.isControlDown()) {
            this.boardView.getSelectionPopupMenu().show(
                    boardView,
                    this.boardView.getCanvas().getX() + event.getX(),
                    this.boardView.getCanvas().getY() + event.getY()
            );
            return;
        }

        final MinesweeperTileData newData = getNewCellDataOnClick(event, cell.getData());
        data.setData(newData);
    }
}

