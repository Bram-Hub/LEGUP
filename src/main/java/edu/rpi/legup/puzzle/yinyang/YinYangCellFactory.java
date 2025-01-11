package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class YinYangCellFactory extends ElementFactory {

    /**
     * Creates a puzzleElement based on the XML document Node and adds it to the board.
     *
     * @param node  Node that represents the puzzleElement
     * @param board Board to add the newly created cell
     * @return newly created cell from the XML document Node
     * @throws InvalidFileFormatException if the file is invalid
     */
    @Override
    public YinYangCell importCell(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "YinYangCellFactory: Unknown puzzle element");
            }

            YinYangBoard yinYangBoard = (YinYangBoard) board;
            int width = yinYangBoard.getWidth();
            int height = yinYangBoard.getHeight();

            NamedNodeMap attributeList = node.getAttributes();
            int x = Integer.parseInt(attributeList.getNamedItem("x").getNodeValue());
            int y = Integer.parseInt(attributeList.getNamedItem("y").getNodeValue());
            String typeString = attributeList.getNamedItem("type").getNodeValue();
            YinYangType type = YinYangType.valueOf(typeString.toUpperCase());

            if (x >= width || y >= height) {
                throw new InvalidFileFormatException(
                        "YinYangCellFactory: Cell location out of bounds");
            }

            YinYangCell cell = new YinYangCell(type, x, y);
            cell.setIndex(y * width + x);
            cell.setModifiable(true); // Ensure modifiable state is set for imported cells
            cell.setGiven(type != YinYangType.UNKNOWN); // Mark non-unknown cells as given
            return cell;

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "YinYangCellFactory: Unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("YinYangCellFactory: Missing attribute(s)");
        } catch (IllegalArgumentException e) {
            throw new InvalidFileFormatException("YinYangCellFactory: Invalid cell type");
        }
    }

    /**
     * Creates an XML document puzzleElement from a cell for exporting.
     *
     * @param document      XML document
     * @param puzzleElement PuzzleElement cell
     * @return XML PuzzleElement
     */
    @Override
    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {
        org.w3c.dom.Element cellElement = document.createElement("cell");

        YinYangCell cell = (YinYangCell) puzzleElement;

        cellElement.setAttribute("x", String.valueOf(cell.getLocation().x));
        cellElement.setAttribute("y", String.valueOf(cell.getLocation().y));
        cellElement.setAttribute("type", cell.getType().toString());

        return cellElement;
    }

    /**
     * Validates the structure and properties of a cell before importing.
     *
     * @param x     The x-coordinate of the cell
     * @param y     The y-coordinate of the cell
     * @param type  The type of the cell
     * @param board The board to which the cell belongs
     * @throws InvalidFileFormatException if the cell is invalid
     */
    private void validateCell(int x, int y, YinYangType type, YinYangBoard board) throws InvalidFileFormatException {
        if (x < 0 || x >= board.getWidth() || y < 0 || y >= board.getHeight()) {
            throw new InvalidFileFormatException("YinYangCellFactory: Cell coordinates out of bounds");
        }
        if (type == null) {
            throw new InvalidFileFormatException("YinYangCellFactory: Cell type is null");
        }
    }
}
