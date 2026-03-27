package edu.rpi.legup.puzzle.minesweeper;

import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MinesweeperTileData(MinesweeperTileType type, int data) {

    public static final int UNSET_DATA = -2;
    public static final int MINE_DATA = -1;
    public static final int EMPTY_DATA = 0;

    /**
     * Always has a type of {@link MinesweeperTileType#UNSET}, and a data value of {@value
     * UNSET_DATA}
     */
    private static final MinesweeperTileData UNSET =
            new MinesweeperTileData(MinesweeperTileType.UNSET, UNSET_DATA);

    /**
     * Always has a type of {@link MinesweeperTileType#MINE}, and a data value of {@value MINE_DATA}
     */
    private static final MinesweeperTileData MINE =
            new MinesweeperTileData(MinesweeperTileType.MINE, MINE_DATA);

    /**
     * Always has a type of {@link MinesweeperTileType#EMPTY}, and a data value of {@value
     * EMPTY_DATA}
     */
    private static final MinesweeperTileData EMPTY =
            new MinesweeperTileData(MinesweeperTileType.EMPTY, EMPTY_DATA);

    /**
     * @param count how many mines are near the number
     * @return a new {@link MinesweeperTileData} with a {@link MinesweeperTileData#type} of {@link
     *     MinesweeperTileType#NUMBER} and a {@link MinesweeperTileData#data} of {@code count}
     */
    @Contract(pure = true)
    public static @NotNull MinesweeperTileData number(int count) {
        return new MinesweeperTileData(MinesweeperTileType.NUMBER, count);
    }

    /**
     * @param data Determines what type of {@link MinesweeperTileData} to return.
     * @return If {@code data} is one of {@link MinesweeperTileData#UNSET_DATA}, {@link
     *     MinesweeperTileData#MINE_DATA}, or {@link MinesweeperTileData#EMPTY_DATA}, it will return
     *     that data. If {@code data} is less than any of the values, or greater than 8, it will
     *     return {@link MinesweeperTileData#UNSET_DATA}. Otherwise, it returns {@link
     *     MinesweeperTileData#number(int)} and passes {@code data} as the parameter.
     */
    @Contract(pure = true)
    public static @NotNull MinesweeperTileData fromData(int data) {
        return switch (data) {
            case UNSET_DATA -> unset();
            case MINE_DATA -> mine();
            case EMPTY_DATA -> empty();
            default -> {
                if (data <= -2) {
                    yield unset();
                }
                yield number(data);
            }
        };
    }

    /**
     *
     * @return the unset status
     */
    public static @NotNull MinesweeperTileData unset() {
        return UNSET;
    }
    /**
     *
     * @return the mine status
     */

    public static @NotNull MinesweeperTileData mine() {
        return MINE;
    }
    /**
     *
     * @return the empty status
     */

    public static @NotNull MinesweeperTileData empty() {
        return EMPTY;
    }

    /**
     *
     * @return true if the tile is unset
     */
    public boolean isUnset() {
        return this.data == UNSET_DATA;
    }
    /**
     *
     * @return  true if the tile is a mine
     */
    public boolean isMine() {
        return this.data == MINE_DATA;
    }
    /**
     *
     * @return true if the tile is empty
     */
    public boolean isEmpty() {
        return this.data == EMPTY_DATA;
    }
    /**
     *
     * @return true if the tile has a number in it
     */
    public boolean isNumber() {
        return this.data > 0 && this.data <= 8;
    }

    /**
     * compares two tile objects to see if they are equal
     *
     * @param o the object being compared against
     * @return true if the two tile are equal to each other
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinesweeperTileData that = (MinesweeperTileData) o;
        return data == that.data && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data);
    }
}
