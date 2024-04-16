package edu.rpi.legup.puzzle.minesweeper;

import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MinesweeperTileData(MinesweeperTileType type, int data) {

    public static final int UNSET_DATA = -2;
    public static final int BOMB_DATA = -1;
    public static final int EMPTY_DATA = 0;

    /**
     * Always has a type of {@link MinesweeperTileType#UNSET}, and a data value of {@value
     * UNSET_DATA}
     */
    private static final MinesweeperTileData UNSET =
            new MinesweeperTileData(MinesweeperTileType.UNSET, UNSET_DATA);

    /**
     * Always has a type of {@link MinesweeperTileType#BOMB}, and a data value of {@value BOMB_DATA}
     */
    private static final MinesweeperTileData BOMB =
            new MinesweeperTileData(MinesweeperTileType.BOMB, BOMB_DATA);

    /**
     * Always has a type of {@link MinesweeperTileType#EMPTY}, and a data value of {@value
     * EMPTY_DATA}
     */
    private static final MinesweeperTileData EMPTY =
            new MinesweeperTileData(MinesweeperTileType.EMPTY, EMPTY_DATA);

    /**
     * @param count how many bombs are near the flag
     * @return a new {@link MinesweeperTileData} with a {@link MinesweeperTileData#type} of {@link
     *     MinesweeperTileType#FLAG} and a {@link MinesweeperTileData#data} of {@code count}
     */
    @Contract(pure = true)
    public static @NotNull MinesweeperTileData flag(int count) {
        return new MinesweeperTileData(MinesweeperTileType.FLAG, count);
    }

    /**
     * @param data Determines what type of {@link MinesweeperTileData} to return.
     * @return If {@code data} is one of {@link MinesweeperTileData#UNSET_DATA}, {@link
     *     MinesweeperTileData#BOMB_DATA}, or {@link MinesweeperTileData#EMPTY_DATA}, it will return
     *     that data. If {@code data} is less than any of the values, or greater than 8, it will
     *     return {@link MinesweeperTileData#UNSET_DATA}. Otherwise, it returns {@link
     *     MinesweeperTileData#flag(int)} and passes {@code data} as the parameter.
     */
    @Contract(pure = true)
    public static @NotNull MinesweeperTileData fromData(int data) {
        return switch (data) {
            case UNSET_DATA -> unset();
            case BOMB_DATA -> bomb();
            case EMPTY_DATA -> empty();
            default -> {
                if (data <= -2 || data > 8) {
                    yield unset();
                }
                yield flag(data);
            }
        };
    }

    public static @NotNull MinesweeperTileData unset() {
        return UNSET;
    }

    public static @NotNull MinesweeperTileData bomb() {
        return BOMB;
    }

    public static @NotNull MinesweeperTileData empty() {
        return EMPTY;
    }

    public boolean isUnset() {
        return this.data == UNSET_DATA;
    }

    public boolean isBomb() {
        return this.data == BOMB_DATA;
    }

    public boolean isEmpty() {
        return this.data == EMPTY_DATA;
    }

    public boolean isFlag() {
        return this.data > 0 && this.data <= 8;
    }

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
