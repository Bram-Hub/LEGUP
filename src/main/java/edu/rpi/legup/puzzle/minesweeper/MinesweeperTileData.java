package edu.rpi.legup.puzzle.minesweeper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class MinesweeperTileData {

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
        switch (data) {
            case UNSET_DATA: return unset();
            case BOMB_DATA: return bomb();
            case EMPTY_DATA: return empty();
            default: return flag(data);
        }
    }

    public static @NotNull MinesweeperTileData unset() { return UNSET; };
    public static @NotNull MinesweeperTileData bomb() {
        return BOMB;
    }

    public static @NotNull MinesweeperTileData empty() {
        return EMPTY;
    }

    private final MinesweeperTileType type;
    private final int data;

    private MinesweeperTileData(@NotNull MinesweeperTileType type, int data) {
        this.type = type;
        this.data = data;
    }

    public @NotNull MinesweeperTileType type() {
        return this.type;
    }

    public int data() {
        return this.data;
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
