package edu.rpi.legup.model.gameboard;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import edu.rpi.legup.save.InvalidFileFormatException;

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
    public abstract Element importCell(Node node, Board board) throws InvalidFileFormatException;

    /**
     * Creates a xml document element from a cell for exporting
     *
     * @param document xml document
     * @param data Element cell
     * @return xml Element
     */
    public abstract org.w3c.dom.Element exportCell(Document document, Element data);
}