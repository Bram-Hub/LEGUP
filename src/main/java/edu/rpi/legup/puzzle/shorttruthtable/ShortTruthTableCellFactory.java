package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ShortTruthTableCellFactory extends ElementFactory {
    /**
     * Creates a puzzleElement based on the xml document Node and adds it to the board
     *
     * @param node node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public ShortTruthTableCell importCell(Node node, Board board)
            throws InvalidFileFormatException {

        try {
            if (!node.getNodeName().equalsIgnoreCase("cell")) {
                throw new InvalidFileFormatException(
                        "ShortTruthTable Factory: unknown puzzleElement puzzleElement");
            }

            ShortTruthTableBoard sttBoard = (ShortTruthTableBoard) board;

            // get the attributes for the cell
            NamedNodeMap attributeList = node.getAttributes();
            int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());
            int charIndex =
                    Integer.valueOf(attributeList.getNamedItem("char_index").getNodeValue());
            String cellType = attributeList.getNamedItem("type").getNodeValue();

            // modify the appropriate cell
            ShortTruthTableCell cell =
                    (ShortTruthTableCell) sttBoard.getCell(charIndex, rowIndex * 2);
            cell.setData(ShortTruthTableCellType.valueOf(cellType));

            return cell;

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException(
                    "nurikabe Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("nurikabe Factory: could not find attribute(s)");
        }
    }


    /**
     * Creates a xml document puzzleElement from a cell for exporting
     *
     * @param document xml document
     * @param puzzleElement PuzzleElement cell
     * @return xml PuzzleElement
     */
    public org.w3c.dom.Element exportCell(Document document, PuzzleElement puzzleElement) {

        ShortTruthTableCell cell = (ShortTruthTableCell) puzzleElement;

        org.w3c.dom.Element cellElement = document.createElement("cell");
        cellElement.setAttribute("row_index", String.valueOf(cell.getY() / 2));
        cellElement.setAttribute("char_index", String.valueOf(cell.getX()));
        cellElement.setAttribute("type", String.valueOf(cell.getType()));

        return cellElement;
    }
}
