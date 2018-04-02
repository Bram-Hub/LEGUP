package model.gameboard;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import save.InvalidFileFormatException;

public abstract class ElementFactory
{
    /**
     * Creates a element based on the xml document Node and adds it to the board
     *
     * @param node node that represents the element
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException
     */
    public abstract ElementData importCell(Node node, Board board) throws InvalidFileFormatException;

    /**
     * Creates a xml document element from a cell for exporting
     *
     * @param document xml document
     * @param data ElementData cell
     * @return xml Element
     */
    public abstract Element exportCell(Document document, ElementData data);
}