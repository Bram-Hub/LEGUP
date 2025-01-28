package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    /** Sets this cell's data to the value specified by {@link Element#getElementID()} */
    public void setType(@NotNull Element element, @NotNull MouseEvent event) {
        System.out.println("here");
        switch (element.getElementName()) {
            case "Mine" -> {
                this.setCellType(MinesweeperTileData.mine());
            }
            case "Empty" -> {
                this.setCellType(MinesweeperTileData.empty());
            }
            case "Number" -> {
                final int currentData = super.data.data();
                switch (event.getButton()) {
                    case MouseEvent.BUTTON1 -> {
                        if (currentData >= 8 || currentData <= 0) {
                            this.setCellType(MinesweeperTileData.number(1));
                            return;
                        }
                        this.setCellType(MinesweeperTileData.number(currentData + 1));
                    }
                    case MouseEvent.BUTTON2, MouseEvent.BUTTON3 -> {
                        if (currentData <= 1 || currentData >= 9) {
                            this.setCellType(MinesweeperTileData.number(8));
                            return;
                        }
                        this.setCellType(MinesweeperTileData.number(currentData - 1));
                    }
                }
            }
            default -> {
                this.setCellType(MinesweeperTileData.unset());
            }
        }
    }

    public void setCellType(MinesweeperTileData type) {
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
