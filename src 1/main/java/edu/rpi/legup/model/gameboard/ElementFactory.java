package edu.rpi.legup.model.gameboard;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import edu.rpi.legup.save.InvalidFileFormatException;

public abstract class ElementFactory {

    /**
     * Creates a {@link PuzzleElement} based on the xml document Node and adds it to the board.
     *
     * @param node  node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException thrown if the xml node is invalid for the specific puzzle element
     */
    public abstract PuzzleElement importCell(Node node, Board board) throws InvalidFileFormatException;

    /**
     * Creates a xml document {@link PuzzleElement} from a cell for exporting.
     *
     * @param document xml document
     * @param puzzleElement     PuzzleElement cell
     * @return xml PuzzleElement
     */
    public abstract Element exportCell(Document document, PuzzleElement puzzleElement);
}