package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;

import edu.rpi.legup.puzzle.masyu.MasyuType;
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
        switch (element.getElementID()) {
            case MinesweeperElementIdentifiers.MINE -> {
                this.data = MinesweeperTileData.mine();
                break;
            }
            case MinesweeperElementIdentifiers.NUMBER -> {
                final int currentData = super.data.data();
                switch (event.getButton()) {
                    case MouseEvent.BUTTON1 -> {
                        if (currentData >= 8) {
                            this.data = MinesweeperTileData.empty();
                            return;
                        }
                        this.data = MinesweeperTileData.number(currentData + 1);
                        return;
                    }
                    case MouseEvent.BUTTON2, MouseEvent.BUTTON3 -> {
                        if (currentData <= 0) {
                            this.data = MinesweeperTileData.empty();
                            return;
                        }
                        this.data = MinesweeperTileData.number(currentData - 1);
                        return;
                    }
                }
            }
            default -> {
                this.data = MinesweeperTileData.empty();
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
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return !(data == MinesweeperTileData.unset());}

}
