package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class MinesweeperCellFactory extends ElementFactory {

    /** The key of the data used in {@link NamedNodeMap} */
    private static final String DATA_ATTRIBUTE = "value";

    /** The key of the x position used in {@link NamedNodeMap} */
    private static final String X_ATTRIBUTE = "x";

    /** The key of the y position used in {@link NamedNodeMap} */
    private static final String Y_ATTRIBUTE = "y";

    private MinesweeperCellFactory() {}

    public static final MinesweeperCellFactory INSTANCE = new MinesweeperCellFactory();

    /**
     * @param node node that represents the puzzleElement
     * @param board Board to use to verify the newly created {@link MinesweeperCell} is valid
     * @return a new {@link MinesweeperCell}
     * @throws InvalidFileFormatException If the node name is not "cell"
     * @throws NumberFormatException If the {@link #X_ATTRIBUTE} or {@link #Y_ATTRIBUTE} is not a
     *     number
     * @throws NullPointerException If one of {@link #DATA_ATTRIBUTE}, {@link #X_ATTRIBUTE} or
     *     {@link #Y_ATTRIBUTE} does not exist.
     */
    @Override
    @Contract(pure = false)
    public @NotNull PuzzleElement<MinesweeperTileData> importCell(
            @NotNull Node node, @NotNull Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "Minesweeper Factory: unknown puzzleElement puzzleElement");
            }

            MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board;
            final int width = minesweeperBoard.getWidth();
            final int height = minesweeperBoard.getHeight();

            final NamedNodeMap attributeList = node.getAttributes();
            final int value =
                    Integer.parseInt(attributeList.getNamedItem(DATA_ATTRIBUTE).getNodeValue());
            final int x = Integer.parseInt(attributeList.getNamedItem(X_ATTRIBUTE).getNodeValue());
            final int y = Integer.parseInt(attributeList.getNamedItem(Y_ATTRIBUTE).getNodeValue());
            if (x >= width || y >= height) {
                throw new InvalidFileFormatException(
                        "Minesweeper Factory: cell location out of bounds");
            }
            if (value < -2) {
                throw new InvalidFileFormatException("Minesweeper Factory: cell unknown value");
            }
            final MinesweeperCell cell =
                    new MinesweeperCell(MinesweeperTileData.fromData(value), new Point(x, y));
            cell.setIndex(y * height + x);
            if (value != -2) {
                cell.setModifiable(false);
            }
            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "Minesweeper Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException(
                    "Minesweeper Factory: could not find attribute(s)");
        }
    }


    /**
     * @param document Document used to create the element
     * @param puzzleElement PuzzleElement cell
     * @return a {@link Element} that contains the {@link #DATA_ATTRIBUTE}, {@link #X_ATTRIBUTE},
     *     and {@link #Y_ATTRIBUTE}
     */
    @Override
    @Contract(pure = false)
    public @NotNull Element exportCell(
            @NotNull Document document,
            @SuppressWarnings("rawtypes") @NotNull PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        MinesweeperCell cell = (MinesweeperCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute(DATA_ATTRIBUTE, String.valueOf(cell.getData()));
        cellElement.setAttribute(X_ATTRIBUTE, String.valueOf(loc.x));
        cellElement.setAttribute(Y_ATTRIBUTE, String.valueOf(loc.y));

        return cellElement;
    }
}
