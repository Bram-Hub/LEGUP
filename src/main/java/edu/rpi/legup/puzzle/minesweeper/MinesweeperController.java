package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.event.MouseEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MinesweeperController extends ElementController {

    /**
     * If the button clicked was button 1, then {@link MinesweeperTileData#fromData(int)} is called
     * with a value of {@code current.data() + 1}. If the button clicked was button 2 or 3, then
     * {@link MinesweeperTileData#fromData(int)} is called with a value of {@code currentData() - 1}
     * Otherwise {@link MinesweeperTileData#empty()} is returned.
     *
     * @param event The user's click data
     * @param current The current data at the cell they clicked on
     * @return A different cell data depending on what the current data is
     */
    @Contract(pure = true)
    public static @NotNull MinesweeperTileData getNewCellDataOnClick(
            @NotNull MouseEvent event, @NotNull MinesweeperTileData current) {
        final int numberData = current.data();
        switch (event.getButton()) { // git?
            case MouseEvent.BUTTON1:
                if (numberData >= 1 && numberData <= 8) {
                    return MinesweeperTileData.fromData(numberData);
                }
                if (numberData == 0) {
                    return MinesweeperTileData.fromData(-2);
                }
                return MinesweeperTileData.fromData(numberData + 1);

            case MouseEvent.BUTTON2, MouseEvent.BUTTON3:
                if (numberData >= 1 && numberData <= 8) {
                    return MinesweeperTileData.fromData(numberData);
                }
                if (numberData == -2) {
                    return MinesweeperTileData.fromData(0);
                }
                return MinesweeperTileData.fromData(numberData - 1);
            default:
                return MinesweeperTileData.empty();
        }
    }

    /**
     * @see #getNewCellDataOnClick(MouseEvent, MinesweeperTileData)
     * @param event The user's click data
     * @param data The current data at the cell they clicked on
     */
    @Override
    @SuppressWarnings("unchecked")
    @Contract(pure = false)
    public void changeCell(
            @NotNull MouseEvent event, @SuppressWarnings("rawtypes") @NotNull PuzzleElement data) {
        final MinesweeperCell cell = (MinesweeperCell) data;
        if (event.isControlDown()) {
            this.boardView
                    .getSelectionPopupMenu()
                    .show(
                            boardView,
                            this.boardView.getCanvas().getX() + event.getX(),
                            this.boardView.getCanvas().getY() + event.getY());
            return;
        }

        final MinesweeperTileData newData = getNewCellDataOnClick(event, cell.getData());
        data.setData(newData);
    }
}
