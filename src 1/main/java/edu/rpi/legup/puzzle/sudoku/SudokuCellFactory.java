package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.awt.*;

public class SudokuCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node  node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException
     */
    @Override
    public SudokuCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException("Sudoku Factory: unknown puzzleElement puzzleElement");
            }

            SudokuBoard sudokuBoard = (SudokuBoard) board;
            int size = sudokuBoard.getSize();
            int minorSize = (int) Math.sqrt(size);

            NamedNodeMap attributeList = node.getAttributes();
            int value = Integer.valueOf(attributeList.getNamedItem("value").getNodeValue());
            int x = Integer.valueOf(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.valueOf(attributeList.getNamedItem("y").getNodeValue());
            if (x >= size || y >= size) {
                throw new InvalidFileFormatException("Sudoku Factory: cell location out of bounds");
            }
            if (value < 0 || value > 9) {
                throw new InvalidFileFormatException("Sudoku Factory: cell unknown value");
            }
            int groupIndex = x / minorSize + y / minorSize * minorSize;
            SudokuCell cell = new SudokuCell(value, new Point(x, y), groupIndex);
            cell.setIndex(y * size + x);
            return cell;
        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("Sudoku Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("Sudoku Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document puzzleElement from a cell for exporting
     *
     * @param document xml document
     * @param puzzleElement     PuzzleElement cell
     * @return xml PuzzleElement
     */
    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        SudokuCell cell = (SudokuCell) puzzleElement;
        Point loc = cell.getLocation();

        cellElement.setAttribute("value", String.valueOf(cell.getData()));
        cellElement.setAttribute("x", String.valueOf(loc.x));
        cellElement.setAttribute("y", String.valueOf(loc.y));

        return cellElement;
    }
}
