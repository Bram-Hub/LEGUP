package edu.rpi.legup.puzzle.fillapix;

import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public record FillapixTileData(FillapixTileType type, int data) {

    public static final int UNSET_DATA = -2;
    public static final int BLACK_DATA = -1;
    public static final int WHITE_DATA = 0;

    /** Static instances for common types */
    private static final FillapixTileData UNSET =
            new FillapixTileData(FillapixTileType.UNSET, UNSET_DATA);
    private static final FillapixTileData BLACK =
            new FillapixTileData(FillapixTileType.BLACK, BLACK_DATA);
    private static final FillapixTileData WHITE =
            new FillapixTileData(FillapixTileType.WHITE, WHITE_DATA);


    @Contract(pure = true)
    public static @NotNull FillapixTileData number(int count) {
        return new FillapixTileData(FillapixTileType.NUMBER, count);
    }

    /**
     * Returns the corresponding tile type from raw data.
     *
     * @param data integer representing type (-2 unset, -1 black, 0 white, 1–9 number)
     */
    @Contract(pure = true)
    public static @NotNull FillapixTileData fromData(int data) {
        return switch (data) {
            case UNSET_DATA -> unset();
            case BLACK_DATA -> black();
            case WHITE_DATA -> white();
            default -> {
                if (data < 0 || data > 9) {
                    yield unset();
                }
                yield number(data);
            }
        };
    }

    public static @NotNull FillapixTileData unset() {
        return UNSET;
    }

    public static @NotNull FillapixTileData black() {
        return BLACK;
    }

    public static @NotNull FillapixTileData white() {
        return WHITE;
    }

    /** Constructor to accept FillapixCellType instead of FillapixTileType */
    public FillapixTileData(FillapixCellType cellType, int data) {
        this(switch (cellType) {
            case UNKNOWN -> FillapixTileType.UNSET;
            case BLACK   -> FillapixTileType.BLACK;
            case WHITE   -> FillapixTileType.WHITE;
        }, data);
    }


    public boolean isUnset() {
        return this.data == UNSET_DATA;
    }

    public boolean isBlack() {
        return this.data == BLACK_DATA;
    }

    public boolean isWhite() {
        return this.data == WHITE_DATA;
    }

    public boolean isNumber() {
        return this.data >= 0 && this.data <= 9 && type == FillapixTileType.NUMBER;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillapixTileData that = (FillapixTileData) o;
        return data == that.data && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data);
    }
}
