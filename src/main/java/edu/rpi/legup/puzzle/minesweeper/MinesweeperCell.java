package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MinesweeperCell extends GridCell<MinesweeperTileData> {

    public MinesweeperCell(@NotNull MinesweeperTileData value, @NotNull Point location) {
        super(value, location);
    }

    public @NotNull MinesweeperTileType getTileType() {
        return super.data.type();
    }

    public @NotNull int getTileNumber() {
        return super.data.data();
    }

    @Override
    @Contract(pure = false)
    /**
     * Sets this cell's data to the value specified by {@link Element#getElementID()}
     */
    public void setType(@NotNull Element element, @NotNull MouseEvent event) {
        switch (element.getElementID()) {
            case MinesweeperElementIdentifiers.BOMB -> {
                this.data = MinesweeperTileData.bomb();
                break;
            }
            case MinesweeperElementIdentifiers.FLAG -> {
                final int currentData = super.data.data();
                switch (event.getButton()) {
                    case MouseEvent.BUTTON1 -> {
                        if (currentData >= 8) {
                            this.data = MinesweeperTileData.empty();
                            return;
                        }
                        this.data = MinesweeperTileData.flag(currentData + 1);
                        return;
                    }
                    case MouseEvent.BUTTON2, MouseEvent.BUTTON3 -> {
                        if (currentData <= 0) {
                            this.data = MinesweeperTileData.empty();
                            return;
                        }
                        this.data = MinesweeperTileData.flag(currentData - 1);
                        return;
                    }
                }
            }
            default -> {
                this.data = MinesweeperTileData.empty();
            }
        }
    }

    public void setCellType(MinesweeperTileData type){
        this.data = type;
    }

    @Override
    @Contract(pure = true)
    public @NotNull MinesweeperCell copy() {
        MinesweeperCell copy = new MinesweeperCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
