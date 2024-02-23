package edu.rpi.legup.puzzle.minesweeper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record MinesweeperTileData(MinesweeperTileType type, int data) {

    public static final int UNSET_DATA = -2;
    public static final int BOMB_DATA = -1;
    public static final int EMPTY_DATA = 0;

    private static final MinesweeperTileData UNSET = new MinesweeperTileData(MinesweeperTileType.UNSET, UNSET_DATA);
    private static final MinesweeperTileData BOMB = new MinesweeperTileData(MinesweeperTileType.BOMB, BOMB_DATA);
    private static final MinesweeperTileData EMPTY = new MinesweeperTileData(MinesweeperTileType.EMPTY, EMPTY_DATA);

    public static @NotNull MinesweeperTileData flag(int count) {
        return new MinesweeperTileData(MinesweeperTileType.FLAG, count);
    }

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
