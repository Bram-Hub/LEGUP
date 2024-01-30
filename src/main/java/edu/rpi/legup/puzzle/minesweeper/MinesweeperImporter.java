package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class MinesweeperImporter extends PuzzleImporter {

    public MinesweeperImporter(@NotNull Minesweeper minesweeper) {
        super(minesweeper);
    }

    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    @Override
    public boolean acceptsTextInput() {
        return false;
    }

    @Override
    public void initializeBoard(int rows, int columns) {
        MinesweeperBoard minesweeperBoard = new MinesweeperBoard(columns, rows);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                MinesweeperCell cell = new MinesweeperCell(MinesweeperTileData.unset(), new Point(x, y));
                cell.setIndex(y * columns + x);
                cell.setModifiable(true);
                minesweeperBoard.setCell(x, y, cell);
            }
        }
        puzzle.setCurrentBoard(minesweeperBoard);
    }

    @Override
    public void initializeBoard(@NotNull Node node) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("Minesweeper Importer: cannot find board puzzleElement");
            }
            final Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("Minesweeper Importer: no puzzleElement found for board");
            }
            final Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            final NodeList elementDataList = dataElement.getElementsByTagName("cell");

            final MinesweeperBoard minesweeperBoard = getMinesweeperBoard(boardElement);

            final int width = minesweeperBoard.getWidth();
            final int height = minesweeperBoard.getHeight();

            for (int i = 0; i < elementDataList.getLength(); i++) {
                final MinesweeperCell cell = (MinesweeperCell) puzzle.getFactory().importCell(elementDataList.item(i), minesweeperBoard);
                final Point loc = cell.getLocation();
                if (MinesweeperTileData.unset().equals(cell.getData())) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                minesweeperBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (minesweeperBoard.getCell(x, y) == null) {
                        final MinesweeperCell cell = new MinesweeperCell(MinesweeperTileData.unset(), new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        minesweeperBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(minesweeperBoard);
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("Minesweeper Importer: unknown value where integer expected");
        }
    }

    private static @NotNull MinesweeperBoard getMinesweeperBoard(@NotNull Element boardElement) throws InvalidFileFormatException {
        MinesweeperBoard minesweeperBoard = null;
        if (!boardElement.getAttribute("size").isEmpty()) {
            final int size = Integer.parseInt(boardElement.getAttribute("size"));
            minesweeperBoard = new MinesweeperBoard(size);
        } else {
            if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                final int width = Integer.parseInt(boardElement.getAttribute("width"));
                final int height = Integer.parseInt(boardElement.getAttribute("height"));
                minesweeperBoard = new MinesweeperBoard(width, height);
            }
        }

        if (minesweeperBoard == null) {
            throw new InvalidFileFormatException("Minesweeper Importer: invalid board dimensions");
        }
        return minesweeperBoard;
    }

    @Override
    public void initializeBoard(@NotNull String[] statements) throws UnsupportedOperationException, IllegalArgumentException {
        throw new UnsupportedOperationException("Minesweeper does not support text input.");
    }
}
